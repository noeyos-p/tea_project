package com.project.tea.service;

import com.project.tea.entity.*;
import com.project.tea.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * UserDataService
 * - 유저가 선택한 티와 Mood/State 정보를 마이페이지에 저장
 * - 사용자 기록 조회
 */
@Service
@RequiredArgsConstructor
public class UserDataService {

    private final UserDataRepository userDataRepository;
    private final UserRepository userRepository;
    private final TeaRepository teaRepository;
    private final MoodRepository moodRepository;
    private final StateRepository stateRepository;

    /**
     * UserData 저장
     *
     * @param userId  로그인 유저 ID
     * @param teaId   선택한 티 ID
     * @param moodId  Mood ID (선택한 경우)
     * @param stateId State ID (선택한 경우)
     * @param memo    사용자 메모 (선택 사항)
     */
    public void saveUserData(Long userId, Long teaId, Long moodId, Long stateId, String memo) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다: " + userId));

        TeaEntity tea = teaRepository.findById(teaId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 티입니다: " + teaId));

        MoodEntity mood = null;
        if (moodId != null) {
            mood = moodRepository.findById(moodId)
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 Mood입니다: " + moodId));
        }

        StateEntity state = null;
        if (stateId != null) {
            state = stateRepository.findById(stateId)
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 State입니다: " + stateId));
        }

        UserDataEntity userData = UserDataEntity.builder()
                .user(user)
                .tea(tea)
                .mood(mood)
                .state(state)
                .memo(memo)
                .date(LocalDate.now())
                .updateDate(LocalDateTime.now())
                .build();

        userDataRepository.save(userData);
    }

    /**
     * 유저별 UserData 조회
     *
     * @param userId 조회할 유저 ID
     * @return UserDataEntity 리스트
     */
    public List<UserDataEntity> getUserDataByUserId(Long userId) {
        return userDataRepository.findAll()
                .stream()
                .filter(data -> data.getUser().getId().equals(userId))
                .toList(); // List<UserDataEntity> 반환
    }
}
