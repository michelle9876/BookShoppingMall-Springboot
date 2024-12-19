package com.github.ecommerce.service.security;

import com.github.ecommerce.web.dto.auth.Authority;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomUserDetails implements UserDetails {

    private Integer userId;
    private String email;
    private String password;
    private String userName;
    private Set<Authority> authorities;

    public Integer getUserId() {
        return userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities.stream().map(Authority::name).map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    public Set<Authority> getAuthoritySet() {
        return authorities;
    }
    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
