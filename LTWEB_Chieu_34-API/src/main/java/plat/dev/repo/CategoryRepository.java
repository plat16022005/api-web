package plat.dev.repo;

import plat.dev.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.*;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	
}
