package de.munichdeveloper.magic.dto;

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
