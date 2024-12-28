package com.example.sms;

import com.example.sms.presentation.api.system.auth.AuthTokenFilter;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.formLogin(login -> login
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
        ).logout(logout -> logout
                .logoutSuccessUrl("/login")
        ).headers(headers -> headers
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
        ).csrf(csrf -> csrf.ignoringRequestMatchers(PathRequest.toH2Console())
        ).csrf(csrf -> csrf.ignoringRequestMatchers("/api/**")
        ).cors(cors -> cors
                .configurationSource(request -> {
                    org.springframework.web.cors.CorsConfiguration corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
                    corsConfiguration.setAllowedOriginPatterns(java.util.List.of("*"));
                    corsConfiguration.addAllowedOrigin("http://localhost:8080");
                    corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);
                    corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);
                    corsConfiguration.setAllowCredentials(true);
                    return corsConfiguration;
                })
        ).authorizeHttpRequests(authz -> authz
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/test/**").permitAll()
                        .anyRequest().authenticated()
                //).sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //).exceptionHandling(ex -> ex.authenticationEntryPoint(new AuthEntryPointJwt())
        );
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
