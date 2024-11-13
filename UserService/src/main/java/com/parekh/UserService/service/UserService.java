package com.parekh.UserService.service;

import com.parekh.UserService.model.User;
import com.parekh.UserService.model.MyUserDetails;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public List<User> getAllUsers();

    public User saveUser(User user);

    public User updateUser(Long id, User updatedUser);

    public void deleteUser(Long id);

    public Optional<User> findUserByUsername(String username);

    public Optional<MyUserDetails> findUserDetails(String username);
}
