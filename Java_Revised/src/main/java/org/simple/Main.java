package org.simple;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

abstract class Vehicle{
    protected String type;
    public Vehicle(String type){
        this.type = type;
    }
    public abstract void drive();
}

class Car extends Vehicle{
    public Car(){
        super("Car");
    }
    @Override
    public void drive(){
        System.out.printf("Vehicle of type "+type+" is driven \n");
    }
}
class CollectionExample{
    public static void test(){
        List<String> fruits = new ArrayList<>();
        fruits.add("Apple");
        fruits.add("pafe");
        fruits.add("fea");
        fruits.add("jef");
        for (String fruit: fruits){
            System.out.println(fruit);
        }
    }
}
class MapExample{
    public static void test(){
        Map<Integer,String> studentMap = new HashMap<>();
        studentMap.put(1,"Alice");
        studentMap.put(2,"Bob");
        studentMap.put(3,"Kavin");
        for (Map.Entry<Integer,String> entry: studentMap.entrySet()){
            System.out.println("Key: "+entry.getKey()+" Name: "+entry.getValue());
        }
    }
}
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World");
        Vehicle myCar = new Car();
        myCar.drive();
        CollectionExample.test();
        MapExample.test();
    }
}
