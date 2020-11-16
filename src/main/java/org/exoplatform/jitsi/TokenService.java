package org.exoplatform.jitsi;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * The Class TokenService used to manage Jitsi tokens.
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
   * Creates the token that will be used in Jitsi ( pass to IFrame ).
   *
   * @param username the username
   * @return the string
   */
  public String createJitsiToken(String username) {
    return Jwts.builder()
               .setHeaderParam("typ", "JWT")
               .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
               .setSubject("*")
               .setIssuer(appId)
               .setAudience(appId)
               .claim("context", new Context(new User(username)))
               .claim("room", "*")
               .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
               .compact();
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
