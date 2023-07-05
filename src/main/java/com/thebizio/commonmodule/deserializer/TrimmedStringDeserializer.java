package com.thebizio.commonmodule.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;

import java.io.IOException;

public class TrimmedStringDeserializer extends StringDeserializer {

    @Override
    public String deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String value = super.deserialize(parser, context);
        if (value != null) {
            value = value.trim();
        }

        return value;
    }
}