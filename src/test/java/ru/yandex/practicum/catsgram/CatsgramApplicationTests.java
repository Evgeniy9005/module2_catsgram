package ru.yandex.practicum.catsgram;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.catsgram.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SpringBootTest
class CatsgramApplicationTests {

	@Test
	void contextLoads() {
		//User user = new User("e","name","2000-10-10");
		User user = new User("e","name", LocalDate.now());
		assertEquals("e",user.getEmail());
		assertEquals("name",user.getNickname());
		//assertEquals("2000-10-10",user.getBirthdate());
	}

}
