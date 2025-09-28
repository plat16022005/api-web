package plat.dev.config;

import plat.dev.model.*;
import plat.dev.repo.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Set;

@Configuration
public class AppConfig {
	@Bean
	CommandLineRunner init(UserRepository userRepo, CategoryRepository catRepo, ProductRepository prodRepo) {
		return args -> {
			if (userRepo.count() == 0) {
				User u1 = new User();
				u1.setFullname("Nguyen Van A");
				u1.setEmail("a@example.com");
				u1.setPassword("123");
				u1.setPhone("0909");
				User u2 = new User();
				u2.setFullname("Tran Thi B");
				u2.setEmail("b@example.com");
				u2.setPassword("123");
				u2.setPhone("0911");
				userRepo.save(u1);
				userRepo.save(u2);

				Category c1 = new Category();
				c1.setName("Điện thoại");
				Category c2 = new Category();
				c2.setName("Laptop");
				catRepo.save(c1);
				catRepo.save(c2);

// gán user–category
				u1.getCategories().add(c1);
				u1.getCategories().add(c2);
				u2.getCategories().add(c2);
				userRepo.save(u1);
				userRepo.save(u2);

				Product p1 = new Product();
				p1.setTitle("iPhone 13");
				p1.setQuantity(5);
				p1.setDesc("Hàng like new");
				p1.setPrice(new BigDecimal("15000000"));
				p1.setOwner(u1);
				p1.setCategories(Set.of(c1));

				Product p2 = new Product();
				p2.setTitle("MacBook Air M2");
				p2.setQuantity(3);
				p2.setDesc("Mới 100%");
				p2.setPrice(new BigDecimal("25000000"));
				p2.setOwner(u2);
				p2.setCategories(Set.of(c2));

				prodRepo.save(p1);
				prodRepo.save(p2);
			}
		};
	}
}