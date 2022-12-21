package ru.practicum.controllers.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CategoryDto;
import ru.practicum.service.CategoryService;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping
    public CategoryDto save(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Category {} create", categoryDto);
        return categoryService.save(categoryDto);
    }

    @PatchMapping
    public CategoryDto update(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Category {} update", categoryDto);
        return categoryService.update(categoryDto);
    }

    @DeleteMapping("/{catId}")
    public void delete(@PathVariable Long catId) {
        log.info("Category id {} delete", catId);
        categoryService.delete(catId);
    }
}