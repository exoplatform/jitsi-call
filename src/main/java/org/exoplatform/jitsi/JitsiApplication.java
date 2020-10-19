package org.exoplatform.jitsi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * The Class JitsiApplication.
 */
@SpringBootApplication
@EnableScheduling
public class JitsiApplication {

  /** The secret. */
  @Value("${exo.jwt.secret}")
  private String secret;

  /**
   * The main method.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(JitsiApplication.class, args);
  }

  /**
   * Token auth filter.
   *
   * @return the filter registration bean
   */
  @Bean
  public FilterRegistrationBean<TokenAuthenticationFilter> tokenAuthFilter() {
    FilterRegistrationBean<TokenAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new TokenAuthenticationFilter(secret));
    registrationBean.addUrlPatterns("/meet/*");
    registrationBean.addUrlPatterns("/api/*");
    return registrationBean;
  }

}
