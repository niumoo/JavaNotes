/**
 * @author niulang
 * @date 2023/05/04
 */
public class RecordTest {
    public static void main(String[] args) {
        Dog dog = new Dog("name", 1);
        System.out.println(dog.name());
        System.out.println(dog.age());
    }
}

record Dog(String name, Integer age) {
}