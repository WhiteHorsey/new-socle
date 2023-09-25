package ma.bcp.security.config;

import ma.bcp.security.filters.TokenValidationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Value("${authorization.public-endpoints}")
    private String[] publicEndpoints;

    @Bean
    public TokenValidationFilter authenticationJwtTokenFilter() {
        return new TokenValidationFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Public And Private Routes
                .authorizeRequests().antMatchers("/public/**", "/v3/api-docs/**").permitAll().and()
                .authorizeRequests().antMatchers(publicEndpoints).permitAll()
                .anyRequest().authenticated().and()
                // Disable cors
                .csrf().disable().cors().disable()
                // Session Management
                .sessionManagement().sessionCreationPolicy(STATELESS).and()
                // Token Validation Filter
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}