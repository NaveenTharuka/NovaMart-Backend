package com.nm.novamart.Repository;

import com.nm.novamart.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByName(String name);

    Category getCategoryByName(String name);
}
