package it.project.cookcraft.services;

import it.project.cookcraft.dao.interfaces.UserDAO;
import it.project.cookcraft.models.User;
import it.project.cookcraft.models.UserType;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserDAO userDAO;

    public CustomOAuth2UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        String email = oauth2User.getAttribute("email");
        String firstName = oauth2User.getAttribute("given_name");
        String lastName = oauth2User.getAttribute("family_name");
        String phoneNumber = oauth2User.getAttribute("phone_number");

        if (email != null) {
            processOAuthPostLogin(email, firstName, lastName, phoneNumber);

            return new DefaultOAuth2User(
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
                    oauth2User.getAttributes(),
                    "email"
            );
        } else {
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
        }
    }

    public void processOAuthPostLogin(String email, String firstName, String lastName, String phoneNumber) {
        User existingUser = userDAO.findUserByEmail(email).orElse(null);
        if (existingUser == null) {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(firstName != null ? firstName : "");
            newUser.setSurname(lastName != null ? lastName : "");
            newUser.setPhoneNumber(phoneNumber != null ? phoneNumber : "");
            newUser.setPassword("");
            newUser.setUserType(UserType.User);
            userDAO.save(newUser);
        }
    }
}
