package com.ly.add.sponge.common.time;

import com.alibaba.fastjson.JSON;
import com.ly.add.sponge.common.utils.TimeUtil;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author : qqy48861
 * date : 2020/1/3.
 */
public class Sigan {

    private LocalDateTime sigan;

    public static Sigan custom(Object input) {
        return new Sigan(input);
    }

    public Sigan(Object input) {
        sigan = TimeUtil.obj2ldt(input);
    }


    public Sigan plus(Integer howMany, String datePattern) {
        switch (datePattern) {
            case "y":
                sigan = sigan.plusYears(howMany);
                break;
            case "M":
                sigan = sigan.plusMonths(howMany);
                break;
            case "d":
                sigan = sigan.plusDays(howMany);
                break;
            case "H":
                sigan = sigan.plusHours(howMany);
                break;
            case "m":
                sigan = sigan.plusMinutes(howMany);
                break;
            case "s":
                sigan = sigan.plusSeconds(howMany);
                break;
            default:
        }
        return this;
    }


    public Sigan plus(Integer howMany, ChronoUnit unit) {
        sigan = sigan.plus(howMany, unit);
        return this;
    }

    public Sigan minus(Integer howMany, String datePattern) {
        switch (datePattern) {
            case "y":
                sigan = sigan.minusYears(howMany);
                break;
            case "M":
                sigan = sigan.minusMonths(howMany);
                break;
            case "d":
                sigan = sigan.minusDays(howMany);
                break;
            case "H":
                sigan = sigan.minusHours(howMany);
                break;
            case "m":
                sigan = sigan.minusMinutes(howMany);
                break;
            case "s":
                sigan = sigan.minusSeconds(howMany);
                break;
            default:
        }
        return this;
    }

    public Sigan minus(Integer howMany, ChronoUnit unit) {
        sigan = sigan.minus(howMany, unit);
        return this;
    }


    public Long toTS10() {
        return TimeUtil.ldt2ts(sigan, true);
    }

    public Long toTS13() {
        return TimeUtil.ldt2ts(sigan, false);
    }

    public String toStr(String pattern) {
        return TimeUtil.ts2str(TimeUtil.ldt2ts(sigan, false), pattern);
    }

    public String toStr() {
        return TimeUtil.ts2str(TimeUtil.ldt2ts(sigan, false));
    }

    public static void main(String[] args) {
        System.out.println(JSON.toJSON(null).toString());
    }


}