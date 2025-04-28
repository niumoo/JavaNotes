package com.wdbyte;

import java.util.Optional;

import org.junit.jupiter.api.Test;

/**
 * <p>
 * JDK8 为解决空指针增加的 Optional 方法
 * 
*  @Author https://www.wdbyte.com
 * @Date 2019/2/19 11:40
 */
public class Jdk8Optional {

    /**
     * 创建一个 Optional
     */
    @Test
    public void createOptionalTest() {
        // Optional 构造方式1 - of 传入的值不能为 null
        Optional<String> helloOption = Optional.of("hello");

        // Optional 构造方式2 - empty 一个空 optional
        Optional<String> emptyOptional = Optional.empty();

        // Optional 构造方式3 - ofNullable 支持传入 null 值的 optional
        Optional<String> nullOptional = Optional.ofNullable(null);
    }

    /**
     * 检查是否有值
     */
    @Test
    public void checkOptionalTest() {
        Optional<String> helloOptional = Optional.of("Hello");
        System.out.println(helloOptional.isPresent());

        Optional<String> emptyOptional = Optional.empty();
        System.out.println(emptyOptional.isPresent());

        // 如果有值，则获取想要的信息
        helloOptional.ifPresent(s -> System.out.println(s.length()));
        emptyOptional.ifPresent(s -> System.out.println(s.length()));
    }

    /**
     * 如果有值，输出长度
     */
    @Test
    public void whenIsPresentTest() {
        Optional<String> helloOptional = Optional.of("Hello");
        Optional<String> emptyOptional = Optional.empty();
        // 如果有值，则获取想要的信息
        helloOptional.ifPresent(s -> System.out.println(s.length()));
        emptyOptional.ifPresent(s -> System.out.println(s.length()));
    }

    /**
     * 如果没有值，获取默认值
     */
    @Test
    public void whenIsNullGetTest() {
        // 如果没有值，获取默认值
        Optional<String> emptyOptional = Optional.empty();
        String orElse = emptyOptional.orElse("orElse default");
        String orElseGet = emptyOptional.orElseGet(() -> "orElseGet default");
        System.out.println(orElse);
        System.out.println(orElseGet);
    }

    /**
     * 如果没有值，会抛异常
     */
    @Test
    public void getTest() {
        Optional<String> stringOptional = Optional.of("hello");
        System.out.println(stringOptional.get());
        // 如果没有值，会抛异常
        Optional<String> emptyOptional = Optional.empty();
        System.out.println(emptyOptional.get());
    }

    /**
     * orElse 和 orElseGet 的区别
     */
    @Test
    public void orElseAndOrElseGetTest() {
        // 如果没有值，默认值
        Optional<String> emptyOptional = Optional.empty();
        System.out.println("空Optional.orElse");
        String orElse = emptyOptional.orElse(getDefault());
        System.out.println("空Optional.orElseGet");
        String orElseGet = emptyOptional.orElseGet(() -> getDefault());
        System.out.println("空Optional.orElse结果：" + orElse);
        System.out.println("空Optional.orElseGet结果：" + orElseGet);
        System.out.println("--------------------------------");
        // 如果没有值，默认值
        Optional<String> stringOptional = Optional.of("hello");
        System.out.println("有值Optional.orElse");
        orElse = stringOptional.orElse(getDefault());
        System.out.println("有值Optional.orElseGet");
        orElseGet = stringOptional.orElseGet(() -> getDefault());
        System.out.println("有值Optional.orElse结果：" + orElse);
        System.out.println("有值Optional.orElseGet结果：" + orElseGet);
    }

    public String getDefault() {
        System.out.println("   获取默认值中..run getDeafult method");
        return "hello";
    }

    /**
     * 如果没有值，抛出异常
     */
    @Test
    public void whenIsNullThrowExceTest() throws Exception {
        // 如果没有值，抛出异常
        Optional<String> emptyOptional = Optional.empty();
        String value = emptyOptional.orElseThrow(() -> new Exception("发现空值"));
        System.out.println(value);
    }

    @Test
    public void functionTest() {
        // filter 过滤
        Optional<Integer> optional123 = Optional.of(123);
        optional123.filter(num -> num == 123).ifPresent(num -> System.out.println(num));

        Optional<Integer> optional456 = Optional.of(456);
        optional456.filter(num -> num == 123).ifPresent(num -> System.out.println(num));

        // map 转换
        Optional<Integer> optional789 = Optional.of(789);
        optional789.map(String::valueOf).map(String::length).ifPresent(length -> System.out.println(length));
    }

    /**
     * 电脑里【有可能】有声卡
     * 声卡【有可能】有USB接口
     */
    @Test
    public void optionalTest() {
        // 没有声卡，没有 Usb 的电脑
        Computer computerNoUsb = new Computer();
        computerNoUsb.setSoundCard(Optional.empty());
        // 获取 usb 版本
        Optional<Computer> computerOptional = Optional.ofNullable(computerNoUsb);
        String version = computerOptional.flatMap(Computer::getSoundCard).flatMap(SoundCard::getUsb)
            .map(Usb::getVersion).orElse("UNKNOWN");
        System.out.println(version);
        System.out.println("-----------------");

        // 如果有值，则输出
        SoundCard soundCard = new SoundCard();
        Usb usb = new Usb();
        usb.setVersion("2.0");
        soundCard.setUsb(Optional.ofNullable(usb));
        Optional<SoundCard> optionalSoundCard = Optional.ofNullable(soundCard);
        optionalSoundCard.ifPresent(System.out::println);
        // 如果有值，则输出
        if (optionalSoundCard.isPresent()) {
            System.out.println(optionalSoundCard.get());
        }

        // 输出没有值，则没有输出
        Optional<SoundCard> optionalSoundCardEmpty = Optional.ofNullable(null);
        optionalSoundCardEmpty.ifPresent(System.out::println);
        System.out.println("-----------------");

        // 筛选 Usb2.0
        optionalSoundCard.map(SoundCard::getUsb)
                .filter(usb1 -> "3.0".equals(usb1.map(Usb::getVersion)
                .orElse("UBKNOW")))
                .ifPresent(System.out::println);
    }

}

/**
 * 计算机
 */
class Computer {
    private Optional<SoundCard> soundCard;

    public Optional<SoundCard> getSoundCard() {
        return soundCard;
    }

    public void setSoundCard(Optional<SoundCard> soundCard) {
        this.soundCard = soundCard;
    }
}

/**
 * 声卡
 */
class SoundCard {
    private Optional<Usb> usb;

    public Optional<Usb> getUsb() {
        return usb;
    }

    public void setUsb(Optional<Usb> usb) {
        this.usb = usb;
    }
}

/**
 * USB
 */
class Usb {
    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
