package com.jiatse.linemessage.mapper;

import com.jiatse.linemessage.dto.NotificationDto;
import com.jiatse.linemessage.entity.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    Notification toEntity(final NotificationDto.EventDto eventDto);
}
