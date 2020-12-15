package it.unibo.canteen.notification;

import it.unibo.canteen.model.User;

import java.io.IOException;

public interface UserNotificationSender {

    void sendTextMessage(User user, String message) throws IOException;
}
