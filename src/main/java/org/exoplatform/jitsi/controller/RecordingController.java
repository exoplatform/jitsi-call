package org.exoplatform.jitsi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.exoplatform.jitsi.CallService;

// Not secured by token
@RestController
public class RecordingController {

  /** The log. */
  private final static Logger log = LoggerFactory.getLogger(RecordingController.class);

  @Autowired
  private CallService         callService;

  @RequestMapping(value = "/recordings", method = RequestMethod.POST, consumes = { "multipart/form-data" })
  public ResponseEntity<String> upload(@RequestParam MultipartFile file) {
    if (log.isDebugEnabled()) {
      log.debug("Handled upload recording request with fileName {}", file.getOriginalFilename());
    }
    callService.uploadRecording(file);
    return ResponseEntity.ok().build();
  }
}
