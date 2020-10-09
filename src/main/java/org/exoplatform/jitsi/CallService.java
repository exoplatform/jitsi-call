package org.exoplatform.jitsi;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * The Class CallService.
 */
@Service
public class CallService {

  /** The log. */
  private final static Logger       log   = LoggerFactory.getLogger(CallService.class);

  @Value("${upload.path}")
  private String                    uploadPath;

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

  public void uploadRecording(MultipartFile file) {
    if (file.isEmpty()) {
      throw new IllegalArgumentException("Failed to store empty file");
    }
    try {
      String fileName = file.getOriginalFilename();
      InputStream is = file.getInputStream();
      Files.copy(is, Paths.get(uploadPath + fileName), StandardCopyOption.REPLACE_EXISTING);
      String callId = fileName.substring(0, fileName.lastIndexOf("_"));
      CallInfo callInfo = getCallInfo(callId);
      // Here we know who is a moderator/owner of the room
      log.info("Recording: " + fileName + " CallInfo: " + callInfo);
    } catch (IOException e) {
      // TODO: custom exception
      throw new RuntimeException("Failed to store file " + file.getName(), e);
    }
  }

  @PostConstruct
  public void init() {
    try {
      Files.createDirectories(Paths.get(uploadPath));
    } catch (IOException e) {
      log.error("Cannot create folder " + uploadPath + " for recordings: ", e.getMessage());
    }
  }
}
