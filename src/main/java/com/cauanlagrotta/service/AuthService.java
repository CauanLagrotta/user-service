package com.cauanlagrotta.service;

import com.cauanlagrotta.payload.dto.SignupDTO;
import com.cauanlagrotta.payload.response.AuthResponse;

public interface AuthService {
  AuthResponse login(String username, String password);
  AuthResponse signup(SignupDTO req);
  AuthResponse getAccessTokenFromRefreshToken(String refreshToken);
}
