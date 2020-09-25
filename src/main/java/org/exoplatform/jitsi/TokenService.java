package org.exoplatform.jitsi;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class TokenService.
 */
@Component
public class TokenService {

  /** The secret. */
  @Value("${jitsi.jwt.secret}")
  private String secret;

  /** The appId. */
  @Value("${jitsi.jwt.app.id}")
  private String appId;

  /**
   * Creates the token.
   *
   * @param username the username
   * @return the string
   * @throws JsonProcessingException 
   * @throws JWTCreationException 
   * @throws IllegalArgumentException 
   */
  public String createToken(String username) throws IllegalArgumentException, JWTCreationException, JsonProcessingException {
    Context context = new Context(new User(username));
    Map<String, Object> headers = new HashMap<>();
    headers.put("typ", "JWT");
    String token = JWT.create()
                      .withHeader(headers)
                      .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
                      .withSubject("*")
                      .withIssuer(appId)
                      .withAudience(appId)
                      .withClaim("context", new ObjectMapper().writeValueAsString(context))
                      .withClaim("room", "*")
                      .sign(Algorithm.HMAC256(secret));
    return token;
  }

  /**
   * The Class Context.
   */
  static class Context {

    /** The user. */
    private User user;

    /**
     * Instantiates a new context.
     *
     * @param user the user
     */
    public Context(User user) {
      this.user = user;
    }

    /**
     * Gets the user.
     *
     * @return the user
     */
    public User getUser() {
      return user;
    }

  }

  /**
   * The Class User.
   */
  static class User {

    /** The name. */
    String name;

    /**
     * Instantiates a new user.
     *
     * @param name the name
     */
    public User(String name) {
      this.name = name;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
      return name;
    }

  }

}
