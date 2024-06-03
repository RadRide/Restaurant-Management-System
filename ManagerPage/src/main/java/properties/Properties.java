package properties;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Properties {

    public static final String DATE = "dd/yy";

    public static final int DELIVERY = 999;
    public static final String DELIVERY_String = "999";

    public static final String PATTERN = "#,##0.##";
    public static final String DOUBLE_PATTERN = "0.##";

    public static final String NO_TABLE = "N/A";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final int IN_KITCHEN = 0;
    public static final int COMPLETE = 1;
    public static final int CHECKED_OUT = 2;

    public static final int ITEM_DISPLAYED = 1;
    public static final int ITEM_NOT_DISPLAYED = 0;

    /**
     * @Definition Default value for the order's paid value for the database
     */
    public static final double DEFAULT_PAID = 0;

    /**
     * @Definition Staff status default active value
     */
    public static final int STAFF_ACTIVE = 1;
    /**
     * @Definition Staff status default disabled value
     */
    public static final int STAFF_DISABLED = 0;

    /**
     * @Definition The URL of the AI Assistant API
     */
    public static final String AI_URL = "http://100.112.85.79:6512/v1/chat/completions";

    /**
     * @Definition The URL of the restaurant website including the table number GET section
     */
    public static final String RESTAURANT_URL = "https://www.restaurant.com/?tableNumber=";

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
