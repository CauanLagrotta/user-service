package com.cauanlagrotta.service.impl;

import com.cauanlagrotta.exception.UserException;
import com.cauanlagrotta.model.User;
import com.cauanlagrotta.repository.UserRepository;
import com.cauanlagrotta.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  public User createUser(User user) {
    return userRepository.save(user);
  }

  @Override
  public User getUserById(Long id) {
    Optional<User> user = userRepository.findById(id);
    return user.orElseThrow(() -> new UserException("User not found"));
  }

  @Override
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  @Override
  public User updateUser(Long id, User user) {
    Optional<User> opt = userRepository.findById(id);
    if (opt.isEmpty()) {
      throw new UserException("User not found");
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

  @Override
  public void deleteUser(Long id) {
    Optional<User> opt = userRepository.findById(id);
    if (opt.isEmpty()) {
      throw new UserException("User not found");
    }
    userRepository.deleteById(id);
  }
}
