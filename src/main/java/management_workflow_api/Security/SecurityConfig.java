package management_workflow_api.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                // needed for H2 console
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))

                .authorizeHttpRequests(auth -> auth

                        // Public endpoints
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/public/**",
                                "/h2-console/**"
                        ).permitAll()

                        // Manager-only endpoints
                        .requestMatchers(
                                "/managers/**",
                                "/workgroups/**",
                                "/taskAssignments/**"
                        ).hasRole("MANAGER")

                        // Employee + Manager endpoints
                        .requestMatchers(
                                "/employees/**",
                                "/tasks/**",
                                "/translate/**"
                        ).hasAnyRole("MANAGER", "EMPLOYEE")

                        // Admin-only endpoints
                        .requestMatchers("/webUsers/**")
                        .hasRole("ADMIN")

                        // Everything else requires login
                        .anyRequest().authenticated()
                )

                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}