package com.example.autoluxe.service.exporters;

import com.example.autoluxe.domain.Payments;
import com.example.autoluxe.utils.DateUtils;
import com.example.autoluxe.utils.MoneyUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
public class CsvExporter implements IExporter{

    private static final String NL = "\r\n";
    private static final String SEMI_COLON = ";";

    @Override
    public byte[] export(List<Payments> payments, boolean withDates) throws Exception {
        StringBuilder str = new StringBuilder();

        log.debug(">Сообщаем о странице{} платежек", payments.size());
        for (Payments item : payments) {
            buildCsvLine(str, item, withDates);
            str.append(NL);
        }
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             OutputStreamWriter osw = new OutputStreamWriter(bos, StandardCharsets.UTF_8);
             BufferedWriter out = new BufferedWriter(osw)) {
            out.write('\ufeff'); // BOM
            out.write(str.toString());
            out.flush();
            return bos.toByteArray();
        }
    }

    private void buildCsvLine(StringBuilder str, Payments payment, boolean withDate) {
        str.append(payment.getUserEmail())
                .append(SEMI_COLON)
                .append(payment.getUserId())
                .append(SEMI_COLON)
                .append(payment.getManagerId())
                .append(SEMI_COLON)
                .append(MoneyUtils.formatRUNoGrouping(payment.getSumma().doubleValue()));
        if (withDate)
            str.append(SEMI_COLON)
                    .append(payment.getTimestamp().withZoneSameInstant(DateUtils.DEFAULT_ZONE).format(DateTimeFormatter.ISO_LOCAL_DATE));
    }
}
