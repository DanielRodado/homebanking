package com.mindhub.homebanking.configurations;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(ant ->
                ant.requestMatchers(HttpMethod.POST, "/api/clients", "/api/cards/pay").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/clients/current").permitAll()
                .requestMatchers(HttpMethod.GET, "/index.html", "/web/pages/login.html", "/web/pages/register.html",
                                "/web/css/**", "/web/js/**", "/web/assets/**").permitAll()
                        .requestMatchers("/rest/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/clients", "/api/clients/{id}",
                                "/api/accounts", "/api/accounts/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/h2-console/**", "/web/pages/manager.html",
                                "/web/pages/admin-loan.html").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/loans/create").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/clients/admin").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/web/pages/**", "/api/clients/current/**", "/api/loans").hasAuthority("CLIENT")
                        .requestMatchers(HttpMethod.POST,"/api/clients/current/**", "/api/loans", "/api/loans/pay").hasAuthority("CLIENT")
                        .requestMatchers(HttpMethod.PATCH, "/api/clients/current/accounts/delete",
                                "/api/clients/current/cards/delete").hasAuthority("CLIENT")
                        .anyRequest().denyAll())

                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(hFrameOpt -> hFrameOpt.disable()));

        http.formLogin(formLogin ->
                formLogin
                        .loginPage("/web/login.html")
                        .loginProcessingUrl("/api/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler((request, response, authentication) -> clearAuthenticationAttributes(request))
                        .failureHandler((request, response, exception) -> response.sendError(403)))
                        .logout(logout -> logout
                                .logoutUrl("/api/logout")
                                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                                .deleteCookies("JSESSIONID"))

                .rememberMe(Customizer.withDefaults()); /* Mantiene la sesión en el servidor desde que te logueas
                                                  hasta que de deslogueas */

        return http.build();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }

}
