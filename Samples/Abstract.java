abstract class Car{
    public abstract void drive();
    public abstract void fly();

    public void playMusic(){
        System.out.println("Playing....");
    }
}

class WagonR extends Car{
    public void drive(){
        System.out.println("Driving...");
    }
    public void fly(){
        System.out.println("flying...");
    }
}
public class Abstract{
    public static void main(String[] args){
        Car car = new WagonR();
        car.drive();
    }
}
