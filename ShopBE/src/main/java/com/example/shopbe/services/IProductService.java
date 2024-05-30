package com.example.shopbe.services;


import com.example.shopbe.dtos.ProductDTO;
import com.example.shopbe.dtos.ProductImageDTO;
import com.example.shopbe.exceptions.DataNotFoundException;
import com.example.shopbe.exceptions.InvalidParamException;
import com.example.shopbe.models.Product;
import com.example.shopbe.models.ProductImage;
import com.example.shopbe.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IProductService {

    // create new product
    Product createProduct(ProductDTO productDTO) throws DataNotFoundException;

    // get product by id
    Product getProductById(long id) throws DataNotFoundException;

    // get all product in a page
    Page<ProductResponse> getAllProducts(String keyword, Long categoryId, PageRequest pageRequest);

    //update product
    Product updateProduct(long id, ProductDTO productDTO) throws DataNotFoundException;

    // delete product
    void deleteProduct(long id);

    // check product is exists or not ?
    boolean existsByName(String name);

    ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws DataNotFoundException, InvalidParamException;
}
