package com.example.autoluxe.service.exporters;

import com.example.autoluxe.domain.Payments;

import java.util.List;

public interface IExporter {

    byte[] export(List<Payments> payments, boolean withDates) throws Exception;
}
