package it.unibo.canteen.notification;

import it.unibo.canteen.notification.firebase.FirebaseUserNotificationSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationFactory {

    @Bean
    public UserNotificationSender userNotificationSender() {
        return new FirebaseUserNotificationSender();
    }
}
