package com.wdbyte.enum2;

/**
 * @author https://www.wdbyte.com
 * @date 2023/05/01
 */
public class WeekdayTest {
    public static void main(String[] args) {
        Weekday day = Weekday.MONDAY;
        if (day == Weekday.MONDAY) {
            System.out.println("Today is Monday.");
        }


        Weekday[] weekdays = Weekday.values();
        for (Weekday weekday : weekdays) {
            System.out.println(weekday);
        }

        for (Weekday weekday : weekdays) {
            System.out.println(weekday.ordinal());
        }
        Weekday monday = Weekday.MONDAY;
        switch (monday){
            case MONDAY :{System.out.println("周一");break;}
            case SUNDAY :{System.out.println("周末");break;}
        }
        System.out.println(Weekday.valueOf("MONDAY"));
        System.out.println(Weekday.valueOf("MONDAY1"));
    }
}
