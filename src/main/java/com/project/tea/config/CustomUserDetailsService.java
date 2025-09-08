package com.project.tea.config;

import com.project.tea.entity.UserEntity;
import com.project.tea.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity u = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("이메일 없음: " + email));

        return User.builder()
                .username(u.getEmail())
                .password(u.getPassword())
                .authorities("ROLE_USER") // 권한이 없어도 Spring Security는 최소 하나 필요
                .build();
    }
}