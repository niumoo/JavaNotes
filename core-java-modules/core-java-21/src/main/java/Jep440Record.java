/**
 * @author https://www.wdbyte.com
 * @date 2023/10/13
 */
public class Jep440Record {
    public static void main(String[] args) {

        Object obj = "Hello www.wdbyte.com";
        if (obj instanceof String s) {
            System.out.println(s);
        }

        Dog dog = new Dog("Husky", 1);
        if (dog instanceof Dog(String name, int age)) {
            String res = StringTemplate.STR."name：\{name} age：\{age}";
            System.out.println(res);
        }
        Object myDog = new MyDog(dog, Color.BLACK);
        if (myDog instanceof MyDog(Dog(String name,int age),Color color)){
            String res = StringTemplate.STR."name：\{name} age：\{age} color:\{color}";
            System.out.println(res);
        }
    }
}

record Dog(String name, int age) {}
enum Color{WHITE,GREY,BLACK};
record MyDog(Dog dog,Color color){};

