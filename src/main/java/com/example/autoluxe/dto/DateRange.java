package com.example.autoluxe.dto;

import lombok.Data;
import lombok.ToString;

import java.time.ZonedDateTime;

@Data
@ToString
public class DateRange {

    private ZonedDateTime from;
    private ZonedDateTime to;
}
