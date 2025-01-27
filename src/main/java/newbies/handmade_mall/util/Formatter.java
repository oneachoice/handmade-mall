package newbies.handmade_mall.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Formatter {

    private Formatter(){}

    public static String format(LocalDateTime localDateTime){
       return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localDateTime);
    }

    public static String formatForCheckout(){
        return DateTimeFormatter.ofPattern("yyMMdd").format(LocalDateTime.now());
    }

    public static String formatForViewCheckout(){
        return DateTimeFormatter.ofPattern("yy.MM.dd").format(LocalDateTime.now());
    }

}


