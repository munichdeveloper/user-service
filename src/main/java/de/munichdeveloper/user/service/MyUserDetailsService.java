package de.munichdeveloper.user.service;

import de.munichdeveloper.user.domain.User;
import de.munichdeveloper.user.enums.Role;
import de.munichdeveloper.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isEmpty()) {
            // user not registered yet.. let's simply create him!
            var newUser = User
                    .builder()
                    .email(username)
                    .role(Role.USER)
                    .build();
            return userRepository.save(newUser);
        }
        return user.get();
    }
}