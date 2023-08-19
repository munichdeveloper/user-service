package de.munichdeveloper.user.magic;

import lombok.Data;

@Data
public class UserMetadata {
    NestedData data;

    @Data
    public class NestedData {
        String issuer;
        String publicAddress;
        String email;
        String oauthProvider;
        String phoneNumber;
    }
}
