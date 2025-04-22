package com.example.autoluxe.service;

import com.example.autoluxe.domain.Payments;
import com.example.autoluxe.service.exporters.ExporterFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    public byte[] composeCSV(String contract, List<Payments> payments, boolean withDates) throws Exception {
        if (payments == null || payments.isEmpty())
            return new byte[0];

        return ExporterFactory.getCsvExporter().export(payments, withDates);

    }

    public byte[] composeXLS(String contract, List<Payments> payments, boolean withDates) throws Exception {
        if (payments == null || payments.isEmpty())
            return new byte[0];

        return ExporterFactory.getXlsExporter().export(payments, withDates);
    }
}
