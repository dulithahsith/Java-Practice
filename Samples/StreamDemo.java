
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class StreamDemo{
    public static void main(String[] args){
        List<Integer> nums = Arrays.asList(4,5,7,3,2,6);

        Consumer<Integer> con = n -> System.out.println(n);

        Stream<Integer> s1 = nums.stream();
//        nums.forEach(n -> System.out.println(n));
//        s1.forEach(n-> System.out.println(n));

        Stream <Integer> s2 = s1.filter(n -> n%2==0);
//        s2.forEach(n-> System.out.println(n));

        Stream<Integer> s3 = s2.map(n->n+2);
        s3.forEach(n-> System.out.println(n));

        int result = nums.stream()
                .filter(n-> n%2==0)
                .map(n->n+2)
                .reduce(0,(c,e)->c+e);
        System.out.println("Result: "+result);

        Stream<Integer> sortedValues = nums.parallelStream()
                .filter(n->n%2==0)
                .sorted();
        sortedValues.forEach(n -> System.out.println(n));
    }

}