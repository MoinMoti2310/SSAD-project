package ourfood;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import ourfood.filters.MobileRestFilter;
import ourfood.service.UserService;

/**
 * Configuration for spring security. This will enable us to add secured and preAuthentication annotations on
 * controllers
 * 
 * @author raghu.mulukoju
 *
 */
// TODO add a similar configuration for development
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@ComponentScan
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder encode;

    /**
     * Method to configure the {@link HttpSecurity} object
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // TODO enable csrf
        // TODO enable form login
        // TODO add exceptionHandling().accessDeniedHandler()
        http.logout().logoutSuccessUrl("/?logout").deleteCookies("JSESSIONID", "remember-me")
                .logoutUrl("/users/logout");
        http.csrf().disable();
        TokenAuthenticationFilter basicAuthenticationFilter = new TokenAuthenticationFilter(userService);
        http.addFilter(basicAuthenticationFilter);
        http.addFilterBefore(new MobileRestFilter(userService, encode), TokenAuthenticationFilter.class);
        // TODO move remember me from controller to here
        http.rememberMe().rememberMeServices(rememberMeServices()).key("password");
    }

    /**
     * Returns {@link RememberMeServices} bean which is used later on to remember the user
     */
    @Bean
    public RememberMeServices rememberMeServices() {
        TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices("password",
                (UserDetailsService) userService);
        rememberMeServices.setCookieName("remember-me");
        rememberMeServices.setParameter("remember-me");
        return rememberMeServices;
    }

    /**
     * Method to configure the {@link AuthenticationManagerBuilder}. This is later used to manage the authentication of
     * users
     * 
     * @param auth
     *            {@link AuthenticationManagerBuilder} object
     * @throws Exception
     */
    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService((UserDetailsService) userService).passwordEncoder(encoder());
        // This authenticationprovider is required for OAuth integration
        // TODO find a better way to do this
        PreAuthenticatedAuthenticationProvider authenticationProvider = new PreAuthenticatedAuthenticationProvider();
        authenticationProvider
                .setPreAuthenticatedUserDetailsService((AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken>) userService);
        auth.authenticationProvider(authenticationProvider);
    }

    /**
     * @return the encoder bean that is used to encode the passwords. The bean is then autowired to encode during
     *         registration
     */
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}