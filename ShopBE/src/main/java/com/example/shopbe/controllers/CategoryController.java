package com.example.shopbe.controllers;

import com.example.shopbe.dtos.CategoryDTO;
import com.example.shopbe.models.Category;
import com.example.shopbe.responses.MessageResponse;
import com.example.shopbe.services.CategoryService;
import com.example.shopbe.services.ICategoryService;
import com.example.shopbe.utils.LocalizationUtil;
import com.example.shopbe.utils.MessageKey;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("${api.prefix}/categories")
@Validated
public class CategoryController {

    private ICategoryService categoryService;

    private LocalizationUtil localizationUtil;

    @Autowired
    public CategoryController(ICategoryService categoryService, LocalizationUtil localizationUtil) {
        this.categoryService = categoryService;
        this.localizationUtil = localizationUtil;
    }

    @PostMapping("")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDTO categoryDTO, BindingResult bindingResult) {
        try {
            if(bindingResult.hasErrors()){
                List<String> errorMessages = bindingResult.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }

            categoryService.createCategory(categoryDTO);
            return ResponseEntity.ok("New category created: " + categoryDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<List<Category>> getAllCategory() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateCategory(@PathVariable int id, @RequestBody CategoryDTO categoryDTO, HttpServletRequest request) {

        Category newCategory = categoryService.updateCategory(id, categoryDTO);

        String localizationResponseMessage = localizationUtil.getLocalizationMessage(MessageKey.CATEGORY_UPDATE_SUCCESSFULLY);

        MessageResponse messageResponse = MessageResponse.builder()
                .message(localizationResponseMessage)
                .build();

        return ResponseEntity.ok(messageResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable int id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Delete category has id: " + id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryId(@PathVariable("id") int id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

//    @GetMapping("")
//    public ResponseEntity<String> getCategoryPaging(@RequestParam int page, @RequestParam int size) {
//        return ResponseEntity.ok(size + " Category in page: " + page);
//    }
}
