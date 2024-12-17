import java.lang.*;

class A{
    int price;
    String name;

    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        A a = (A) object;
        return price == a.price && java.util.Objects.equals(name, a.name);
    }

}

public class Object_class{
    public static void main(String[] args){
        A obj1 = new A();
        A obj2 = new A();

        obj1.price =100;
        obj2.price =100;
        obj1.name="a";
        obj2.name="a";
        System.out.println(obj1.equals(obj2));

        int num =2;
        Integer num1 = 4;
    }
}