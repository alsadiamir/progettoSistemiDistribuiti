package it.unibo.canteen.notification.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import it.unibo.canteen.model.User;
import it.unibo.canteen.notification.UserNotificationSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseUserNotificationSender implements UserNotificationSender {
    private final Logger LOGGER = LoggerFactory.getLogger(FirebaseUserNotificationSender.class);

    @Value("${google.firebase.credentials.serviceAccountFilePath}")
    private String serviceAccountFilePath;

    @Override
    public void sendTextMessage(User user, String message) throws IOException {
        if (user.getDevice() == null || user.getDevice().length() == 0) {
            return;
        }

        FirebaseApp firebaseApp;
        try {
            firebaseApp = FirebaseApp.getInstance();
        } catch (Exception e) {
            FileInputStream serviceAccount = new FileInputStream(serviceAccountFilePath);
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            firebaseApp = FirebaseApp.initializeApp(options);
        }

        try {
            final String title = "Check this out!";
            Message msg = Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(message)
                            .build())
                    .setWebpushConfig(WebpushConfig.builder()
                            .setNotification(WebpushNotification.builder()
                                    .setTitle(title)
                                    .setBody(message)
                                    .build())
                            .build())
                    .setToken(user.getDevice())
                    .build();
            String response = FirebaseMessaging.getInstance(firebaseApp).send(msg);
        } catch (Exception e) {
            LOGGER.error("Failed to send firebase push notification", e);
        }
    }
}
