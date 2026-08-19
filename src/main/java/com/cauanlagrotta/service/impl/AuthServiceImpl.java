package com.cauanlagrotta.service.impl;

import com.cauanlagrotta.model.User;
import com.cauanlagrotta.payload.dto.SignupDTO;
import com.cauanlagrotta.payload.response.AuthResponse;
import com.cauanlagrotta.payload.response.TokenResponse;
import com.cauanlagrotta.repository.UserRepository;
import com.cauanlagrotta.service.AuthService;
import com.cauanlagrotta.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final KeycloakService keycloakService;

  @Override
  public AuthResponse login(String username, String password) {

    TokenResponse tokenResponse = keycloakService.getAdminAccessToken(username, password, "password", null);

    AuthResponse authResponse = new AuthResponse();
    authResponse.setRefresh_token(tokenResponse.getRefreshToken());
    authResponse.setJwt(tokenResponse.getAccessToken());
    authResponse.setMessage("Successfully logged");

    return authResponse;
  }

  @Override
  public AuthResponse signup(SignupDTO req) {
    keycloakService.createUser(req);

    String fullName = req.getFirstName() + " " + req.getLastName();

    User user = new User();
    user.setUsername(req.getUsername());
    user.setPassword(req.getPassword());
    user.setEmail(req.getEmail());
    user.setRole(req.getRole());
    user.setFullName(fullName);
    user.setCreatedAt(LocalDateTime.now());

    userRepository.save(user);

    TokenResponse tokenResponse = keycloakService.getAdminAccessToken(req.getUsername(), req.getPassword(), "password", null);

    AuthResponse authResponse = new AuthResponse();
    authResponse.setRefresh_token(tokenResponse.getRefreshToken());
    authResponse.setJwt(tokenResponse.getAccessToken());
    authResponse.setRole(user.getRole());
    authResponse.setMessage("Successfully registered");

    return authResponse;
  }

  @Override
  public AuthResponse getAccessTokenFromRefreshToken(String refreshToken) {

    TokenResponse tokenResponse = keycloakService.getAdminAccessToken(null, null, "refresh_token", refreshToken);

    AuthResponse authResponse = new AuthResponse();
    authResponse.setRefresh_token(tokenResponse.getRefreshToken());
    authResponse.setJwt(tokenResponse.getAccessToken());
    authResponse.setMessage("Successfully logged");

    return authResponse;
  }
}
