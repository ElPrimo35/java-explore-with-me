package ru.yandex.practicum.main.service.core.service.category;

import ru.yandex.practicum.main.service.dto.CategoryDto;

import java.util.List;

public interface CategoryServiceInt {
    CategoryDto createCategory(CategoryDto categoryDto);

    void deleteCategory(Integer categoryId);

    CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId);

    List<CategoryDto> getAllCategories(Integer from, Integer size);

    CategoryDto getCategoryById(Integer catId);
}
