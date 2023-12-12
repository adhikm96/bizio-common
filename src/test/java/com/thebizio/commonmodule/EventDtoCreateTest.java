package com.thebizio.commonmodule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thebizio.commonmodule.dto.mq.EventDto;
import com.thebizio.commonmodule.service.EventDtoService;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
            "groupName",
            "componentName",
            "hostName",
            "eventType",
            "logType",
            "actor",
            "username",
            "activityGroup",
            "activity",
            "content",
            "payload",
                false,
                false
        );

        assertNotNull(eventDto);

        eventDto = eventDtoService.createEventDto(
                "groupName",
                "componentName",
                "hostName",
                "eventType",
                "logType",
                "actor",
                "username",
                "activityGroup",
                "activity",
                "content",
                new HashMap<String, String>() {{
                    put("some", "data");
                }},
                true,
                false
        );

        assertTrue(eventDto.isLog());


        assertNotNull(eventDto);
        assertNotNull(eventDto.getPayload());

        eventDto = eventDtoService.createEventDto(
                "groupName",
                "componentName",
                "hostName",
                "eventType",
                "logType",
                "actor",
                "username",
                "activityGroup",
                "activity",
                "content",
                new Temp("data1", "data2", "data3"),
                false,
                false
        );

        assertNotNull(eventDto);
        assertNotNull(eventDto.getPayload());
        assertFalse(eventDto.isLog());



        System.out.println(eventDto.getPayload());

        System.out.println(objectMapper.writeValueAsString(eventDto));
    }
}
