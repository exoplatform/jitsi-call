package org.exoplatform.jitsi;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * The Class CallService.
 */
@Service
public class CallService {

  /** The log. */
  private final static Logger       log   = LoggerFactory.getLogger(CallService.class);

  /** The recordings url. */
  @Value("${exo.recordings.url}")
  private String                    recordingsUrl;

  /** The exo secret. */
  @Value("${exo.jwt.secret}")
  private String                    exoSecret;

  /** The calls. */
  private HashMap<String, CallInfo> calls = new HashMap<>();

  /**
   * Save call info.
   *
   * @param callId the call id
   * @param callInfo the call info
   */
  public void saveCallInfo(String callId, CallInfo callInfo) {
    calls.put(callId, callInfo);
  }

  /**
   * Gets the call info.
   *
   * @param callId the call id
   * @return the call info
   */
  public CallInfo getCallInfo(String callId) {
    return calls.get(callId);
  }


  /**
   * Gets the upload link.
   *
   * @param callId the call id
   * @return the upload link
   */
  public String getUploadLink(String callId) {
    CallInfo callInfo = getCallInfo(callId);
    if (callInfo != null) {
      String token = Jwts.builder()
                         .setHeaderParam("typ", "JWT")
                         .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5)))
                         .claim("action", "external_auth")
                         .claim("callInfo", callInfo)
                         .signWith(Keys.hmacShaKeyFor(exoSecret.getBytes()))
                         .compact();
      return new StringBuilder(recordingsUrl).append("?token=").append(token).toString();
    }
    return null;
  }
}
