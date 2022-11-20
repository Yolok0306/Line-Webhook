package com.jiatse.linemessage.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class NotificationDto {

    private String destination;

    private List<EventDto> events = Collections.emptyList();

    @Getter
    @Setter
    public static class EventDto {
        private String replyToken;

        private String type;

        private Long timestamp;

        private SourceDto source;

        private MessageDto message;

        @Getter
        @Setter
        public static class SourceDto {
            private String type;

            private String userId;
        }

        @Getter
        @Setter
        public static class MessageDto {
            private String id;

            private String type;

            private String text;
        }
    }
}
