package de.munichdeveloper.user.service;

import de.munichdeveloper.user.domain.User;
import de.munichdeveloper.user.enums.Role;
import de.munichdeveloper.user.events.UserCreatedEvent;
import de.munichdeveloper.user.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Value("${event.on.userCreated}")
    private boolean eventOnUserCreated;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isEmpty()) {
            // user not registered yet.. let's simply create him!
            User savedUser = createUser(username);
            processUserCreated(savedUser);
            return savedUser;
        }
        return user.get();
    }

    private void processUserCreated(User savedUser) {
        if (eventOnUserCreated) {
            this.publisher.publishEvent(UserCreatedEvent
                    .builder()
                    .email(savedUser.getEmail())
                    .build());
        }
    }

    @NotNull
    private User createUser(String username) {
        var newUser = User
                .builder()
                .email(username)
                .role(Role.USER)
                .build();
        return userRepository.save(newUser);
    }
}