package de.munichdeveloper.user.magic;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DIDToken {
    @SerializedName("iat")
    long issuedAt;

    @SerializedName("ext")
    long expiredAt;

    @SerializedName("iss")
    String issuer;

    @SerializedName("sub")
    String subject;

    @SerializedName("aud")
    String audience;

    @SerializedName("nbf")
    long notBefore;

    @SerializedName("tid")
    String didTokenId;

    @SerializedName("add")
    String additional;
}
