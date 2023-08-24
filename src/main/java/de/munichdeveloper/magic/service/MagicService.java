package de.munichdeveloper.magic.service;

import com.google.gson.Gson;
import de.munichdeveloper.magic.dto.DIDToken;
import de.munichdeveloper.magic.dto.UserMetadata;
import de.munichdeveloper.magic.lib.DIDTokenHelper;
import de.munichdeveloper.user.dto.JwtAuthenticationResponse;
import de.munichdeveloper.user.exception.FeatureNotEnabledException;
import de.munichdeveloper.user.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class MagicService {

    private KafkaTemplate<Object, Object> kafkaTemplate;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MagicService magicService;

    @Value("${enable.magic.link}")
    private boolean enableMagicLink;

    @Value("${magic.api.key}")
    private String magicApiKey;

    private final static String BASE_URL = "https://api.magic.link";
    private final static String V1_USER_INFO = "/v1/admin/auth/user/get";
    private final static String V2_USER_LOGOUT = "/v2/admin/auth/user/logout";
    private final static String SECRET_KEY_HEADER = "X-Magic-Secret-Key";
    private final static HttpClient HTTP_CLIENT = HttpClient.newHttpClient();


    public UserMetadata getMetadataByIssuer(String issuer) throws java.io.IOException, InterruptedException {
        var request = getRequest(V1_USER_INFO + "?issuer=" + issuer);
        var response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return new Gson().fromJson(response.body(), UserMetadata.class);
    }

    public void logoutByIssuer(String issuer) throws java.io.IOException, InterruptedException {
        var request = getRequest(V2_USER_LOGOUT + "?issuer=" + issuer);
        HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public JwtAuthenticationResponse signinByMagicLink(String didToken) throws IOException, InterruptedException {
        // Check if a signin through magic link is enabled
        if (!enableMagicLink) {
            throw new FeatureNotEnabledException("This feature is currently disabled. You can set 'enable.magic.link' to 'true' to enable it.");
        }
        DIDToken parsedDIDToken = DIDTokenHelper.parseAndValidateToken(didToken);
        String issuer = parsedDIDToken.getIssuer();
        UserMetadata metadataByIssuer = magicService.getMetadataByIssuer(issuer);
        String email = metadataByIssuer.getData().getEmail();

        this.kafkaTemplate.send("user-signin-magic-link", email);

        String jwt = jwtService.generateToken(email);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    private HttpRequest getRequest(String path) {
        return HttpRequest
                .newBuilder(URI.create(BASE_URL + path))
                .header("accept", "application/json")
                .header(SECRET_KEY_HEADER, magicApiKey)
                .build();
    }

}
