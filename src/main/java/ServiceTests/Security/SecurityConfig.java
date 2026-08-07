package ServiceTests.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                /*
                 * Enables the CorsConfigurationSource bean below.
                 */
                .cors(Customizer.withDefaults())

                .csrf(csrf -> csrf.disable())

                // Needed for H2 console
                .headers(headers ->
                        headers.frameOptions(frame -> frame.disable())
                )

                .authorizeHttpRequests(auth -> auth

                        // Public endpoints
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/public/**",
                                "/h2-console/**",
                                "/translate/**"
                        ).permitAll()

                        // Manager-only endpoints
                        .requestMatchers(
                                "/manager/**",
                                "/workgroups/**",
                                "/taskAssignments/**",
                                "/workflow/**"
                        ).hasRole("MANAGER")

                        // Employees and managers can read employee data
                        .requestMatchers(
                                HttpMethod.GET,
                                "/employee/**"
                        ).hasAnyRole("MANAGER", "EMPLOYEE")

                        // Only managers can create, update, or delete employees
                        .requestMatchers(
                                HttpMethod.POST,
                                "/employee/**"
                        ).hasRole("MANAGER")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/employee/**"
                        ).hasRole("MANAGER")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/employee/**"
                        ).hasRole("MANAGER")

                        // Employees and managers can read tasks
                        .requestMatchers(
                                HttpMethod.GET,
                                "/tasks/**"
                        ).hasAnyRole("MANAGER", "EMPLOYEE")

                        // Only managers can create, update, or delete tasks
                        .requestMatchers(
                                HttpMethod.POST,
                                "/tasks/**"
                        ).hasRole("MANAGER")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/tasks/**"
                        ).hasRole("MANAGER")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/tasks/**"
                        ).hasRole("MANAGER")

                        // Admin-only endpoints
                        .requestMatchers("/webUsers/**")
                        .hasRole("ADMIN")

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )

                .httpBasic(Customizer.withDefaults());

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        /*
         * Your frontend currently runs through Live Server:
         * http://127.0.0.1:5500
         */
        configuration.setAllowedOrigins(List.of(
                "http://127.0.0.1:5500",
                "http://localhost:5500",
                "https://manager-translation-frontend.onrender.com"
        ));

        configuration.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "OPTIONS"
        ));

        configuration.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type"
        ));

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}