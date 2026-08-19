package com.cauanlagrotta.controller;

import com.cauanlagrotta.payload.dto.LoginDTO;
import com.cauanlagrotta.payload.dto.SignupDTO;
import com.cauanlagrotta.payload.response.AuthResponse;
import com.cauanlagrotta.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/signup")
  public ResponseEntity<AuthResponse> signup(@RequestBody SignupDTO req){

    AuthResponse res = authService.signup(req);
    return ResponseEntity.ok(res);
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody LoginDTO req){

    AuthResponse res = authService.login(req.getUsername(), req.getPassword());
    return ResponseEntity.ok(res);
  }

  @PostMapping("/access-token/refresh-token/{refreshToken}")
  public ResponseEntity<AuthResponse> getAccessToken(@PathVariable String refreshToken){

    AuthResponse res = authService.getAccessTokenFromRefreshToken(refreshToken);
    return ResponseEntity.ok(res);
  }
}
