package it.project.cookcraft.services;

import it.project.cookcraft.dao.interfaces.UserDAO;
import it.project.cookcraft.models.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User.UserBuilder;
import static org.springframework.security.core.userdetails.User.withUsername;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {


    private final UserDAO userDAO;

    public CustomUserDetailsService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        UserBuilder builder = withUsername(user.getEmail());
        builder.password(user.getPassword());
        builder.roles(user.getUserType().name().toUpperCase());
        return builder.build();
    }
}
