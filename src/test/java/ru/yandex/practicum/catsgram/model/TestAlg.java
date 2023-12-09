package ru.yandex.practicum.catsgram.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.catsgram.exception.InputParametersException;

import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class TestAlg {
    private final List<Post> posts = new ArrayList<>();


    @BeforeEach
    void start() {
        for (int i = 0; i < 24; i++) {
            Post post = new Post("email@email",
                    "Описание " + i,
                    "URL " + i
            );
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println(e);
            }

            posts.add(post);
        }
    }

    private List<Post> sortPosts(String sort) {
        List<Post> list = new ArrayList<>();

        if(sort.equals("asc")) { //сортировка по возростанию
            list.addAll(posts);
            list.sort(Comparator.comparing(Post::getCreationDate));
            return list;
        } else if (sort.equals("desc")) {//сортировка по убыванию
            list.addAll(posts);
            list.sort((p1,p2)-> p2.getCreationDate().compareTo(p1.getCreationDate()));
            return list;
        }
        return list;
    }

    @Test
    void testSort(){
        posts.stream().forEach(System.out::println);
        System.out.println();
        System.out.println();

        sortPosts("desc").stream().forEach(System.out::println);
        System.out.println();
        System.out.println();

        sortPosts("asc").stream().forEach(System.out::println);
        System.out.println();
        System.out.println();
    }


    public Map<Integer,List<Post>> findAll(String sort, Integer size, Optional<Integer> page) {
        Map<Integer,List<Post>> mapPage = new HashMap<>();
        int postSize = posts.size();
        int pageGet = page.get();

        boolean bNoPage = page.isEmpty() || page.get() <= 0; //нет страниц
        boolean bSort = sort.equals("desc") || sort.equals("asc");//есть сортировка
        boolean bSize = size > 0 && size != null; //есть порядковы номер с которого нужно выводить
        boolean bPageSize = bSize && !bNoPage; //есть количество страниц и количество постов

        //Вывод постов в количестве 10 штук или меньше если они есть
        //отсортировать по дате, по возрастанию и вернуть 10 самых свежих постов
        if(!bSort && bSize && !bNoPage || //нет сортировки есть листы и есть посты
                bSort && !bSize && !bNoPage || //есть сортировка нет постов есть литы
                !bSort && !bSize && !bNoPage || //если нет сортировки нет постов но есть листы
                !bSort && bSize && bNoPage // нет сортировки есть посты нет листов
        ) {
            if(postSize >= 10) {
                mapPage.put(1,sortPosts("asc").subList(0,10));
            } else {
                mapPage.put(1,sortPosts("asc"));
            }
            return mapPage;
        }

        //Вывод постов c заданного номера
        if(bSort && bSize && bNoPage) { //есть сортировка и есть посты но нет листов
            if(postSize > size ) {
                mapPage.put(1,posts.subList(size,postSize));
                return mapPage;
            } else if(postSize >= 10) {
                mapPage.put(1,sortPosts(sort).subList(0,10));
                return mapPage;
            } else {
                mapPage.put(1,sortPosts(sort));
                return mapPage;
            }
        }


        //Вывод постов постранично
        if(bPageSize && bSort && !posts.isEmpty()) { //есть сортировка посты и лесты
            int tail = 0;
            int pageAll = postSize/size; //вычисляем количество листов
            int pagePrint = (pageGet < pageAll ? pageGet: pageAll) * size; //вычисляем возможное количество листов
            int key = 0;

            List<Post> postsSort = sortPosts(sort);

            for(int i = 0; i < pagePrint; i = i + size) {

                mapPage.put(++key, postsSort.subList(i, i + size));

            }

            int remains = postSize%size;
            tail = postSize-remains;

            if( tail != postSize && remains < size && pageGet > 2) {
                mapPage.put(++key,postsSort.subList(tail,postSize));
            }

            return mapPage;
        }

        mapPage.put(1,posts);
        return mapPage;
    }

    @Test
    void testA(){
        String desc = "desc";
        String asc = "asc";
        String des = "";
        Map<Integer,List<Post>> map1 = findAll(desc,5,Optional.of(7));

        System.out.println(map1.keySet());
        System.out.println();
        System.out.println();

        for(int i=1; i < map1.keySet().size()+1; i++) {
            System.out.println(map1.get(i));
            System.out.println(i);
        }

    }

}
