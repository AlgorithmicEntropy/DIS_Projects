package main;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.regex.Pattern;

public class Util {

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy");

    private static final String PATTERN_DATE = "(0?[1-9]|[12][0-9]|3[01])(\\.)(0?[1-9]|1[0-2])(\\.)((?:19|20)\\d{2})";

    private Util() {
    }

    public static Date parseDate(String date){
        if(date == null || "".equals(date )){
            return null;
        }

        if(!date.matches(PATTERN_DATE)){
            System.out.println("Date \"" + date + "\" does not match expected format (DD.MM.YYYY).");
            // FIXME CB: Was soll hier der default sein? Wie soll damit umgegangen werden?
            return null;
        }

        try {
            return DATE_FORMATTER.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Integer parseInt(String integer){
        if(integer == null || "".equals(integer )){
            return null;
        }

        if(!integer.matches("[0-9]*")){
            System.out.println("Number \"" + integer + "\" is not whole number.");
            // FIXME CB: Was soll hier der default sein? Wie soll damit umgegangen werden?
            return null;
        }

        return Integer.valueOf(integer);
    }

    public static BigDecimal parseMoney(String money){
        if(money == null || "".equals(money )){
            return null;
        }

        if(!money.matches("[0-9]+(,[0-9]*)?")){
            System.out.println("Value \"" + money + "\" is not a decimal number.");
            // FIXME CB: Was soll hier der default sein? Wie soll damit umgegangen werden?
            return null;
        }

        return new BigDecimal(money.replace(".", ","));
    }
}
