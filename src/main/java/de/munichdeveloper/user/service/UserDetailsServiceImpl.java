package de.munichdeveloper.user.service;

import de.munichdeveloper.user.domain.User;
import de.munichdeveloper.user.enums.Role;
import de.munichdeveloper.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaTemplate<Object, Object> kafkaTemplate;

    @Value("${event.on.userCreated}")
    private boolean eventOnUserCreated;

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
            User savedUser = userRepository.save(newUser);
            if (eventOnUserCreated) {
                this.kafkaTemplate.send("user-created", savedUser.getEmail());
            }
            return savedUser;
        }
        return user.get();
    }
}