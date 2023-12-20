/*
 * 
 */
package org.exoplatform.jitsi;

import java.io.IOException;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * The Class TokenAuthenticationFilter used to secure endpoints.
 */
public class TokenAuthenticationFilter implements Filter {

  /** The Constant log. */
  private static final Logger log               = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

  /** The Constant AUTH_TOKEN_HEADER. */
  private final static String AUTH_TOKEN_HEADER = "X-Exoplatform-External-Auth";

  /** The Constant EXTERNAL_AUTH. */
  private static final String EXTERNAL_AUTH     = "external_auth";

  /** The secret. */
  private String              secret;

  /**
   * Instantiates a new token authentication filter.
   *
   * @param secret the secret
   */
  public TokenAuthenticationFilter(String secret) {
    this.secret = secret;
  }

  /**
   * Do filter.
   *
   * @param request the request
   * @param response the response
   * @param chain the chain
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws ServletException the servlet exception
   */
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;
    String authToken = req.getHeader(AUTH_TOKEN_HEADER);
    if (authToken != null && !authToken.trim().isEmpty()) {
      if (verifyToken(authToken)) {
        chain.doFilter(request, response);
      } else {
        res.sendError(HttpStatus.UNAUTHORIZED.value(), "The auth token is not valid");
      }
    } else {
      res.sendError(HttpStatus.UNAUTHORIZED.value(), "The auth token is not provided");
    }
  }

  /**
   * Verify token.
   *
   * @param token the token
   * @return true, if successful
   */
  private boolean verifyToken(String token) {
    try {
      Jws<Claims> jws = Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(secret.getBytes())).parseClaimsJws(token);
      String action = String.valueOf(jws.getBody().get("action"));
      if (EXTERNAL_AUTH.equals(action)) {
        return true;
      }
    } catch (Exception e) {
      log.warn("Cannot verify token {} : {}", token, e.getMessage());
    }
    return false;
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    // Nothing to do
  }

  @Override
  public void destroy() {
    // Nothing to do
  }

}
