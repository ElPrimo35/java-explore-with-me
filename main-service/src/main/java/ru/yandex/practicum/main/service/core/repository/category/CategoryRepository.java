package ru.yandex.practicum.main.service.core.repository.category;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.main.service.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByName(String name);

    @Query("SELECT c FROM Category c " +
            "WHERE (?1 IS NULL OR c.name IN ?1)")
    Category findCategoryByName(String Name);
}
