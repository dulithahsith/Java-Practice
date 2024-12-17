package validators;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator{
    public static boolean isValid(String email) {
        // Regex pattern for membership ID: Starts with "21", followed by 4 digits, and ends with a single alphabet
        String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailPattern);

        if (email == null) {
            return false;
        }

        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}