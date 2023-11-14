package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@EnableWebSecurity
@Configuration
class WebAuthorization extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

       http.authorizeRequests() // Autoriza peticiones
               .antMatchers(HttpMethod.POST, "/api/clients").permitAll()
               .antMatchers(HttpMethod.GET, "/api/clients/current").permitAll()
               .antMatchers(HttpMethod.GET, "/web/index.html", "/web/pages/login.html", "/web/pages/register.html",
                                                             "/web/css/**", "/web/js/**", "/web/assets/**").permitAll()
               .antMatchers("/h2-console/**", "/rest/**", "/api/clients", "/api/clients/{id}", "/api/loans/create",
                                              "/api/accounts", "/api/accounts/**").hasAuthority("ADMIN")
               .antMatchers("/web/pages/manager.html", "/web/pages/admin-loan.html").hasAuthority("ADMIN")
               .antMatchers(HttpMethod.GET, "/web/pages/**", "/api/clients/current/**", "/api/loans").authenticated()
               .antMatchers(HttpMethod.POST,"/api/clients/current/**", "/api/loans", "/api/loans/pay").authenticated()
               .anyRequest().denyAll();



        // turn off checking for CSRF tokens
        http.csrf().disable();

        //disabling frameOptions so h2-console can be accessed
        http.headers().frameOptions().disable();

        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "error"));

        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");

        http.logout().logoutUrl("/api/logout");
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }

}
