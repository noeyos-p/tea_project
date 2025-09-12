package com.project.tea.service;

import com.project.tea.entity.*;
import com.project.tea.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDataService {

    private final UserDataRepository userDataRepository;
    private final UserRepository userRepository;
    private final TeaRepository teaRepository;
    private final MoodRepository moodRepository;
    private final StateRepository stateRepository;

    @PersistenceContext
    private EntityManager entityManager; // ✅ 중복 제거, 하나만 유지

    /**
     * 차 + Mood/State 선택 후 기록 저장 (메모는 아직 null)
     */
    // 기존: public UserDataEntity getOrCreateToday(Long userId)
    @Transactional
    public Long saveUserData(Long userId, Long teaId, Long moodId, Long stateId) {
        UserEntity user = userRepository.findById(userId).orElseThrow();
        TeaEntity tea = teaRepository.findById(teaId)
                .orElseThrow(() -> new IllegalArgumentException("차가 존재하지 않습니다: " + teaId));
        MoodEntity mood = moodId != null ? moodRepository.findById(moodId).orElse(null) : null;
        StateEntity state = stateId != null ? stateRepository.findById(stateId).orElse(null) : null;

        UserDataEntity data = UserDataEntity.builder()
                .user(user)
                .tea(tea)
                .mood(mood)
                .state(state)
                .date(LocalDate.now(ZoneId.of("Asia/Seoul")))
                .memo(null)
                .updateDate(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
                .build();

        return userDataRepository.save(data).getId();
    }



    /**
     * 기록된 ID 기준으로 메모 작성/수정
     */
    @Transactional
    public void saveMemo(Long userDataId, String memo) {
        UserDataEntity data = userDataRepository.findById(userDataId)
                .orElseThrow(() -> new IllegalArgumentException("해당 기록이 존재하지 않습니다: " + userDataId));
        data.setMemo(memo); // 메모 수정
        data.setUpdateDate(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
    }

    /**
     * (메모 작성/수정 페이지)
     */
    public UserDataEntity getUserDataById(Long userDataId) {
        return userDataRepository.findById(userDataId)
                .orElseThrow(() -> new IllegalArgumentException("해당 기록이 존재하지 않습니다: " + userDataId));
    }

    // 최신 메모 반환
    public UserDataEntity getTodayMemo(Long userId) {
        List<UserDataEntity> todayMemos = userDataRepository.findTodayMemos(userId, LocalDate.now(ZoneId.of("Asia/Seoul")));
        return todayMemos.isEmpty() ? null : todayMemos.get(0);
    }

    /**
     * 특정 유저의 전체 기록 조회
     */
    public List<UserDataEntity> getUserDataByUserId(Long userId) {
        return userDataRepository.findByUserId(userId);
    }

    public Optional<UserDataEntity> findByUserAndDate(Long userId, LocalDate date) {
        return userDataRepository.findByUser_IdAndDate(userId, date);
    }

    /**
     * 오늘자 데이터 저장 또는 기존 데이터 반환
     */
    @Transactional
    public SaveResult saveOrGetToday(Long userId, Long teaId, Long moodId, Long stateId) {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));

        // 이미 오늘 기록이 있으면 그대로 반환
        UserDataEntity existed = userDataRepository.findTopByUser_IdAndDateOrderByUpdateDateDesc(userId, today);
        if (existed != null) {
            return new SaveResult(existed.getId(), true);
        }

        // 없으면 새로 생성
        Long id = saveUserData(userId, teaId, moodId, stateId);
        return new SaveResult(id, false);
    }

    // ✅ SaveResult DTO
    @Value
    public static class SaveResult {
        Long id;
        boolean already;
    }

    // 오늘자 userData 가져오거나 없으면 새로 생성
    public UserDataEntity getOrCreateToday(Long userId) {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        return userDataRepository.findByUser_IdAndDate(userId, today)
                .orElseGet(() -> {
                    UserEntity user = userRepository.getReferenceById(userId);
                    UserDataEntity e = UserDataEntity.builder()
                            .user(user)
                            .date(today)
                            .build();
                    return userDataRepository.save(e);
                });
    }

    // TeaEntity 프록시 반환 (DB hit 최소화)
    public TeaEntity getTeaReference(Long teaId) {
        return entityManager.getReference(TeaEntity.class, teaId);
    }

    // 오늘 메모가 있는지 여부
    public boolean existsMemoToday(Long userId) {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        return userDataRepository.existsByUser_IdAndDateAndMemoIsNotNull(userId, today);
    }

    @Transactional
    public void saveOnlyTea(UserDataEntity e) {
        userDataRepository.save(e);
        entityManager.flush();
        entityManager.clear(); // ✅ 1차 캐시 초기화
    }
}
