package de.munichdeveloper.user.events;

import lombok.Builder;
import lombok.Getter;

@Builder
public class UserCreatedEvent {
    @Getter
    private String email;
}
