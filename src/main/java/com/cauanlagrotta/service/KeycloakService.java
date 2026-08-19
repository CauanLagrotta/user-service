package com.cauanlagrotta.service;

import com.cauanlagrotta.payload.dto.*;
import com.cauanlagrotta.payload.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakService {

  private static final String KEYCLOAK_BASE_URL = "http://localhost:8080";
  private static final String KEYCLOAK_ADMIN_API = KEYCLOAK_BASE_URL + "/admin/realms/master/users";
  private static final String TOKEN_URL = KEYCLOAK_BASE_URL + "/realms/master/protocol/openid-connect/token";

  private static final String CLIENT_ID = "saloon-booking-client";
  private static final String CLIENT_SECRET = "jwT6OoUp0kdv5tQt30aJOOUsHj4R01eZsGKrHY9dYXmBtrc6kGZj2abfAGjZv6OBhrGB1daCRuOLHuGpUAyQub";
  private static final String GRANT_TYPE = "password";
  private static final String scope = "openid profile email";
  private static final String username = "c1000";
  private static final String password = "admin";
  private static final String clientId = "03db980f-8728-41d9-8580-4e309148a9f3";

  private final RestTemplate restTemplate;

  public void createUser(SignupDTO signupDTO){

    String ACCESS_TOKEN = getAdminAccessToken(username, password, GRANT_TYPE, null).getAccessToken();

    Credential credential = new Credential();
    credential.setTemporary(false);
    credential.setType("password");
    credential.setValue(signupDTO.getPassword());

    UserRequest userRequest = new UserRequest();
    userRequest.setUsername(signupDTO.getUsername());
    userRequest.setEmail(signupDTO.getEmail());
    userRequest.setEnabled(true);
    userRequest.setFirstName(signupDTO.getFirstName());
    userRequest.setLastName(signupDTO.getLastName());

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(ACCESS_TOKEN);

    HttpEntity<UserRequest> requestEntity = new HttpEntity<>(userRequest, headers);

    ResponseEntity<String> response = restTemplate.exchange(
        KEYCLOAK_ADMIN_API,
        HttpMethod.POST,
        requestEntity,
        String.class
    );

    if(response.getStatusCode() == HttpStatus.CREATED){
      log.info("======================= USER CREATED SUCCESSFULLY ========================");

      KeycloakUserDTO user = fetchFirstUserByUsername(signupDTO.getUsername(), ACCESS_TOKEN);

      KeycloakRole role = getRoleByName(clientId, ACCESS_TOKEN, signupDTO.getRole().toString());

      List<KeycloakRole> roles = new ArrayList<>();
      roles.add(role);

      assignRoleToUser(user.getId(), clientId, roles, ACCESS_TOKEN);

    }else {
      throw new RuntimeException(response.getBody());
    }
  }

  public TokenResponse getAdminAccessToken(String username,
                                           String password,
                                           String grantType,
                                           String refreshToken){

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
    requestBody.add("grant_type", grantType);
    requestBody.add("username", username);
    requestBody.add("password", password);
    requestBody.add("refreshToken", refreshToken);
    requestBody.add("client_id", CLIENT_ID);
    requestBody.add("client_secret", CLIENT_SECRET);
    requestBody.add("scope", scope);

    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

    ResponseEntity<TokenResponse> response = restTemplate.exchange(
        TOKEN_URL,
        HttpMethod.POST,
        requestEntity,
        TokenResponse.class
    );

    if(response.getStatusCode() == HttpStatus.OK && response.getBody() != null){
      return response.getBody();
    }

    throw new RuntimeException("Failed to obtain access token");
  }

  public KeycloakRole getRoleByName(String clientId,
                                    String token,
                                    String role){

    String url = KEYCLOAK_BASE_URL + "/admin/realms/master/clients/" + clientId + "/roles/" + role;

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + token);
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

    ResponseEntity<KeycloakRole> response = restTemplate.exchange(
        url,
        HttpMethod.GET,
        requestEntity,
        KeycloakRole.class
    );

    return response.getBody();

  }

  public KeycloakUserDTO fetchFirstUserByUsername(String username, String token){
    String url = KEYCLOAK_BASE_URL + "/admin/realms/master/clients/users?username=" + username;

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

    ResponseEntity<KeycloakUserDTO[]> response = restTemplate.exchange(
        url,
        HttpMethod.GET,
        requestEntity,
        KeycloakUserDTO[].class
    );

    KeycloakUserDTO[] users = response.getBody();

    if(users != null && users.length > 0){
      return users[0];
    }

    throw new RuntimeException("User not found with username " + username);
  }

  public void assignRoleToUser(String userId,
                               String clientId,
                               List<KeycloakRole> roles,
                               String token){

    String url = KEYCLOAK_BASE_URL + "/admin/realms/master/users/" + userId + "/role-mappings/clients/" + clientId;

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<List<KeycloakRole>> requestEntity = new HttpEntity<>(roles, headers);

    try {
      ResponseEntity<String> response = restTemplate.exchange(
          url,
          HttpMethod.POST,
          requestEntity,
          String.class
      );
    }catch (Exception e){
      throw new RuntimeException("Failed to assign new role");
    }
  }
}
