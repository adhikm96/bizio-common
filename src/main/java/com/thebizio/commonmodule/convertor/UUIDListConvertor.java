package com.thebizio.commonmodule.convertor;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class UUIDListConvertor implements AttributeConverter<List<UUID>, String> {
    @Override
    public String convertToDatabaseColumn(List<UUID> uuids) {
        return uuids == null ? null : uuids.stream().map(UUID::toString).collect(Collectors.joining(","));
    }

    @Override
    public List<UUID> convertToEntityAttribute(String s) {
        return s == null ? Collections.emptyList() : Arrays.stream(s.split(",")).map(UUID::fromString).collect(Collectors.toList());
    }
}
