package com.example.autoluxe.dto.searchcriteria;

import com.example.autoluxe.dto.DateRange;
import com.example.autoluxe.utils.DateUtils;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@Data
@ToString
public class PaymentSearchCriteria {

    private String[] range;
    private Long userId;

    private int page = 0;
    private int count = 100;

    private Sort.Direction direction = Sort.Direction.DESC;
    private String sortProperty = "timestamp";

    private ZonedDateTime from;
    private ZonedDateTime to;

    public Pageable getPageable() {
        Sort sort = Sort.by(new Sort.Order(getDirection(), getSortProperty()));
        return PageRequest.of(getPage(), getCount(), sort);
    }

    public void validate() {
        DateRange dateRange = DateUtils.parseRange(range);
        if (dateRange != null) {
            from = dateRange.getFrom();
            to = dateRange.getTo();

            ZonedDateTime now = DateUtils.todayStart();
            int months = 3;
            if (from == null && to == null) {
                to = now.plusDays(1).minusSeconds(1);
                from = to.minusMonths(months).plusSeconds(1);
            }

            if (from == null)
                from = to.minusMonths(months).truncatedTo(ChronoUnit.DAYS);

            if (to == null) {
                to = from.plusMonths(months).truncatedTo(ChronoUnit.DAYS);
                if (to.isAfter(now))
                    to = now.plusDays(1).minusSeconds(1);
            }

        }
    }

}
