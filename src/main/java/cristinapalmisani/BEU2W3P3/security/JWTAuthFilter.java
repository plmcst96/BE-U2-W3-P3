package cristinapalmisani.BEU2W3P3.security;

import cristinapalmisani.BEU2W3P3.entities.User;
import cristinapalmisani.BEU2W3P3.exception.UnauthorizedException;
import cristinapalmisani.BEU2W3P3.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private UserService userService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new UnauthorizedException("Please insert a valid bearer token");
            } else {
                String token = authHeader.substring(7);
                jwtTools.verifyToken(token);
                String id = jwtTools.extractIdFromToken(token);
                User currentUser = userService.findById(UUID.fromString(id));
                Authentication authentication = new UsernamePasswordAuthenticationToken(currentUser, null, currentUser.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            }
        }

       /* @Override
        protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
            return new AntPathMatcher().match("/auth/**", request.getServletPath());
        }*/


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String servletPath = request.getServletPath();
        return servletPath.startsWith("/auth/") || servletPath.startsWith("/swagger-ui/") || servletPath.equals("/v3/api-docs.yaml");
    }
    }

