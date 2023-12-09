package ru.yandex.practicum.catsgram.model;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PostTest {


    @Test
void test(){
    List<Integer> list = new ArrayList<>(List.of(1,2,3,4,5,6,7,8,9));
    list.add(10);
        System.out.println(list);
        System.out.println(list.subList(0,5));
        System.out.println(list);
        System.out.println(list.subList(5,10));
}

}