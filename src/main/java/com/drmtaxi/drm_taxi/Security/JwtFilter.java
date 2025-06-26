package com.drmtaxi.drm_taxi.Security;

import java.io.IOException;
import java.util.Map;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.drmtaxi.drm_taxi.Services.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwt;
    private final AppUserService appUserService;

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        return request.getServletPath().toString().startsWith("/api/v1/auth/") ||
                request.getServletPath().toString().startsWith("/api/v1/open/");
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        Integer version = -165464;

        if (authHeader == null) {
            String authCookie = "none";
            Cookie[] cookies = request.getCookies();

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    System.out.println("cookie.getName() = " + cookie.getName());
                    if (cookie.getName().equals("actn")) {
                        authCookie = cookie.getValue();
                        break;
                    }
                }
            }

            token = authCookie.replace("Bearer ", "");
        } else if (authHeader.startsWith("Bearer ")) {
            token = authHeader.replace("Bearer ", "");
        }
        System.out.println("token = " + token);
        if (token != null) {
            try {
                Map<String, Object> payload = jwt.verify(token);
                username = (String) payload.get("sub");
                System.out.println("=====================================================================");
                version = (Integer) payload.get("version");
                System.out.println("version = " + version);
            } catch (Exception ignored) {
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = appUserService.loadUserByUsername(username);
            System.out.println("userDetails = " + userDetails);
            double storedVersion = userDetails instanceof AppUser ? ((AppUser) userDetails).getTokenVersion()
                    : -1;
            System.out.println("version = " + version);
            System.out.println("storedVersion = " + storedVersion);
            if (storedVersion != version)
                throw new JwtException("invalid token");
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                    userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        filterChain.doFilter(request, response);

    }
}
