package com.jiatse.linemessage.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document("notification")
public class Notification {

    @Id
    private String id;

    private String replyToken;

    private String type;

    private Long timestamp;

    private Source source;

    private Message message;

    @Getter
    @Setter
    public static class Source {
        private String type;

        private String userId;
    }

    @Getter
    @Setter
    public static class Message {
        private String id;

        private String type;

        private String text;
    }
}
