package it.unibo.canteen.authentication;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class GoogleJwtVerifier {
    private static final HttpTransport transport = new NetHttpTransport();
    private static final JsonFactory jsonFactory = new JacksonFactory();

    AuthUserData verify(String idTokenString) throws IOException, GeneralSecurityException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList("853386000336-9v0mk8f84acj7plh9ni54pkip84qqsnt.apps.googleusercontent.com"))
                .build();

        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();

            AuthUserData result = new AuthUserData();
            result.setEmail(email);
            return result;
        } else {
            throw new GeneralSecurityException("Invalid idToken");
        }
    }

}
