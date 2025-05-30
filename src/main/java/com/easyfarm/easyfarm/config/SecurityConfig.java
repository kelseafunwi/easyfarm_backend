package com.easyfarm.easyfarm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").authenticated() // All routes starting with /admin require authentication
                        .anyRequest().permitAll()                     // All other routes are public
                )
                .formLogin(form -> form
                        .loginPage("/login") // Custom login page
                        .defaultSuccessUrl("/admin/dashboard", true) // Redirect to an admin page after successful login
                        .failureUrl("/login?error=true") // Redirect on failure
                        .permitAll() // The login page itself should be public
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout=true") // Redirect after logout
                        .permitAll() // The logout mechanism should be accessible
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // For demonstration, an in-memory user.
        // In a real application, you'd use a database-backed UserDetailsService.
        UserDetails adminUser = User
                .withUsername("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("ADMIN") // Assigning an ADMIN role
                .build();

        UserDetails regularUser = User // Example of a non-admin user, if needed for testing public routes
                .withUsername("user")
                .password(passwordEncoder().encode("user123"))
                .roles("USER")
                .build();


        return new InMemoryUserDetailsManager(adminUser, regularUser);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}