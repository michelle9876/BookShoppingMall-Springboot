package com.github.ecommerce.web.dto.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class SecessionResponse {
    private LocalDateTime deletedAt;
}
