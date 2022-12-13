package org.exoplatform.jitsi;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = { TokenService.class })
public class TokenServiceTest {

  @Autowired
  private TokenService tokenService;

  @Test
  public void testCreateJitsiToken() throws JSONException {
    assertNotNull(tokenService);
    assertThrows(IllegalArgumentException.class, () -> tokenService.createJitsiToken(null));
    assertThrows(IllegalArgumentException.class, () -> tokenService.createJitsiToken(""));
    String token = tokenService.createJitsiToken("root");
    String[] split_string = token.split("\\.");
    String base64EncodedHeader = split_string[0];

    Base64 base64Url = new Base64(true);
    String header = new String(base64Url.decode(base64EncodedHeader));

    JSONObject json = new JSONObject(header);

    String type  = json.getString("typ");
    String alg  = json.getString("alg");
    assertEquals("JWT", type);
    assertEquals("HS256", alg);
    String base64EncodedBody = split_string[1];
    String body = new String(base64Url.decode(base64EncodedBody));
    json = new JSONObject(body);
    String aud = json.getString("aud");
    assertEquals("exo-jitsi", aud);
    String iss = json.getString("iss");
    assertEquals("exo-jitsi", iss);
    String nbf = json.getString("nbf");
    assertNotNull(nbf);
    String exp = json.getString("exp");
    assertNotNull(exp);
    JSONObject user = json.getJSONObject("context").getJSONObject("user");
    String username = user.getString("name");
    assertEquals("root", username);

    ReflectionTestUtils.setField(tokenService, "appId", "");
    assertThrows(IllegalArgumentException.class, () -> tokenService.createJitsiToken("root"));
    ReflectionTestUtils.setField(tokenService, "appId", "exo-jitsi");
    ReflectionTestUtils.setField(tokenService, "secret", "");
    assertThrows(IllegalArgumentException.class, () -> tokenService.createJitsiToken("root"));
    ReflectionTestUtils.setField(tokenService, "secret", "testWrongLengthToken");
    assertThrows(IllegalArgumentException.class, () -> tokenService.createJitsiToken("root"));
    ReflectionTestUtils.setField(tokenService, "algorithm", "PS512");
    assertThrows(UnsupportedOperationException.class, () -> tokenService.createJitsiToken("root"));
  }
}
