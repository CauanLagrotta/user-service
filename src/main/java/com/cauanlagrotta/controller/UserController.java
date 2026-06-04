package com.cauanlagrotta.controller;

import com.cauanlagrotta.model.User;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  public User getUser() {
    User user = new User();

    return user;
  }
}
