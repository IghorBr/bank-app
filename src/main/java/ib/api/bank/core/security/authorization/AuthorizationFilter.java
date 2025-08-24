package ib.api.bank.core.security.authorization;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

    private static final String HEADER_AUTHORIZATION_TYPE_BEARER = "Bearer";

    private final AuthorizationService service;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain chain) throws ServletException, IOException {

        var authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (ObjectUtils.isEmpty(authorization) || !authorization.startsWith(HEADER_AUTHORIZATION_TYPE_BEARER)) {
            chain.doFilter(request, response);
            return;
        }

        var jwt = authorization.split(" ")[1].trim();
        var usuario = this.service.authorize(jwt).orElseThrow(() -> new AuthorizationException("Invalid or expired token"));
        var roles = List.of(new SimpleGrantedAuthority("USER"));
        var authentication = new UsernamePasswordAuthenticationToken(usuario, jwt, roles);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }
}
