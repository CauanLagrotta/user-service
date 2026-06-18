package com.cauanlagrotta.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String fullName;

  @NotBlank(message = "username is required")
  private String username;

  @NotBlank(message = "Email is required")
  @Email(message = "Email is invalid")
  private String email;
  private String phone;

  @NotBlank(message = "Role is required")
  private String role;

  @NotBlank(message = "Password is required")
  private String password;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;

  public User(String fullName, String email, String phone, String role, String password, String username, LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.fullName = fullName;
    this.username = username;
    this.email = email;
    this.phone = phone;
    this.role = role;
    this.password = password;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public User() {
  }

}
