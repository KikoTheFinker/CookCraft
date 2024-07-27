package it.project.cookcraft.services;

import it.project.cookcraft.dao.interfaces.UserDAO;
import it.project.cookcraft.models.CustomUserDetails;
import it.project.cookcraft.models.User;
import it.project.cookcraft.services.interfaces.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDAO userDAO;

    public CustomUserDetailsService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userDAO.findUserByEmail(email);
        if(user.isEmpty())
        {
            throw new UsernameNotFoundException("User Not Found.");
        }
        return new CustomUserDetails(user.get());
    }
}
