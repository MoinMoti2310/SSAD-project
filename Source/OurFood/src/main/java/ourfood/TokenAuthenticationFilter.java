package ourfood;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import ourfood.domain.RoleType;
import ourfood.domain.User;
import ourfood.domain.UserToken;
import ourfood.domain.enums.UserType;
import ourfood.service.UserService;

// TODO rewrite see {http://jaxenter.com/rest-api-spring-java-8-112289.html}
@Order
public class TokenAuthenticationFilter extends BasicAuthenticationFilter {

    AuthenticationManager authenticationManager;

    UserService userService;
    
    private PasswordEncoder encode;

    @SuppressWarnings("deprecation")
    public TokenAuthenticationFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
            ServletException {

        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;

        String header;
        String token;
        String version, hash, appId;
        UserToken userToken = null;

        header = request.getHeader("Authorization");
        token = request.getHeader("s-param-t");
        version = request.getHeader("s-param-v");
        hash = request.getHeader("s-param-h");
        appId = request.getHeader("s-param-a");

        if (version != null && hash != null && appId != null) {

            // Request is from mobile app, authenticate based on hash
            if (token == null || token.isEmpty()) {
                // New Registration
                if (request.getServletPath().equals("/rest/user/register")) {
                    chain.doFilter(request, response);
                    return;
                } else {
                    // Either not registered Or Temporary registration
                    String primaryEmail = null;
                    String registeredMobile = null;

                    // Do not append '@temp.scanon.in' for mobile numbers.
                    if (appId.startsWith("+") && !appId.contains("@")) {
                        primaryEmail = appId;
                        registeredMobile = appId;
                    } else {
                        // primaryEmail = appId + "@temp.scanon.in";
                        primaryEmail = appId;
                    }

                    User user = userService.getUserByPrimaryEmail(primaryEmail);
                    
                    if (user == null) {
                        String userDefaultName = "Anonymous";
                        // User not found. Register user.
                        user = new User();
                        user.setPrimaryEmail(primaryEmail);
                        user.setRegisteredMobile(registeredMobile);
                        user.setIsVerified(true); // No verification required
                        user.setFirstName(userDefaultName);
                        user.setLastName(userDefaultName);
                        if (encode == null) {
                            user.setPassword(primaryEmail);
                        } else {
                            user.setPassword(encode.encode(primaryEmail));
                        }
                        user.setRole(userService.getRole(RoleType.ROLE_END_USER));
                        user.setUserType(UserType.MOBILE);
                        user.setCreatedDate(new Date());
                        userService.save(user);
                    }

                    try {

                        // FIXME: Token has to be set to user token
                        // Temp fix provided in rest.HistoryController.add
                        // userToken = this.userService.createToken(user);
                        SecurityContextHolder.getContext().setAuthentication(
                                new PreAuthenticatedAuthenticationToken(user, null, null));
                    } catch (AuthenticationException failed) {
                        SecurityContextHolder.clearContext();
                        return;
                    }
                }
            }
        } else if (header == null || !header.startsWith("Bearer ")) {

            chain.doFilter(request, response);
            return;
        }
        else {

            try {
                token = header.substring(7);
                userToken = userService.getUserTokenByToken(token);
                SecurityContextHolder.getContext().setAuthentication(
                        new PreAuthenticatedAuthenticationToken(userToken, null, null));
            } catch (AuthenticationException failed) {
                SecurityContextHolder.clearContext();
                return;
            }
        }

        chain.doFilter(request, response);
    }
}