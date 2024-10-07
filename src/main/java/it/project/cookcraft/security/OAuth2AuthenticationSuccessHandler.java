package it.project.cookcraft.security;

import it.project.cookcraft.models.User;
import it.project.cookcraft.services.CustomOAuth2UserService;
import it.project.cookcraft.services.CustomUserDetailsService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public OAuth2AuthenticationSuccessHandler(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        if (authentication instanceof OAuth2AuthenticationToken oauth2Token) {
            String email = oauth2Token.getPrincipal().getAttribute("email");
            String firstName = oauth2Token.getPrincipal().getAttribute("given_name");
            String lastName = oauth2Token.getPrincipal().getAttribute("family_name");
            String phoneNumber = oauth2Token.getPrincipal().getAttribute("phone_number");

            if (email != null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                String jwt = jwtUtil.generateToken(userDetails.getUsername());

                String redirectUrl = String.format(
                        "http://localhost:3000/loginSuccess?token=%s&firstName=%s&lastName=%s&email=%s&phoneNumber=%s&address=",
                        jwt, firstName, lastName, email, phoneNumber != null ? phoneNumber : ""
                );

                UriComponents uriComponents = ServletUriComponentsBuilder.fromUriString(redirectUrl).build();
                response.sendRedirect(uriComponents.toUriString());
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email not found from OAuth2 provider");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Authentication type not supported");
        }
    }
}
