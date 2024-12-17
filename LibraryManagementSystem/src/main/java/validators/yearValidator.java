package validators;
import java.time.Year;

public class YearValidator{
    public static boolean isValid(int year){
        int currentYear = Year.now().getValue();
        return year <= currentYear;
    }
}