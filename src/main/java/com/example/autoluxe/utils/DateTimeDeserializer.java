package com.example.autoluxe.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.SneakyThrows;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class DateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    String pattern = "EEE, dd MMM yyyy HH:mm:ss Z";
    SimpleDateFormat format = new SimpleDateFormat(pattern,Locale.ENGLISH);


    @SneakyThrows
    @Override
    public LocalDateTime deserialize(JsonParser jsonParser,
                            DeserializationContext deserializationContext) throws IOException, JacksonException {
        String date = jsonParser.getText();
        if (date.isEmpty()) {
            return null;
        }
        Date javaDate = format.parse(date);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(javaDate.toInstant(), ZoneId.systemDefault());

        return localDateTime;

    }
}
