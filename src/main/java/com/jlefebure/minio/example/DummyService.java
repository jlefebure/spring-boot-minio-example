
package com.jlefebure.minio.example;

import com.jlefebure.spring.boot.minio.MinioException;
import com.jlefebure.spring.boot.minio.MinioService;
import com.jlefebure.spring.boot.minio.notification.MinioNotification;
import io.minio.ObjectStat;
import io.minio.notification.NotificationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class DummyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DummyService.class);

    @Autowired
    private MinioService minioService;

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
        Arrays
            .stream(notificationInfo.records)
            .map(notificationEvent -> {
                try {
                    return minioService.getMetadata(Path.of(notificationEvent.s3.object.key));
                } catch (MinioException e) {
                    e.printStackTrace();
                    return null;
                }

            })
            .forEach(objectStat -> LOGGER.info("metadata for " + objectStat.name() + objectStat.httpHeaders()));
    }

}
