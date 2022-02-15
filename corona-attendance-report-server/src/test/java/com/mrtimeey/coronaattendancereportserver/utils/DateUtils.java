package com.mrtimeey.coronaattendancereportserver.utils;

import org.assertj.core.api.Assertions;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class DateUtils {

    public static void isBetweenNowAndOneMinuteAgo(LocalDateTime localDateTime) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneMinuteAgo = now.minus(1, ChronoUnit.MINUTES);
        assertThat(localDateTime).isBetween(oneMinuteAgo, now);
    }

}
