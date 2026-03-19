package com.noobsmoke.basedblogbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@ToString
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private LocalDate createdDate;
    private String avatar;
    private List<String> favorite_topics;

    private User(Builder builder) {
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.userName = builder.userName;
        this.password = builder.password;
        this.createdDate = builder.createdDate;
        this.avatar = builder.avatar;
        this.favorite_topics = builder.favorite_topics;
    }

    public static class Builder {
        private Long id;
        private String lastName;
        private String firstName;
        private String userName;
        private String password;
        private LocalDate createdDate;
        private String avatar;
        private List<String> favorite_topics;

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

        public Builder createdAt(LocalDate createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Builder avatar(String avatar) {
            this.avatar = avatar;
            return this;
        }

        public Builder favoriteTopics(List<String> favorite_topics) {
            this.favorite_topics = favorite_topics;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}

