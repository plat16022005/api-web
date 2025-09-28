package plat.dev.graphql;

import plat.dev.model.*;
import plat.dev.repo.*;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Controller
@Transactional
public class MutationResolver {
	private final UserRepository userRepo;
	private final CategoryRepository categoryRepo;
	private final ProductRepository productRepo;

	public MutationResolver(UserRepository userRepo, CategoryRepository categoryRepo, ProductRepository productRepo) {
		this.userRepo = userRepo;
		this.categoryRepo = categoryRepo;
		this.productRepo = productRepo;
	}

// ----- User CRUD -----
	public record UserInput(String fullname, String email, String password, String phone) {
	}

	@MutationMapping
	public User createUser(@Argument UserInput input) {
		User u = new User();
		u.setFullname(input.fullname());
		u.setEmail(input.email());
		u.setPassword(input.password());
		u.setPhone(input.phone());
		return userRepo.save(u);
	}

	@MutationMapping
	public User updateUser(@Argument Long id, @Argument UserInput input) {
		User u = userRepo.findById(id).orElseThrow();
		u.setFullname(input.fullname());
		u.setEmail(input.email());
		u.setPassword(input.password());
		u.setPhone(input.phone());
		return userRepo.save(u);
	}

	@MutationMapping
	public Boolean deleteUser(@Argument Long id) {
		if (!userRepo.existsById(id))
			return false;
		userRepo.deleteById(id);
		return true;
	}

	// ----- Category CRUD -----
    public record CategoryInput(String name, String images) {}

    @MutationMapping
    public Category createCategory(@Argument CategoryInput input) {
        Category c = new Category();
        c.setName(input.name());
        c.setImages(input.images());
        return categoryRepo.save(c);
    }

    @MutationMapping
    public Category updateCategory(@Argument Long id, @Argument CategoryInput input) {
        Category c = categoryRepo.findById(id).orElseThrow();
        c.setName(input.name());
        c.setImages(input.images());
        return categoryRepo.save(c);
    }

    @MutationMapping
    public Boolean deleteCategory(@Argument Long id) {
        if (!categoryRepo.existsById(id)) return false;
        categoryRepo.deleteById(id);
        return true;
    }

    // ----- Product CRUD -----
    public record ProductInput(String title, Integer quantity, String desc, BigDecimal price, Long userId, List<Long> categoryIds) {}

    @MutationMapping
    public Product createProduct(@Argument ProductInput input) {
        Product p = new Product();
        applyProductInput(p, input);
        return productRepo.save(p);
    }

    @MutationMapping
    public Product updateProduct(@Argument Long id, @Argument ProductInput input) {
        Product p = productRepo.findById(id).orElseThrow();
        applyProductInput(p, input);
        return productRepo.save(p);
    }

    @MutationMapping
    public Boolean deleteProduct(@Argument Long id) {
        if (!productRepo.existsById(id)) return false;
        productRepo.deleteById(id);
        return true;
    }

    private void applyProductInput(Product p, ProductInput input) {
        p.setTitle(input.title());
        p.setQuantity(input.quantity());
        p.setDesc(input.desc());
        p.setPrice(input.price());

        if (input.userId() != null) {
            User u = userRepo.findById(input.userId()).orElseThrow();
            p.setOwner(u);
        } else {
            p.setOwner(null);
        }

        // map categories nếu có
        if (input.categoryIds() != null) {
            Set<Category> cats = new HashSet<>(categoryRepo.findAllById(input.categoryIds()));
            p.setCategories(cats);
            // đồng bộ 2 chiều (không bắt buộc, nhưng tốt nếu dùng DTO)
            cats.forEach(c -> c.getProducts().add(p));
        } else {
            p.getCategories().clear();
        }
    }
}