package org.exoplatform.jitsi;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

import javax.crypto.spec.SecretKeySpec;

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

  /** The signature algorithm */
  @Value("${signature.algorithm}")
  private String algorithm;

  /**
   * Creates the token that will be used in Jitsi ( pass to IFrame ).
   *
   * @param username the username
   * @return the string
   */
  public String createJitsiToken(String username) {
    if (username == null || username.isEmpty()) {
      throw new IllegalArgumentException("username is mandatory");
    }
    if (appId == null || appId.isEmpty()) {
      throw new IllegalArgumentException("appId is mandatory");
    }
    if (algorithm == null || algorithm.isEmpty()) {
      throw new IllegalArgumentException("algorithm is mandatory");
    }
    if (secret == null || secret.isEmpty()) {
      throw new IllegalArgumentException("secret is mandatory");
    }
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.forName(algorithm);
    int bitLength = secret.getBytes().length * 8;

    switch (signatureAlgorithm) {
    case HS256:
    case HS384:
    case HS512:
      if (bitLength < signatureAlgorithm.getMinKeyLength()) {
        throw new IllegalArgumentException("The specified key byte array is " + bitLength + " bits which "
            + "is not secure enough for any JWT ");
      }
      break;
    case NONE:
    case ES512:
    case ES384:
    case ES256:
    case PS256:
    case PS384:
    case PS512:
    case RS256:
    case RS384:
    case RS512:
      throw new UnsupportedOperationException("Signature algorithm not supported");

    }
    return Jwts.builder()
               .setHeaderParam("typ", "JWT")
               .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
               .setSubject("*")
               .setIssuer(appId)
               .setAudience(appId)
               .setNotBefore(new Date(System.currentTimeMillis()))
               .claim("context", new Context(new User(username)))
               .claim("room", "*")
               .signWith(new SecretKeySpec(secret.getBytes(), signatureAlgorithm.getJcaName()), signatureAlgorithm)
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
