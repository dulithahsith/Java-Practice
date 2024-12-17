package validators;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class memIdValidator{
    public static boolean isValid(String membershipID) {
        // Regex pattern for membership ID: Starts with "21", followed by 4 digits, and ends with a single alphabet
        String idPattern = "^21\\d{4}[A-Za-z]$";
        Pattern pattern = Pattern.compile(idPattern);

        if (membershipID == null) {
            return false;
        }

        Matcher matcher = pattern.matcher(membershipID);
        return matcher.matches();
    }

}