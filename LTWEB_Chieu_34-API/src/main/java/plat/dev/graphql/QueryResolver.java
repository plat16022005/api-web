package plat.dev.graphql;

import plat.dev.model.*;
import plat.dev.repo.*;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.*;

@Controller
public class QueryResolver {
    private final ProductRepository productRepo;
    private final UserRepository userRepo;
    private final CategoryRepository categoryRepo;

    public QueryResolver(ProductRepository productRepo, UserRepository userRepo, CategoryRepository categoryRepo) {
        this.productRepo = productRepo;
        this.userRepo = userRepo;
        this.categoryRepo = categoryRepo;
    }

    @QueryMapping
    public List<Product> products(@Argument Optional<String> orderByPrice) {
        // chỉ hỗ trợ ASC/DESC đơn giản theo yêu cầu
        if (orderByPrice.isPresent() && orderByPrice.get().equalsIgnoreCase("DESC")) {
            List<Product> asc = productRepo.findAllByOrderByPriceAsc();
            Collections.reverse(asc);
            return asc;
        }
        return productRepo.findAllByOrderByPriceAsc();
    }

    @QueryMapping
    public List<Product> productsByCategory(@Argument Long categoryId) {
        return productRepo.findAllByCategoryId(categoryId);
    }

    @QueryMapping
    public List<User> users() {
        return userRepo.findAll();
    }

    @QueryMapping
    public List<Category> categories() {
        return categoryRepo.findAll();
    }
}