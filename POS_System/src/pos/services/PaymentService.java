package pos.services;

import java.util.Scanner;
public class PaymentService {
    public static void processPayment(double amount){
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n Enter Payment Amount: ");
        double payment = scanner.nextDouble();

        if (payment >= amount) {
            System.out.println("\nPayment successful");
            System.out.println("\nChange: "+(payment - amount));
        }
        else{
            System.out.println("\nPayment insufficient");
        }
    }
}