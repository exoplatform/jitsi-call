/*
 * 
 */
package org.exoplatform.jitsi.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.exoplatform.jitsi.CallInfo;
import org.exoplatform.jitsi.CallService;
import org.exoplatform.jitsi.TokenService;

/**
 * The Class APIController.
 */
@RestController
@RequestMapping("api/v1")
public class APIController {

  /** The log. */
  private final static Logger log               = LoggerFactory.getLogger(APIController.class);

  /** The Constant AUTH_TOKEN_HEADER. */
  private final static String AUTH_TOKEN_HEADER = "X-Exoplatform-External-Auth";

  /** The token service. */
  private final TokenService  tokenService;

  /** The call service. */
  private final CallService   callService;

  /**
   * Instantiates a new API controller.
   *
   * @param tokenService the token service
   * @param callService the call service
   */
  public APIController(TokenService tokenService, CallService callService) {
    this.tokenService = tokenService;
    this.callService = callService;
  }

  /**
   * Userinfo.
   *
   * @param request the request
   * @param inviteId the invite id
   * @return the user info response
   */
  @GetMapping("/userinfo/{inviteId}")
  public UserInfoResponse userinfo(HttpServletRequest request, @PathVariable("inviteId") String inviteId) {
    if (log.isDebugEnabled()) {
      log.debug("Handled userinfo request with inviteId {}", inviteId);
    }
    UserInfo userInfo = new UserInfo("guest-" + inviteId, "Guest", inviteId);
    String token = request.getHeader(AUTH_TOKEN_HEADER);
    return new UserInfoResponse(userInfo, token);
  }

  /**
   * Get JWT token for Jitsi Meet.
   *
   * @param username the username
   * @return the response entity
   */
  @GetMapping("/token/{username}")
  public ResponseEntity<String> token(@PathVariable("username") String username) {
    if (log.isDebugEnabled()) {
      log.debug("Handled token request with username {}", username);
    }
    try {
      return ResponseEntity.ok(tokenService.createJitsiToken(username));
    } catch (Exception e) {
      log.error("Cannot generate token: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  /**
   * Save call info.
   *
   * @param request the request
   * @param callId the call id
   * @param callInfo the call info
   * @return the response entity
   */
  @PostMapping("/calls/{callId}")
  public ResponseEntity<String> saveCallInfo(HttpServletRequest request,
                                             @PathVariable("callId") String callId,
                                             @RequestBody CallInfo callInfo) {
    if (log.isDebugEnabled()) {
      log.debug("Handled saveCallInfo request with callId {}", callId);
    }
    callService.saveCallInfo(callId, callInfo);
    return ResponseEntity.ok().build();
  }

  /**
   * Save call info.
   *
   * @param callId the call id
   * @return the response entity
   */
  @GetMapping("/calls/{callId}/uploadLink")
  public ResponseEntity<String> uploadLink(@PathVariable("callId") String callId) {
    if (log.isDebugEnabled()) {
      log.debug("Handled uploadLink request with callId {}", callId);
    }
    String uploadLink = callService.getUploadLink(callId);
    // TODO: use ControllerAdvice for Exception Handling
    if (uploadLink != null) {
      return ResponseEntity.ok(uploadLink);
    }
    return ResponseEntity.badRequest().build();
  }

  /**
   * The Class UserInfoResponse.
   */
  public static class UserInfoResponse {

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
  public static class UserInfo {

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
