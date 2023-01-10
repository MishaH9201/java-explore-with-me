package ru.practicum.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.dto.CategoryDto;
import ru.practicum.models.Category;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryMapper {

    public static Category toCategory(CategoryDto categoryDto) {
        return new Category(categoryDto.getId(), categoryDto.getName());
    }

    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }
}
