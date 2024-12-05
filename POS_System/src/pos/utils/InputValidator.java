package pos.utils;

import java.util.Scanner;
public class InputValidator {
    public static double validatePrice(Scanner scanner){
        double price;
        while(true){
            try{
                price = Double.parseDouble(scanner.nextLine());
                if (price<0){
                    System.out.println("Price can not be negative. Enter a valid amount");
                    continue;
                }
                break;
            } catch(NumberFormatException e){
                System.out.println("Invalid price. Please enter a valid number.");
            }
        }
        return price;
    }
    public static int validateQuantity(Scanner scanner){
        int quantity;
        while(true){
            try{
                quantity = Double.parseInt(scanner.nextLine());
                if (quantity<1){
                    System.out.println("Quantity can not be less than 1. Enter a valid amount");
                    continue;
                }
                break;
            } catch(NumberFormatException e){
                System.out.println("Invalid quantity. Please enter a valid amount.");
            }
        }
        return price;
    }
}