package org.exoplatform.jitsi.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.exoplatform.jitsi.TokenService;

/**
 * The Class APIController.
 */
@RestController
@RequestMapping("api")
public class APIController {

  private Logger              log               = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private TokenService        tokenService;

  private final static String AUTH_TOKEN_HEADER = "X-Exoplatform-External-Auth";

  /**
   * Userinfo.
   *
   * @param inviteId the invite id
   * @return the map
   */
  // Auth endpoint ( will be open in iframe from eXo for setting the token to local storage )
  @GetMapping("/userinfo/{inviteId}")
  public UserInfoResponse userinfo(HttpServletRequest request, @PathVariable("inviteId") String inviteId) {
    UserInfo userInfo = new UserInfo("guest-" + inviteId, "Guest", inviteId);
    String token = request.getHeader(AUTH_TOKEN_HEADER);
    return new UserInfoResponse(userInfo, token);
  }

  @GetMapping("/token/{username}")
  public ResponseEntity<String> token(@PathVariable("username") String username) {
    try {
      return ResponseEntity.ok(tokenService.createToken(username));
    } catch (Exception e) {
      log.error("Cannot generate token: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  /**
   * The Class UserInfoResponse.
   */
  public class UserInfoResponse {

    /** The user info. */
    private final UserInfo userInfo;

    /** The auth token. */
    private final String   authToken;

    /**
     * Instantiates a new user info response.
     *
     * @param userInfo the user info
     * @param authToken the auth token
     */
    public UserInfoResponse(UserInfo userInfo, String authToken) {
      super();
      this.userInfo = userInfo;
      this.authToken = authToken;
    }

    /**
     * Gets the user info.
     *
     * @return the user info
     */
    public UserInfo getUserInfo() {
      return userInfo;
    }

    /**
     * Gets the auth token.
     *
     * @return the auth token
     */
    public String getAuthToken() {
      return authToken;
    }

  }

  /**
   * The Class UserInfo.
   */
  public class UserInfo {

    /** The id. */
    private final String id;

    /** The firstname. */
    private final String firstName;

    /** The lastname. */
    private final String lastName;

    /**
     * Instantiates a new user info.
     *
     * @param id the id
     * @param firstname the firstname
     * @param lastname the lastname
     */
    public UserInfo(String id, String firstname, String lastname) {
      super();
      this.id = id;
      this.firstName = firstname;
      this.lastName = lastname;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
      return id;
    }

    /**
     * Gets the firstname.
     *
     * @return the firstname
     */
    public String getFirstName() {
      return firstName;
    }

    /**
     * Gets the lastname.
     *
     * @return the lastname
     */
    public String getLastName() {
      return lastName;
    }

  }
}
