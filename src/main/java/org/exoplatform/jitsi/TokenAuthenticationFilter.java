package org.exoplatform.jitsi;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * The Class TokenAuthenticationFilter.
 */
public class TokenAuthenticationFilter implements Filter {

  private Logger              log               = LoggerFactory.getLogger(this.getClass());

  /** The secret. */
  private String              secret;

  /** The Constant AUTH_TOKEN_HEADER. */
  private final static String AUTH_TOKEN_HEADER = "X-Exoplatform-External-Auth";

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
        res.setStatus(HttpStatus.UNAUTHORIZED.value());
        res.getWriter().write("{\"error\":\"The auth token is not valid\"}");
        res.flushBuffer();
      }
    } else {
      res.setStatus(HttpStatus.UNAUTHORIZED.value());
      res.getWriter().write("{\"error\":\"The auth token is not provided\"}");
      res.flushBuffer();
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
      if ("external_auth".equals(action)) {
        return true;
      }
    } catch (Exception e) {
      log.warn("Cannot verify token {}", e.getMessage());
    }
    return false;
  }

}
