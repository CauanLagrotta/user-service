package com.cauanlagrotta.payload.response;

import com.cauanlagrotta.domain.UserRole;
import lombok.Data;

@Data
public class AuthResponse {
  private String jwt;
  private String refresh_token;
  private String message;
  private String title;
  private UserRole role;
}
