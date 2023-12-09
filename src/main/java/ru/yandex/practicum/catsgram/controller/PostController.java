package ru.yandex.practicum.catsgram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    /*@GetMapping("/posts")
    public List<Post> findAll() {
        return postService.findAll();
    }

    @GetMapping("/posts")
    public List<Post> findAll(String sort, Integer size) {
        return postService.findAll();
    }*/

    @GetMapping("/posts")
    @ResponseBody
    public Map<Integer,List<Post>> findAll(@RequestParam(required = false) String sort,
                                           @RequestParam(required = false) Integer size,
                                           @RequestParam() Optional<Integer> page) {
        return postService.findAll(sort, size, page);
    }

    @PostMapping(value = "/post")
    public Post create(@RequestBody Post post) {
        return postService.create(post);
    }

    @GetMapping("/post/{postId}")
    public Post findPost(@PathVariable("postId") Integer postId){
        return postService.findPostById(postId);
    }
}
