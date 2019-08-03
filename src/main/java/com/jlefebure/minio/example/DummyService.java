
package com.jlefebure.minio.example;

import com.jlefebure.spring.boot.minio.notification.MinioNotification;
import io.minio.notification.NotificationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class DummyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DummyService.class);

    @MinioNotification({"s3:ObjectCreated:Post"})
    public void handleUpload(NotificationInfo notificationInfo) {
        LOGGER.info(Arrays
            .stream(notificationInfo.records)
            .map(notificationEvent -> "Receiving event " + notificationEvent.eventName + " for " + notificationEvent.s3.object.key)
            .collect(Collectors.joining(","))
        );
    }

    @MinioNotification(value = {"s3:ObjectAccessed:Get"}, suffix = ".pdf")
    public void handleGetPdf(NotificationInfo notificationInfo) {
        LOGGER.info(Arrays
            .stream(notificationInfo.records)
            .map(notificationEvent -> "Receiving event " + notificationEvent.eventName + " for " + notificationEvent.s3.object.key)
            .collect(Collectors.joining(","))
        );
    }

}