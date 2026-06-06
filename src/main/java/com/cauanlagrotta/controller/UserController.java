package com.cauanlagrotta.controller;

import com.cauanlagrotta.model.User;
import com.cauanlagrotta.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

  private final UserRepository userRepository;

  public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @PostMapping("/api/users")
  public User createUser(@RequestBody @Valid User user){
    return userRepository.save(user);
  }

  @GetMapping("/api/users")
  public List<User> getUsers() {
    return userRepository.findAll();
  }

  @GetMapping("/api/users/{id}")
  public User getUserById(@PathVariable("id") Long id){
    Optional<User> user = userRepository.findById(id);
    return user.orElseThrow(() -> new RuntimeException("User not found"));
  }

  @PutMapping("/api/users/{id}")
  public User updateUser(@PathVariable Long id,
                         @RequestBody User user) {

    Optional<User> opt = userRepository.findById(id);
    if (opt.isEmpty()) {
      throw new RuntimeException("User not found");
    }

    User existingUser = opt.get();

    existingUser.setFullName(user.getFullName());
    existingUser.setEmail(user.getEmail());
    existingUser.setPhone(user.getPhone());
    existingUser.setRole(user.getRole());
    existingUser.setPassword(user.getPassword());
    existingUser.setUpdatedAt(LocalDateTime.now());

    return userRepository.save(existingUser);

  }

  @DeleteMapping("/api/users/{id}")
  public void deleteUser(@PathVariable("id") Long id) {
    Optional<User> opt = userRepository.findById(id);
    if (opt.isEmpty()) {
      throw new RuntimeException("User not found");
    }
    userRepository.deleteById(id);
  }
}
