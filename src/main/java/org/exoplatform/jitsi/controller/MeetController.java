package org.exoplatform.jitsi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The Class MeetController used to handle Jitsi calls.
 */
@Controller
@RequestMapping("meet")
public class MeetController {

  /** The log. */
  private Logger log = LoggerFactory.getLogger(this.getClass());

  /** The app url. */
  @Value("${jitsi.app.url}")
  private String appUrl;

  /**
   * This endpoint returns Jitsi Call Page.
   *
   * @param model the model
   * @param meetId the meet id
   * @return the string
   */
  @GetMapping("/{meetId}")
  public String call(Model model, @PathVariable("meetId") String meetId) {
    if (log.isDebugEnabled()) {
      log.debug("Handled call page request with meetId: {}", meetId);
    }
    model.addAttribute("appUrl", appUrl);
    return "call";
  }

}
