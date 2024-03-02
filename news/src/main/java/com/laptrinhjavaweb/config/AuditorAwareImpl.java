package com.laptrinhjavaweb.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // Trong trường hợp này, bạn có thể trả về tên người dùng hoặc ID của người dùng
            // Tùy thuộc vào cách bạn muốn lưu trữ thông tin người tạo.
            return Optional.of(authentication.getName());
        }
        return Optional.empty();
    }
}
