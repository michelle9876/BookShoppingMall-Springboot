package com.github.ecommerce.service.security;

import com.github.ecommerce.data.entity.auth.User;
import com.github.ecommerce.data.repository.auth.AuthRepository;
import com.github.ecommerce.web.dto.auth.Authority;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Primary
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final AuthRepository authRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User userPrincipal = authRepository.findByEmail(email).orElseThrow(()
                -> new UsernameNotFoundException("email 에 해당하는 UserPrincipal가 없습니다"));

        Set<Authority> roles = userPrincipal.getAuthorities();

        return CustomUserDetails.builder()
                .userId(userPrincipal
                        .getUserId())
                .email(userPrincipal.getEmail())
                .password(userPrincipal.getPassword())
                .userName(userPrincipal.getUserName())
                .authorities(roles)
                .build();
    }
}
