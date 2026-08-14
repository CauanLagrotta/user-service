package com.cauanlagrotta.payload.dto;

import com.cauanlagrotta.domain.UserRole;
import lombok.Data;

@Data
public class SignupDTO {

  private String firstName;
  private String lastName;
  private String email;
  private String password;
  private String username;
  private UserRole role;
}
