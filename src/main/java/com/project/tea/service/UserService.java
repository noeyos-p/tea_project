package com.project.tea.service;

import com.project.tea.entity.UserEntity;
import com.project.tea.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /** 로그인된 사용자 ID 반환 */
    public Long getCurrentUserId() {
        String email = getCurrentUserEmail();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("로그인한 유저를 찾을 수 없습니다: " + email));
        return user.getId();
    }

    private String getCurrentUserEmail() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("인증 정보 없음");
        }
        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails ud) return ud.getUsername();
        if (principal instanceof String s) return s;
        throw new RuntimeException("알 수 없는 Principal: " + principal);
    }

    /** 회원가입 */
    @Transactional
    public void signup(String email, String password, String confirmPassword, String nickname) {
        if (userRepository.findByEmail(email).isPresent())
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        if (!Objects.equals(password, confirmPassword))
            throw new IllegalArgumentException("비밀번호 확인이 일치하지 않습니다.");
        if (nickname == null || nickname.isBlank())
            throw new IllegalArgumentException("닉네임을 입력해주세요.");

        UserEntity u = UserEntity.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .build();
        userRepository.save(u);
    }
}
