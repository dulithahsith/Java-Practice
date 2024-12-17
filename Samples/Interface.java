interface Computer{
    public void code();
}
class Laptopp implements Computer{
    public void code(){
        System.out.println("Code -> Sleep -> Repeat");
    }
}
class Desktop implements Computer{
    @java.lang.Override
    public void code() {
        System.out.println("Code -> Sleep -> Repeat");
    }
}
class Deveop {
    public void devApp(Computer comp){
        comp.code();
    }
}
public class Interface{
    public static void main(String[] args){
        Computer comp = new Desktop();
        Computer lap = new Laptopp();

        Deveop Dulitha = new Deveop();
        Dulitha.devApp(lap);
    }
}
