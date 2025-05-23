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
import com.drmtaxi.drm_taxi.Utils.Messager;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwt;
    private final AppUserService appUserService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        double version = -1;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.replace("Bearer ", "");
            try {
                Map<String, Object> payload = jwt.verify(token);
                System.out.println("payload = " + payload);
                username = (String) payload.get("sub");
                version = (double) payload.get("version");
                System.out.println("payload.get(\"version\") = " + payload.get("version"));
            } catch (Exception ignored) {
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = appUserService.loadUserByUsername(username);
            double storedVersion = userDetails instanceof AppUser ? ((AppUser) userDetails).getTokenVersion()
                    : 0;
            System.out.println("storedVersion = " + storedVersion);
            System.out.println("version = " + version);
            if (storedVersion != version)
                throw new JwtException(Messager.invalidToken());
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                    userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        filterChain.doFilter(request, response);

    }
}
