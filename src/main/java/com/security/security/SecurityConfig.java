package com.security.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.security.security.service.MyUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  //define custom userDetailsService
  @Autowired
  private MyUserDetailsService userDetailsService;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // cerate own custom authprovider
  //must define a passwordencoder
  //encode password before saving to database
  public AuthenticationProvider authProvider(PasswordEncoder passwordEncoder) {
    //we need a authenticator that uses database
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    // setuserdetailsservice takes care to connect to user table
    //takes UserDetailsService as input
    //it is an interface defaine a class
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder);
    // DaoAuthenticationProvider implements DaoAuthenticationProvider
    return provider;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http)
    throws Exception {
    http
      //disable csrf
      //becaz use stateless restfulapi
      .csrf(customizer -> customizer.disable())
      .authorizeHttpRequests(request ->
        request
          //permit these path for all user
          .requestMatchers("/all", "/password")
          .permitAll()
          //authenticate for all paths
          .anyRequest()
          .authenticated()
      )
      //default form settings
      //form login
      .formLogin(Customizer.withDefaults())
      //not required for simple form login
      //cerate a  custoum login form
      .formLogin(formLogin ->
        formLogin
          .loginPage("/login")
          .loginProcessingUrl("/login")
          .defaultSuccessUrl("/all", true)
          .permitAll()
        // .failureUrl("login?error=true")
      )
      .httpBasic(Customizer.withDefaults())
      .sessionManagement(session ->
        //use stateless restfullapi
        //new  sessionid for all new request
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      );

    return http.build();
  }
}




