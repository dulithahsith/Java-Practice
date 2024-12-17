class MyException extends Exception{
    public MyException(String string){
        super(string);
    }
}
public class ExceptionDemo{
    public static void main(String args[]){
        int i=2;
        int j=0;
        int[] nums = new int[4];
        try{
            i=18/i;
            if (j==0){
                throw new MyException("Testing one...");
            }
            nums[4]=3;
        }
        catch(ArithmeticException e){
            System.out.println("Can't divide by zero.");
        }
        catch(ArrayIndexOutOfBoundsException e){
            System.out.println("Stay within your limit");
        } catch (MyException e) {
            j=1;
            System.out.println("Error handled");
        }
    }
}