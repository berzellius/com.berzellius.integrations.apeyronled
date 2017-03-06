import java.util.Calendar;
import java.util.Date;

/**
 * Created by berz on 08.01.2017.
 */
public class TestUtil {
    public static Date earlyDate(){
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.YEAR, 2016);
        calendar1.set(Calendar.MONTH, Calendar.NOVEMBER);
        calendar1.set(Calendar.DAY_OF_MONTH, 1);
        calendar1.set(Calendar.HOUR, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);

        return calendar1.getTime();
    }

    public static Date lateDate(){
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.YEAR, 2016);
        calendar2.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar2.set(Calendar.DAY_OF_MONTH, 31);
        calendar2.set(Calendar.HOUR, 23);
        calendar2.set(Calendar.MINUTE, 59);
        calendar2.set(Calendar.SECOND, 59);
        calendar2.set(Calendar.MILLISECOND, 999);

        return calendar2.getTime();
    }
}
