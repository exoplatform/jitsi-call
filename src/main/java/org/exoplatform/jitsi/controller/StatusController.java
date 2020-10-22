package org.exoplatform.jitsi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The Class StatusController.
 */
@RestController
public class StatusController {

  /** The log. */
  private Logger log = LoggerFactory.getLogger(this.getClass());

  /**
   * Status.
   *
   * @return the response entity
   */
  @GetMapping("/")
  public StatusResponse status() {
    if (log.isDebugEnabled()) {
      log.debug("Handled status request: active");
    }
    return new StatusResponse("active");
  }

  /**
   * The Class StatusResponse.
   */
  class StatusResponse {

    /** The status. */
    private final String status;

    /**
     * Instantiates a new status response.
     *
     * @param status the status
     */
    public StatusResponse(String status) {
      this.status = status;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public String getStatus() {
      return status;
    }

  }
}
