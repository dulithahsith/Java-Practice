import java.io.BufferedReader;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ioDemo{
    public static void main(String[] args){
        InputStreamReader in = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(in);

        int num = 0;
        try {
            num = Integer.parseInt(br.readLine());
        } catch (IOException e) {
            System.out.println("Input interrupted.");;
        }
        System.out.println(num);

        Scanner sc = new Scanner(System.in);
        System.out.println(sc.nextInt());

    }
}