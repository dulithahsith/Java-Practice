public class Enum_Ex2 {
    public enum Color{
        RED("Red"),GREEN("Green"),BLACK("Black");
        private String colour;
        private Color(String colour){
            this.colour = colour;
        }
        public boolean isRed(){
            return (this == RED);
        }
    }
}
