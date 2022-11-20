package com.jiatse.linemessage.controller;

import com.jiatse.linemessage.dto.NotificationDto;
import com.jiatse.linemessage.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.List;

@Controller
@RequestMapping(path = "/api/notification/")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @PostMapping(path = "save", produces = "application/json")
    public ResponseEntity<String> saveNotification(@NonNull @RequestBody final NotificationDto notificationDto) {
        this.notificationService.save(notificationDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "reply", produces = "application/json")
    public ResponseEntity<String> replyNotification(@NonNull @PathParam("replyToken") final String replyToken,
                                                    @NonNull @PathParam("message") final String message)
            throws URISyntaxException, IOException, InterruptedException {
        final HttpResponse<String> httpResponse = this.notificationService.reply(replyToken, message);
        return ResponseEntity.status(httpResponse.statusCode()).body(httpResponse.body());
    }

    @GetMapping(path = "listMessage")
    public @ResponseBody List<String> listMessageByUser(@NonNull @PathParam("userId") final String userId) {
        return this.notificationService.listMessage(userId);
    }
}
