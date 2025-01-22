import java.util.Currency;

public class Enum_Ex {
    private enum Currency{
        USD("America"), AUD("Australia"), SLR("Sri lanka");

        private final String country;
        Currency(String country) {
            this.country = country;
        }

        public String getCountry() {
            return country;
        }
    };
    public static void main(String[] args){
        for(Currency i: Currency.values()){
            System.out.println(i+i.getCountry());
        }
    }
}

