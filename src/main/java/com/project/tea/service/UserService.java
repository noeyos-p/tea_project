package com.project.tea.service;

import com.project.tea.entity.UserEntity;
import com.project.tea.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * UserService
 * - 로그인한 유저 정보 조회
 * - 회원 관련 기능 담당
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 현재 로그인한 유저 ID 반환
     * (예: SecurityContext에서 Authentication 가져오기)
     * @return 로그인한 유저 ID
     */
    public Long getCurrentUserId() {
        // 예시: SecurityContext에서 로그인한 유저명 조회
        String username = getCurrentUsername();

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("로그인한 유저를 찾을 수 없습니다."));
        return user.getId();
    }

    /**
     * 현재 로그인한 유저명 조회
     * 실제 구현 시 SecurityContext 사용
     */
    private String getCurrentUsername() {
        // TODO: 실제 SecurityContext 사용
        return "exampleUser";
    }
}
