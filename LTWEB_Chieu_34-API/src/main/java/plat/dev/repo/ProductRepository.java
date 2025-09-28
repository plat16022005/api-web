package plat.dev.repo;

import plat.dev.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.*;

public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findAllByOrderByPriceAsc();

	@Query("select p from Product p join p.categories c where c.id = :cid")
	List<Product> findAllByCategoryId(@Param("cid") Long categoryId);
}
