package validators;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IsbnValidator{
    public static boolean isValid(String ISBN){
        String isbn = "^(?=(?:[^0-9]*[0-9]){10}(?:(?:[^0-9]*[0-9]){3})?$)[\\d-]+$";
        Pattern p = Pattern.compile(isbn);

        if(ISBN==null){
            return false;
        }

        Matcher m = p.matcher(ISBN);

        return m.matches();
    }
    public static void main(String[] args){
        String k = "978-1-45678-123-4";
        System.out.println(isValid(k));
    }

}