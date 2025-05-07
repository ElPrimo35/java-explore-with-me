package ru.yandex.practicum.main.service.core.repository.category;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.main.service.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByName(String name);
}
