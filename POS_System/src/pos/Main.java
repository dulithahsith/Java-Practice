package pos;

import pos.models.ShoppingCart;
import pos.models.Product;
import pos.services.PaymentService;
import pos.utils.InputValidator;

public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner();
        ShoppingCart sc = new ShoppingCart();

        System.out.println("\nWelcome to POS system");
        InputValidator iv = new InputValidator();

        while(true){
            System.out.println("\nEnter the item name: ");
            String prodName = scanner.nextLine();
            System.out.println("\nEnter the quantity: ");
            int prodQ = iv.validateQuantity(scanner);
            System.out.println("\nEnter the price: ");
            double prodPrice = iv.validatePrice(scanner);
            Product p = new Product(prodName,prodQ,prodPrice);
            sc.add(p);
            System.out.println("\nIf done, type \"done\" else type \".\" ");
            String con = scanner.nextLine();
            if con.equals("done"){
                break;
            }
        }
    }
}