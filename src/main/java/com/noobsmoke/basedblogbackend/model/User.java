package com.noobsmoke.basedblogbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "password")
public class User implements UserDetails {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private LocalDateTime createdDate;
    private boolean enabled;
    private String avatar;
    private String verificationCode;
    private LocalDateTime verificationExpirationAt;
    private List<String> favoriteTopics;

    private User(Builder builder) {
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.username = builder.userName;
        this.password = builder.password;
        this.email = builder.email;
        this.createdDate = builder.createdDate;
        this.verificationCode = builder.verificationCode;
        this.verificationExpirationAt = builder.verificationExpirationAt;
        this.enabled = builder.enabled;
        this.avatar = builder.avatar;
        this.favoriteTopics = builder.favoriteTopics;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public static class Builder {
        private Long id;
        private String lastName;
        private String firstName;
        private String userName;
        private String password;
        private String email;
        private LocalDateTime createdDate;
        private boolean enabled;
        private String verificationCode;
        private LocalDateTime verificationExpirationAt;
        private String avatar;
        private List<String> favoriteTopics;

        public Builder() {

        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder createdAt(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder verificationCode(String verificationCode) {
            this.verificationCode = verificationCode;
            return this;
        }

        public Builder verificationExpirationAt(LocalDateTime verificationExpirationAt) {
            this.verificationExpirationAt = verificationExpirationAt;
            return this;
        }

        public Builder avatar(String avatar) {
            this.avatar = avatar;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder favoriteTopics(List<String> favorite_topics) {
            this.favoriteTopics = favorite_topics;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}

