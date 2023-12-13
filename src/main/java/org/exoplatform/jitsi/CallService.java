package org.exoplatform.jitsi;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.map.PassiveExpiringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

/**
 * The Class CallService is used to handle CallInfo and upload links for recordings.
 */
@Service
public class CallService {

  /** The log. */
  private final static Logger                  log = LoggerFactory.getLogger(CallService.class);

  /** The recordings url. */
  @Value("${exo.recordings.url}")
  private String                               recordingsUrl;

  /** The exo secret. */
  @Value("${exo.jwt.secret}")
  private String                               exoSecret;

  /** The expires (hours) for calls cache. */
  @Value("${calls.cache.expires}")
  private int                                  expires;

  /** The calls. */
  private PassiveExpiringMap<String, CallInfo> calls;

  /**
   * Inits the.
   *
   * @throws InterruptedException the interrupted exception
   */
  @PostConstruct
  public void init() throws InterruptedException {
    PassiveExpiringMap.ConstantTimeToLiveExpirationPolicy<String, CallInfo> expirePeriod =
                                                                                         new PassiveExpiringMap.ConstantTimeToLiveExpirationPolicy<>(expires,
                                                                                                                                                     TimeUnit.HOURS);
    this.calls = new PassiveExpiringMap<>(expirePeriod, new HashMap<>());
  }

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
   * Generates the links with token, that contains call info ( can be used to determine recording owner )
   *
   * @param callId the call id
   * @return the upload link
   */
  public String getUploadLink(String callId) {

    CallInfo callInfo = getCallInfo(callId);
    if (callInfo != null) {
      String token = Jwts.builder()
                         .setSubject("exo-webconf")
                         .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
                         .claim("callId", callId)
                         .claim("owner", callInfo.getOwner())
                         .claim("type", callInfo.getType())
                         .claim("moderator", callInfo.getModerator())
                         .claim("participants", callInfo.getParticipants())
                         .signWith(Keys.hmacShaKeyFor(exoSecret.getBytes()))
                         .compact();
      // TODO: add support for chat-rooms
      return new StringBuilder(recordingsUrl).append("?token=").append(token).toString();
    }
    return null;
  }

  /**
   * Delete expired items from the cache
   */
  @Scheduled(cron = "${calls.cache.cleanup}")
  private void clearCache() {
    // This triggers cleanup of the cache
    calls.size();
    log.info("Calls cache was cleared from expired items");
  }
}
