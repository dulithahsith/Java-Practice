public class Enum_Ex3 {
    public enum Shape{
        CIRCLE{
            public int area(){
                return 1;
            }
        },
        SQUARE{
            public int area(){
                return 2;
            }
        };
        public abstract int area();
    }
    public static void main(String[] args){
        System.out.println(Shape.CIRCLE.area());
    }

}
