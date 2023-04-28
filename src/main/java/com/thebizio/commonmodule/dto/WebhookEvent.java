package com.thebizio.commonmodule.dto;

import com.thebizio.commonmodule.enums.WebhookEventType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WebhookEvent {
    private WebhookEventType eventType;
    private Object data;
}
