package com.project.tea.service;

import com.project.tea.entity.*;
import com.project.tea.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDataService {

    private final UserDataRepository userDataRepository;
    private final UserRepository userRepository;
    private final TeaRepository teaRepository;
    private final MoodRepository moodRepository;
    private final StateRepository stateRepository;

    /**
     * 차 + Mood/State 선택 후 기록 저장 (메모는 아직 null)
     */
    @Transactional
    public Long saveUserData(Long userId, Long teaId, Long moodId, Long stateId) {
        UserEntity user = userRepository.findById(userId).orElseThrow();
        TeaEntity tea = teaRepository.findById(teaId).orElseThrow();
        MoodEntity mood = moodId != null ? moodRepository.findById(moodId).orElse(null) : null;
        StateEntity state = stateId != null ? stateRepository.findById(stateId).orElse(null) : null;

        UserDataEntity data = UserDataEntity.builder()
                .user(user)
                .tea(tea)
                .mood(mood)
                .state(state)
                .date(LocalDate.now())
                .memo(null) // 메모는 나중에 입력
                .updateDate(LocalDateTime.now())
                .build();

        userDataRepository.save(data);
        return data.getId();
    }

    /**
     *  기록된 ID 기준으로 메모 작성/수정
     */
    @Transactional
    public void saveMemo(Long userDataId, String memo) {
        UserDataEntity data = userDataRepository.findById(userDataId)
                .orElseThrow(() -> new IllegalArgumentException("해당 기록이 존재하지 않습니다: " + userDataId));
        data.setMemo(memo); // 메모수정처리
        data.setUpdateDate(LocalDateTime.now()); //날짜는 계속 업댓됨 시간 넣을지말지고민
    }


    /**
     *  (메모 작성/수정 페이지)
     */
    public UserDataEntity getUserDataById(Long userDataId) {
        return userDataRepository.findById(userDataId)
                .orElseThrow(() -> new IllegalArgumentException("해당 기록이 존재하지 않습니다: " + userDataId));
    }

    /**
     * 특정 유저의 전체 기록 조회
     */
    public List<UserDataEntity> getUserDataByUserId(Long userId) {
        return userDataRepository.findByUserId(userId);
    }
}
