package com.cauanlagrotta.controller;

import com.cauanlagrotta.model.User;
import com.cauanlagrotta.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/api/users")
  public ResponseEntity<User> createUser(@RequestBody @Valid User user){
    User createdUser = userService.createUser(user);
    return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
  }

  @GetMapping("/api/users/profile")
  public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt){
    User user = userService.getUserFromJwt(jwt);
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @GetMapping("/api/users")
  public ResponseEntity<List<User>> getUsers() {
    List<User> users = userService.getAllUsers();
    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  @GetMapping("/api/users/{id}")
  public ResponseEntity<User> getUserById(@PathVariable("id") Long id){
    User user = userService.getUserById(id);
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @PutMapping("/api/users/{id}")
  public ResponseEntity<User> updateUser(@PathVariable Long id,
                         @RequestBody User user) {

    User updatedUser = userService.updateUser(id, user);
    return new ResponseEntity<>(updatedUser, HttpStatus.OK);
  }

  @DeleteMapping("/api/users/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
    userService.deleteUser(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
