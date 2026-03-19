package com.noobsmoke.basedblogbackend.repository;

import com.noobsmoke.basedblogbackend.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class FakeRepo {

    private List<User> userList = new ArrayList<>();
    AtomicLong atomicLong = new AtomicLong(0);

    public void addUser(User newUser) {
        newUser.setId(atomicLong.getAndIncrement());
        userList.add(newUser);
    }

    public boolean containsUsername(String username) {
        return userList.stream()
                .anyMatch(user -> user.getUserName().equalsIgnoreCase(username));
    }

    public User findUserByUsername(String username) {
        return userList.stream().filter(user -> user.getUserName()
                .equalsIgnoreCase(username)).findFirst()
                .orElseThrow(() ->new RuntimeException("User with the " + username + " cannot be found"));
    }

   public User findUserByUserNameAndPassword(String username, String password) {
        return userList.stream().filter(user -> user.getUserName().equalsIgnoreCase(username
        ) && user.getPassword().equalsIgnoreCase(password)).findFirst()
                .orElseThrow(() -> new RuntimeException("Incorrect credentials"));
   }

   public List<User> findAllUsers() {
        return userList;
   }
}
