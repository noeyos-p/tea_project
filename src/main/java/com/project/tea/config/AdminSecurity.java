package com.project.tea.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class AdminSecurity {

    // 관리자: admin@admin.com / 1111 (ROLE_ADMIN)
    @Bean(name = "adminUserDetailsService") // ← 명시적 이름 부여 (UserDetailsService 중복 대비)
    public UserDetailsService adminUserDetailsService(BCryptPasswordEncoder encoder) {
        return new InMemoryUserDetailsManager(
                User.withUsername("admin@admin.com")
                        .password(encoder.encode("1111"))
                        .roles("ADMIN")
                        .build()
        );
    }

    // 인메모리 인증 Provider
    @Bean(name = "adminAuthProvider") // ← 명시적 이름 부여
    public DaoAuthenticationProvider adminAuthProvider(
            @Qualifier("adminUserDetailsService") UserDetailsService adminUds,
            BCryptPasswordEncoder encoder) {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(adminUds);
        p.setPasswordEncoder(encoder);
        return p;
    }
}