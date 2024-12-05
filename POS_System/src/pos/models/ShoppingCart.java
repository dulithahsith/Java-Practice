package pos.models

public class ShoppingCart{
    private List<Product> products;

    public ShoppingCart(){
        this.products = new ArrayList<>();
    }
    public addProduct(Product product){
        products.add(product);
    }
    public returnProduct(Product prod){
        for (int i=0; i<products.size(); i++){
            Product product = products[i];
            if product.getName().equals(prod.getName()){
                products.remove(i);
                break;
            }
        }
    }
    public double totalBill(){
        total=0;
        for (Product product: products){
           total += product.get_total();
        }
    }
    public double applyDisc(float total, float discP){
        return total - (total*discP);
    }
    public double applyTax(float total,float taxP){
        return total + (total*taxP);
    }
    public void printReceipt(){
        for (Product pr: products){
            System.out.println(pr.getName()+" "+pr.getPr);
        }
    }

}