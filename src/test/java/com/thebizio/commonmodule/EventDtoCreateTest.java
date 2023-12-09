package com.thebizio.commonmodule;

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
    EventDtoService eventDtoService = new EventDtoService(new ObjectMapper());

    public EventDtoCreateTest( String testName )
    {
        super( testName );
    }

    public static Test suite()
    {
        return new TestSuite( EventDtoCreateTest.class );
    }


    public void testApp()
    {
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
            "payload"
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
                }}
        );

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
                new Temp("data1", "data2", "data3")
        );

        assertNotNull(eventDto);
        assertNotNull(eventDto.getPayload());

        System.out.println(eventDto.getPayload());
    }
}
