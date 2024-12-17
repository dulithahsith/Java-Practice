enum Laptop{
    Macbook(2000) , XPS(2000),Surface(1500),Vostro(1400);

    private int price;

    private Laptop(){
        this.price=1000;
    }
    private Laptop(int price){
        this.price=price;
    }
    public int getPrice(){
        return this.price;
    }
    public void setPrice(int price){
        this.price = price;
    }
}

public class enums{
    public static void main(String[] args){
        for (Laptop laptop: Laptop.values()){
            System.out.println("Laptop :"+laptop+" Price: "+laptop.getPrice());
        }
    }
}