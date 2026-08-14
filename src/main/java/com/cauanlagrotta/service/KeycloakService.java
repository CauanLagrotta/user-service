package com.cauanlagrotta.service;

import com.cauanlagrotta.payload.dto.*;
import com.cauanlagrotta.payload.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
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
    }
  }

  public TokenResponse getAdminAccessToken(String username,
                                           String password,
                                           String grantType,
                                           String refreshToken){
    return new TokenResponse();
  }

  public KeycloakRole getRoleByName(String clientId,
                                    String token,
                                    String role){

    return null;
  }

  public KeycloakUserDTO fetchFirstUserByUsername(String username, String token){
    return null;
  }

  public void assignRoleToUser(String userId,
                               String clientId,
                               List<KeycloakRole> roles,
                               String token){

  }
}
