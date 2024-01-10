package flashcardsbackend.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig {
    @Value("${frontend.uri}")
    private String frontend;
    @Autowired
    SecurityFilter securityFilter;

    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(List.of(frontend));
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                    .requestMatchers(HttpMethod.POST, "/api/public/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/public/**").permitAll()
                    .requestMatchers("/oauth2/authorization/**").permitAll()
                    .requestMatchers("/login/**","/v3/api-docs/**","/swagger-ui.html","/swagger-ui/**").permitAll()
                    .anyRequest().authenticated()
                    .and().addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                    .addFilterBefore(corsFilter(), SecurityFilter.class)
                .logout()
                    .logoutUrl("/logout")  // URL de logout
                    .logoutSuccessUrl(frontend)  // URL para redirecionar após o logout bem-sucedido
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                .and()
                .oauth2Login(oauth2->{
                    oauth2.loginPage("/login");
                    oauth2.successHandler(oAuth2LoginSuccessHandler);
                })
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }



}
