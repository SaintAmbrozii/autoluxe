package com.example.autoluxe.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class DateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    String pattern = "EEE, dd MMM yyyy HH:mm:ss Z";
    SimpleDateFormat format = new SimpleDateFormat(pattern);


    @Override
    public LocalDateTime deserialize(JsonParser jsonParser,
                                     DeserializationContext deserializationContext) throws IOException, JacksonException {
        String date = jsonParser.getText();
        if (date.isEmpty()) {
            return null;
        }
        return null;
    }
}
