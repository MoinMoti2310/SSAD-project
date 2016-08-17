package ourfood.filters;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import ourfood.domain.RoleType;
import ourfood.domain.User;
import ourfood.domain.UserToken;
import ourfood.domain.enums.UserType;
import ourfood.service.UserService;
import ourfood.utils.Digest;

public class MobileRestFilter implements Filter {

    private UserService userService;
    private PasswordEncoder encode;

    public MobileRestFilter(UserService userService, PasswordEncoder encode) {
        this.userService = userService;
        this.encode = encode;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
            ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;
        String token, version, hash, appId, orgCode;
        UserToken userToken;

        if (!request.getServletPath().startsWith("/rest")) {
            chain.doFilter(request, response);
            return;
        }

        token = request.getHeader("s-param-t");
        version = request.getHeader("s-param-v");
        hash = request.getHeader("s-param-h");
        appId = request.getHeader("s-param-a");
        orgCode = request.getHeader("s-param-o");

        // Validate default information
        // AppId should be valid (min 5 char)
        // FIXME: This should be clearly wetted through before fixing
        // if (appId.length() < 5) {
        // return;
        // }

        if (version == null || hash == null || appId == null)
            throw new ServletException("Invalid Message");

        if (!Digest.isValid(version, appId, hash)) {
            throw new ServletException("Invalid Message");
        }

        if (token == null || token.isEmpty()) {
            // New Registration
            if (request.getServletPath().equals("/rest/user/register")) {
                chain.doFilter(request, response);
                return;
            } else {
                // Either not registered Or Temporary registration
                String primaryEmail = appId + "@temp.scanon.in";
                User user = userService.getUserByPrimaryEmail(primaryEmail);
                if (user == null) {

                    String userDefaultName = "anonymous";
                    // User not found. Register it.
                    user = new User();
                    user.setPrimaryEmail(primaryEmail);
                    user.setIsVerified(true); // No verification required
                    user.setFirstName(userDefaultName);
                    user.setLastName(userDefaultName);
                    user.setPassword(encode.encode(primaryEmail));
                    user.setRole(userService.getRole(RoleType.ROLE_MOBILE_USER));
                    user.setUserType(UserType.MOBILE);
                    user.setCreatedDate(new Date());
                    userService.save(user);
                }
                
                userToken = this.userService.createToken(user);
            }

        } else {
            // User registered. Validate token
            userToken = userService.getUserTokenByToken(token.substring("scanon ".length()));
        }
        
        SecurityContextHolder.getContext().setAuthentication(
                new PreAuthenticatedAuthenticationToken(userToken, null, null));
        chain.doFilter(request, response);
        return;
    }

    @Override
    public void destroy() {

    }
}
