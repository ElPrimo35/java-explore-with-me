package ru.yandex.practicum.main.service.core.service.category;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.main.service.core.repository.category.CategoryRepository;
import ru.yandex.practicum.main.service.core.repository.event.EventRepository;
import ru.yandex.practicum.main.service.dto.CategoryDto;
import ru.yandex.practicum.main.service.exception.ConflictException;
import ru.yandex.practicum.main.service.exception.NotFoundException;
import ru.yandex.practicum.main.service.mapper.CategoryMapper;
import ru.yandex.practicum.main.service.model.Category;
import ru.yandex.practicum.main.service.model.Event;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService implements CategoryServiceInt {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final EventRepository eventRepository;


    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new ConflictException("Category name is already exists");
        }
        Category category = categoryRepository.save(categoryMapper.toCategory(categoryDto));
        return categoryMapper.toDto(category);
    }

    @Override
    public void deleteCategory(@PathVariable Integer categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Category not found"));
        List<Event> eventsByCategories = eventRepository.findByCategoryName(category.getName());
        if (!eventsByCategories.isEmpty()) {
            throw new ConflictException("The category is not empty");
        }
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {
        categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Category not found"));
        Category category = categoryMapper.toCategory(categoryDto);
        category.setId(categoryId);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public List<CategoryDto> getAllCategories(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        return categoryRepository.findAll(pageable).stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto getCategoryById(@RequestParam Integer catId) {
        Category category = categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Category not found"));
        return categoryMapper.toDto(category);
    }

}
