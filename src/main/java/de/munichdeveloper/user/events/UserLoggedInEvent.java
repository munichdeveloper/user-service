package de.munichdeveloper.user.events;

import lombok.Builder;
import lombok.Getter;

@Builder
public class UserLoggedInEvent {
    @Getter
    private String email;
}
