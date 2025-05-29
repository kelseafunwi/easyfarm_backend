package com.easyfarm.easyfarm.service;

import com.easyfarm.easyfarm.enums.UserRole;
import com.easyfarm.easyfarm.model.User;
import com.easyfarm.easyfarm.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class    UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Attempting to load user by username: {}", username);

        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.error("User not found with username: {}", username);
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())))
                .accountExpired(!user.getActive())
                .accountLocked(!user.getActive())
                .credentialsExpired(!user.getActive())
                .disabled(!user.getActive())
                .build();
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        log.debug("Retrieving all users");
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        log.debug("Retrieving user by ID: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", id);
                    return new EntityNotFoundException("User not found with ID: " + id);
                });
    }

    @Transactional
    public User registerUser(User user) {
        log.debug("Registering new user with username: {}", user.getUsername());

        if (existsByUsername(user.getUsername())) {
            log.error("Username already exists: {}", user.getUsername());
            throw new IllegalStateException("Username already exists");
        }

        if (existsByEmail(user.getEmail())) {
            log.error("Email already exists: {}", user.getEmail());
            throw new IllegalStateException("Email already exists");
        }

        // Set default role if not specified
        if (user.getRole() == null) {
            user.setRole(UserRole.USER);
        }

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);

        User savedUser = userRepository.save(user);
        log.info("Successfully registered user with ID: {}", savedUser.getId());
        return savedUser;
    }

    @Transactional
    public User updateUser(Long id, User userDetails) {
        log.debug("Updating user with ID: {}", id);

        User existingUser = getUserById(id);

        // Check if username is being changed and if it's already taken
        if (!existingUser.getUsername().equals(userDetails.getUsername())
            && existsByUsername(userDetails.getUsername())) {
            log.error("Cannot update user. Username already exists: {}", userDetails.getUsername());
            throw new IllegalStateException("Username already exists");
        }

        // Check if email is being changed and if it's already taken
        if (!existingUser.getEmail().equals(userDetails.getEmail())
            && existsByEmail(userDetails.getEmail())) {
            log.error("Cannot update user. Email already exists: {}", userDetails.getEmail());
            throw new IllegalStateException("Email already exists");
        }

        // Update fields
        existingUser.setName(userDetails.getName());
        existingUser.setEmail(userDetails.getEmail());
        existingUser.setUsername(userDetails.getUsername());

        // Only update password if a new one is provided
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        // Only admin can change roles
        if (userDetails.getRole() != null) {
            existingUser.setRole(userDetails.getRole());
        }

        User updatedUser = userRepository.save(existingUser);
        log.info("Successfully updated user with ID: {}", updatedUser.getId());
        return updatedUser;
    }

    @Transactional
    public void deleteUser(Long id) {
        log.debug("Attempting to delete user with ID: {}", id);

        if (!userRepository.existsById(id)) {
            log.error("Cannot delete. User not found with ID: {}", id);
            throw new EntityNotFoundException("User not found with ID: " + id);
        }

        userRepository.deleteById(id);
        log.info("Successfully deleted user with ID: {}", id);
    }

    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username))
                .orElseThrow(() -> {
                    log.error("User not found with username: {}", username);
                    return new UsernameNotFoundException("User not found with username: " + username);
                });
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> {
                    log.error("User not found with email: {}", email);
                    return new EntityNotFoundException("User not found with email: " + email);
                });
    }

    @Transactional
    public void deactivateUser(Long id) {
        log.debug("Attempting to deactivate user with ID: {}", id);

        User user = getUserById(id);
        user.setActive(false);
        userRepository.save(user);
        log.info("Successfully deactivated user with ID: {}", id);
    }

    @Transactional
    public void activateUser(Long id) {
        log.debug("Attempting to activate user with ID: {}", id);

        User user = getUserById(id);
        user.setActive(true);
        userRepository.save(user);
        log.info("Successfully activated user with ID: {}", id);
    }

}
