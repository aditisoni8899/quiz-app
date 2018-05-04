package com.developervishalsehgal.udacityscholarsapp.services;

import com.developervishalsehgal.udacityscholarsapp.data.models.Notification;
import com.developervishalsehgal.udacityscholarsapp.utils.NotificationUtils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.developervishalsehgal.udacityscholarsapp.data.DataHandlerProvider;
import com.developervishalsehgal.udacityscholarsapp.data.models.Notification;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Service for dealing with firebase push notifications
 */
public class FBMessagingService extends FirebaseMessagingService {

    private static final String TAG = FBMessagingService.class.getSimpleName();

    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_FROM = "from";
    private static final String KEY_TYPE = "type";
    private static final String KEY_ACTION = "action";
    private static final String KEY_EXTRA_1 = "extra_1";
    private static final String KEY_EXTRA_2 = "extra_2";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // check if message contains a data payload
        if (remoteMessage != null && remoteMessage.getData().size() > 0) {
            Notification notification = createNotificationObject(remoteMessage);
            storeNotification(notification);
            raiseSystemNotification(notification);
        }

        // Check if message contains a notification payload
        if (remoteMessage != null && remoteMessage.getNotification() != null) {
            // TODO: Decide if any action is needed here
        }
    }

    /**
     * Creates a {@link Notification} object from @{@link RemoteMessage} by extracting parameters
     * from data
     *
     * @param remoteMessage {@link RemoteMessage} received from fo=irebase clud messaging
     * @return A {@link Notification} representing the notification received
     */
    private Notification createNotificationObject(@NonNull RemoteMessage remoteMessage) {
        Map<String, String> receivedData = remoteMessage.getData();

        Notification notification = new Notification();

        notification.setTitle(receivedData.get(KEY_TITLE));
        notification.setAction(receivedData.get(KEY_ACTION));
        notification.setDescription(receivedData.get(KEY_DESCRIPTION));
        notification.setFrom(receivedData.get(KEY_FROM));
        notification.setType(receivedData.get(KEY_TYPE));
        notification.setTimeStamp(System.currentTimeMillis() / 1000);
        notification.setExtra1(receivedData.get(KEY_EXTRA_1));
        notification.setExtra2(receivedData.get(KEY_EXTRA_2));

        return notification;
    }

    /**
     * Store the message data into local database
     *
     * @param notification notification to be stored locally
     */
    private void storeNotification(@NonNull Notification notification) {
        DataHandlerProvider.provide().addNotification(notification);
    }

    /**
     * Raises the system notification when the data-payload is received from server
     *
     * @param notification - {@link Notification} object generated from received data payload
     */
    private void raiseSystemNotification(@NonNull Notification notification) {

        NotificationUtils.createNotification(this, notification, notification.getType());
    }
}