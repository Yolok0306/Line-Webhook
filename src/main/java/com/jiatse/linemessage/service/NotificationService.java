package com.jiatse.linemessage.service;

import com.jiatse.linemessage.dto.NotificationDto;
import com.jiatse.linemessage.entity.Notification;
import com.jiatse.linemessage.mapper.NotificationMapper;
import com.jiatse.linemessage.repository.NotificationRepository;
import com.jiatse.linemessage.util.UUIDUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private Environment environment;

    @Transactional
    public void save(final NotificationDto notificationDto) {
        if (notificationDto.getEvents() == null || notificationDto.getEvents().isEmpty()) {
            return;
        }

        final List<Notification> notificationList = notificationDto.getEvents().stream()
                .filter(eventDto -> eventDto.getType().equals("message"))
                .map(this::constructNotification)
                .collect(Collectors.toList());
        this.notificationRepository.saveAll(notificationList);
    }

    private Notification constructNotification(final NotificationDto.EventDto eventDto) {
        final Notification notification = this.notificationMapper.toEntity(eventDto);
        notification.setId(UUIDUtil.getUUID());
        return notification;
    }

    public HttpResponse<String> reply(final String replyToken, final String message)
            throws URISyntaxException, IOException, InterruptedException {
        final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
        final URI uri = new URI("https://api.line.me/v2/bot/message/reply");
        final String body = constructBody(replyToken, message);
        final HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + environment.getProperty("channel-access-token"))
                .build();
        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    private String constructBody(final String replyToken, final String context) {
        final JSONObject message = new JSONObject();
        message.put("type", "text");
        message.put("text", context);
        final JSONArray messages = new JSONArray();
        messages.put(message);

        final JSONObject body = new JSONObject();
        body.put("replyToken", replyToken);
        body.put("messages", messages);
        body.put("notificationDisabled", true);
        return body.toString();
    }

    @Transactional(readOnly = true)
    public List<String> listMessage(final String userId) {
        final List<Notification> notificationList = this.notificationRepository.findBySourceUserId(userId);
        if (notificationList.isEmpty()) {
            return Collections.emptyList();
        }

        return notificationList.stream()
                .map(notification -> notification.getMessage().getText())
                .collect(Collectors.toList());
    }
}
