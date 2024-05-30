package com.example.shopbe.services;

import com.example.shopbe.dtos.CategoryDTO;
import com.example.shopbe.models.Category;

import java.util.List;

public interface ICategoryService {

    Category createCategory(CategoryDTO categoryDTO);

    Category getCategoryById(long id);

    List<Category> getAllCategories();

    Category updateCategory(long id, CategoryDTO categoryDTO);

    void deleteCategory(long id);
}
