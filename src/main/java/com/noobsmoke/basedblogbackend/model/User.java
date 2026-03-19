package com.noobsmoke.basedblogbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "password")
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String email;
    private LocalDateTime createdDate;
    private String avatar;
    private List<String> favoriteTopics;

    private User(Builder builder) {
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.userName = builder.userName;
        this.password = builder.password;
        this.email = builder.email;
        this.createdDate = builder.createdDate;
        this.avatar = builder.avatar;
        this.favoriteTopics = builder.favoriteTopics;
    }

    public static class Builder {
        private Long id;
        private String lastName;
        private String firstName;
        private String userName;
        private String password;
        private String email;
        private LocalDateTime createdDate;
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

