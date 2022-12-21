package ru.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.dto.CategoryDto;
import ru.practicum.mappers.CategoryMapper;
import ru.practicum.models.Category;
import ru.practicum.repositories.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryDto save(CategoryDto categoryDto) {
        if (categoryRepository.findByName(categoryDto.getName()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Name already in use");
        }
        Category category = CategoryMapper.toCategory(categoryDto);
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    public CategoryDto update(CategoryDto categoryDto) {
        Category categoryUpdate = categoryRepository.findById(categoryDto.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        if (categoryRepository.findByName(categoryDto.getName()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Name already in use");
        }
        categoryUpdate.setName(categoryDto.getName());
        return CategoryMapper.toCategoryDto(categoryRepository.save(categoryUpdate));
    }


    public void delete(Long catId) {
        categoryRepository.deleteById(catId);
    }

    public List<CategoryDto> getAll(PageRequest pageRequest) {
        return categoryRepository.findAll(pageRequest).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }


    public CategoryDto getById(Long catId) {
        return CategoryMapper.toCategoryDto(categoryRepository.findById(catId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found")));
    }
}
