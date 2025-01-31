package newbies.handmade_mall.util;

import newbies.handmade_mall.common.CheckoutProductState;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Formatter {

    private Formatter(){}

    public static String format(LocalDateTime localDateTime){
       return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localDateTime);
    }

    public static String formatForCheckoutCode(LocalDateTime localDateTime){
        return DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(localDateTime);
    }

    public static String formatForViewCheckout(LocalDateTime localDateTime){
        return DateTimeFormatter.ofPattern("yy.MM.dd").format(localDateTime);
    }

    /**
     * 주문 상태 ENUM -> String 으로 변경
     */
    public static String getCheckoutState(CheckoutProductState checkoutProductState){
        String statement = "";
        switch (checkoutProductState){
            case CANCEL -> statement = "주문취소";
            case WAIT -> statement = "주문완료";
            case CONFIRM -> statement = "주문확인";
        }
        return statement;
    }


}


