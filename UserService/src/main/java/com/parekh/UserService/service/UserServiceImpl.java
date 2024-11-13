package com.parekh.UserService.service;

import com.parekh.UserService.exception.UserNotFoundException;
import com.parekh.UserService.model.User;
import com.parekh.UserService.model.MyUserDetails;
import com.parekh.UserService.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        logger.info("Task started");
        return userRepository.findAll();
    }

    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User updatedUser) {
        // Find the existing user by ID
        return userRepository.findById(id).map(user -> {
            // Update user fields only if they're provided (non-null or non-empty)
            user.setUsername(updatedUser.getUsername() != null ? updatedUser.getUsername() : user.getUsername());
            user.setEmail(updatedUser.getEmail() != null ? updatedUser.getEmail() : user.getEmail());
            user.setFirstName(updatedUser.getFirstName() != null ? updatedUser.getFirstName() : user.getFirstName());
            user.setLastName(updatedUser.getLastName() != null ? updatedUser.getLastName() : user.getLastName());
            user.setActive(updatedUser.isActive());
            user.setVerified(updatedUser.isVerified());

            // Optionally handle password update (hash the new password if updated)
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                user.setPassword(updatedUser.getPassword());  // You might hash the password here
            }

            // Save and return the updated user
            return userRepository.save(user);
        }).orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
        userRepository.delete(user);
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<MyUserDetails> findUserDetails(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            MyUserDetails userDetails = MyUserDetails.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .email(user.getEmail())
                    .build();
            return Optional.of(userDetails);
        }
        return Optional.empty();
    }
}
