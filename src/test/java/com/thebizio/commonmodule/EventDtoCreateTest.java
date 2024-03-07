package com.thebizio.commonmodule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thebizio.commonmodule.dto.mq.EventDto;
import com.thebizio.commonmodule.enums.events.Actor;
import com.thebizio.commonmodule.enums.events.EType;
import com.thebizio.commonmodule.enums.events.EventType;
import com.thebizio.commonmodule.service.EventDtoService;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.HashMap;

@Data @AllArgsConstructor @NoArgsConstructor
class Temp {
    String prop1;
    String prop2;
    String prop3;
}

public class EventDtoCreateTest extends TestCase {
    private ObjectMapper objectMapper = new ObjectMapper();
    EventDtoService eventDtoService = new EventDtoService(objectMapper);

    public EventDtoCreateTest( String testName )
    {
        super( testName );
    }

    public static Test suite()
    {
        return new TestSuite( EventDtoCreateTest.class );
    }


    public void testApp() throws JsonProcessingException {
        EventDto eventDto = eventDtoService.createEventDto(
            "projectName",
            "moduleName",
            "hostName",
            "eventType",
            EType.INFO,
            Actor.USER,
            "username",
            "activityGroup",
            "activity",
            "content",
            "payload",
                false,
                false,
                EventType.NOTIFICATION,
                "org",
                Collections.singletonList("notificationIds")
        );

        assertNotNull(eventDto);

        eventDto = eventDtoService.createEventDto(
                "projectName",
                "moduleName",
                "hostName",
                "eventType",
                EType.ERROR,
                Actor.USER,
                "username",
                "activityGroup",
                "activity",
                "content",
                new HashMap<String, String>() {{
                    put("some", "data");
                }},
                true,
                false,
                EventType.NOTIFICATION,
                "org",
                Collections.singletonList("notificationIds")
        );

        assertTrue(eventDto.isLog());


        assertNotNull(eventDto);
        assertNotNull(eventDto.getPayload());

        eventDto = eventDtoService.createEventDto(
                "projectName",
                "moduleName",
                "hostName",
                "eventType",
                EType.ERROR,
                Actor.USER,
                "username",
                "activityGroup",
                "activity",
                "content",
                new Temp("data1", "data2", "data3"),
                false,
                false,
                EventType.NOTIFICATION,
                "org",
                Collections.singletonList("notificationIds")
        );

        assertNotNull(eventDto);
        assertNotNull(eventDto.getPayload());
        assertFalse(eventDto.isLog());

        System.out.println(eventDto.getPayload());

        System.out.println(objectMapper.writeValueAsString(eventDto));
    }
}
