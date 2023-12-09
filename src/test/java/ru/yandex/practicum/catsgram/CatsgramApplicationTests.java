package ru.yandex.practicum.catsgram;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.User;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@SpringBootTest
@AutoConfigureMockMvc
class CatsgramApplicationTests {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	private User user = new User();

	//private Post post = new Post()
	private final List<Post> posts = new ArrayList<>();


	@BeforeEach
	void start() {
		user.setEmail("email@email");
		user.setNickname("Logan");
		user.setBirthdate(LocalDate.of(1990, 01, 02));


		for (int i = 0; i < 20; i++) {
			Post post = new Post(user.getEmail(),
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

	void createPosts(int pieces) throws Exception {
		for(int i =0 ; i < pieces; i++) {
			Post post = new Post(user.getEmail(),
					"Описание " + i,
					"URL " + i
			);
			post.setId(i);
			mockMvc.perform(post("/post")
							.content(objectMapper.writeValueAsString(post))
							.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk());
		}
	}

	@Test
	void contextLoads() throws Exception {

		//createPosts(5);

		mockMvc.perform(get("/posts?sort=desc&size=5&page=2"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		mockMvc.perform(get("/posts?sort=desc&size=5"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		System.out.println();
		System.out.println("-------------------------------------------------------------------");
		System.out.println(
		  mockMvc.perform(get("/posts"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString()
		);

	}

}
