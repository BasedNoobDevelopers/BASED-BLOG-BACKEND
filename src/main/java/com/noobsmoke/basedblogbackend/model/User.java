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
@AllArgsConstructor
public class User {
    private long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private LocalDate createdDate;
    private String avatar;
    private List<String> favorite_topics;
}

