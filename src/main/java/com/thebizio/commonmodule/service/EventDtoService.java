package com.thebizio.commonmodule.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thebizio.commonmodule.dto.mq.EventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
public class EventDtoService {

    final ObjectMapper objectMapper;

    public EventDtoService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public EventDto createEventDto(String projectName, String moduleName, String hostName, String eventType, String logType, String actor, String username, String activityGroup, String activity, String content, Object payload, boolean log_, boolean forward, String event, String org, List<String> notificationsIds) {

        EventDto eventDto = new EventDto();

        eventDto.setProject(projectName);
        eventDto.setModule(moduleName);
        eventDto.setHostName(hostName);
        eventDto.setEventType(eventType);

        // adding current epoch ms
        eventDto.setTimestamp(Instant.now().toEpochMilli());

        eventDto.setEType(logType);
        eventDto.setActor(actor);
        eventDto.setUsername(username);
        eventDto.setActivityGroup(activityGroup);
        eventDto.setActivity(activity);
        eventDto.setActivityContent(content);

        eventDto.setLog(log_);
        eventDto.setForward(forward);
        eventDto.setEvent(event);
        eventDto.setOrg(org);
        eventDto.setNotificationsIds(notificationsIds);

        if(payload.getClass() == String.class)
            eventDto.setPayload((String) payload);
        else {
            try {
                eventDto.setPayload(objectMapper.writeValueAsString(payload));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        return eventDto;
    }
}
