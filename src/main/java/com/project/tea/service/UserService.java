//package com.project.tea.service;
//
//import com.project.tea.entity.UserEntity;
//import com.project.tea.repository.UserRepository;
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import lombok.Setter;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Objects;
//
//@Service
//@RequiredArgsConstructor
//public class UserService {
//
//    private final UserRepository userRepository;
//    private final BCryptPasswordEncoder passwordEncoder;
//
//    /** 로그인된 사용자 ID 반환 */
//    public Long getCurrentUserId() {
//        String email = getCurrentUserEmail();
//        UserEntity user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("로그인한 유저를 찾을 수 없습니다: " + email));
//        return user.getId();
//    }
//
//    private String getCurrentUserEmail() {
//        var auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth == null || !auth.isAuthenticated()) {
//            throw new RuntimeException("인증 정보 없음");
//        }
//        Object principal = auth.getPrincipal();
//        if (principal instanceof UserDetails ud) return ud.getUsername();
//        if (principal instanceof String s) return s;
//        throw new RuntimeException("알 수 없는 Principal: " + principal);
//    }
//
//    /** 회원가입 */
//    @Transactional
//    public void signup(String email, String password, String confirmPassword, String nickname) {
//        if (userRepository.findByEmail(email).isPresent())
//            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
//        if (!Objects.equals(password, confirmPassword))
//            throw new IllegalArgumentException("비밀번호 확인이 일치하지 않습니다.");
//        if (nickname == null || nickname.isBlank())
//            throw new IllegalArgumentException("닉네임을 입력해주세요.");
//
//        UserEntity u = UserEntity.builder()
//                .email(email)
//                .password(passwordEncoder.encode(password))
//                .nickname(nickname)
//                .build();
//        userRepository.save(u);
//    }
//}

//              기존 UserService 주석 처리

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

   // 로그인된 사용자 ID 반환
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

    // 단일 사용자 조회
    public UserEntity getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));
    }

    // 회원가입 (이메일/닉네임 중복 금지, 비밀번호 확인, 길이 검증)
    @Transactional
    public void signup(String email, String password, String confirmPassword, String nickname) {
        if (email == null || email.isBlank())
            throw new IllegalArgumentException("이메일을 입력해주세요.");
        if (userRepository.findByEmail(email).isPresent())
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");

        if (nickname == null || nickname.isBlank())
            throw new IllegalArgumentException("닉네임을 입력해주세요.");
        if (userRepository.existsByNickname(nickname))
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");

        if (!Objects.equals(password, confirmPassword))
            throw new IllegalArgumentException("비밀번호 확인이 일치하지 않습니다.");
        if (password == null || password.length() < 8)
            throw new IllegalArgumentException("비밀번호는 8자 이상이어야 합니다.");

        UserEntity u = UserEntity.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .build();
        userRepository.save(u);
    }

   // 닉네임 변경 (중복 불가, 동일 닉네임이면 무시)
    @Transactional
    public void updateNickname(Long userId, String newNickname) {
        if (newNickname == null || newNickname.isBlank())
            throw new IllegalArgumentException("닉네임을 입력해주세요.");

        UserEntity user = getById(userId);

        // 동일 닉네임이면 변경 불필요
        if (newNickname.equals(user.getNickname())) return;

        if (userRepository.existsByNickname(newNickname))
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");

        user.setNickname(newNickname);
        userRepository.save(user);
    }

    // 비밀번호 변경 (8자 이상, 기존 비밀번호 재사용 금지)
    @Transactional
    public void updatePassword(Long userId, String rawPassword) {
        if (rawPassword == null || rawPassword.isBlank())
            throw new IllegalArgumentException("비밀번호를 입력해주세요.");
        if (rawPassword.length() < 8)
            throw new IllegalArgumentException("비밀번호는 8자 이상이어야 합니다.");

        UserEntity user = getById(userId);

        // 기존 비밀번호와 동일하면 금지
        if (passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("기존 비밀번호와 동일한 비밀번호는 사용할 수 없습니다.");
        }

        user.setPassword(passwordEncoder.encode(rawPassword));
        userRepository.save(user);
    }
}
