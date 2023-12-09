package ru.yandex.practicum.catsgram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.InputParametersException;
import ru.yandex.practicum.catsgram.exception.PostNotFoundException;
import ru.yandex.practicum.catsgram.exception.UserNotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.User;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final UserService userService;
    private final List<Post> posts = new ArrayList<>();

    private static Integer globalId = 0;

    @Autowired
    public PostService(UserService userService) {
        this.userService = userService;
    }

    public Map<Integer,List<Post>> findAll(String sort, Integer size, Optional<Integer> page) {
        Map<Integer,List<Post>> mapPage = new HashMap<>();
        int postSize = posts.size();
        int pageGet = 0;

        boolean bNoPage = page.isEmpty() || page.get() <= 0 || page == null; //нет страниц

        if (!bNoPage) { pageGet = page.get();}

        boolean bSort = sort != null && (sort.equals("desc") || sort.equals("asc"));//есть сортировка
        boolean bSize = size != null && size > 0; //есть порядковы номер с которого нужно выводить
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


    private static Integer getNextId(){
        return globalId++;
    }

    public Post create(Post post) {
        User postAuthor = userService.findUserByEmail(post.getAuthor());
        if (postAuthor == null) {
            throw new UserNotFoundException(String.format(
                    "Пользователь %s не найден",
                    post.getAuthor()));
        }

        post.setId(getNextId());
        posts.add(post);
        return post;
    }

    public Post findPostById(Integer postId) {
        return posts.stream()
                .filter(p -> p.getId().equals(postId))
                .findFirst()
                .orElseThrow(() -> new PostNotFoundException(String.format("Пост № %d не найден", postId)));
    }


}
