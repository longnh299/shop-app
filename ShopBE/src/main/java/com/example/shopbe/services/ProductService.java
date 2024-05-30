package com.example.shopbe.services;

import com.example.shopbe.dtos.ProductDTO;
import com.example.shopbe.dtos.ProductImageDTO;
import com.example.shopbe.exceptions.DataNotFoundException;
import com.example.shopbe.exceptions.InvalidParamException;
import com.example.shopbe.models.Category;
import com.example.shopbe.models.Product;
import com.example.shopbe.models.ProductImage;
import com.example.shopbe.responses.ProductResponse;
import com.example.shopbe.utils.StaticValues;
import com.example.shopbe.repositories.CategoryRepository;
import com.example.shopbe.repositories.ProductImageRepository;
import com.example.shopbe.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductService implements IProductService{

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private ProductImageRepository productImageRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, ProductImageRepository productImageRepository){
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productImageRepository = productImageRepository;
    }

    @Override
    @Transactional
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {

        Category existsCategory = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException("Category with id: " + productDTO.getCategoryId() + " not found!"));

        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .description(productDTO.getDescription())
                .build();

        newProduct.setCategory(existsCategory);

        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(long id) throws DataNotFoundException {
        return productRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Product has id: " + id + " not found!"));
    }

    @Override
    public Page<ProductResponse> getAllProducts(String keyword, Long categoryId, PageRequest pageRequest) {


        Page<Product> productsPage;

        // convert Page<Product> to Page<ProductResponse>
        productsPage = productRepository.searchProducts(keyword, categoryId, pageRequest);

        return productsPage.map(ProductResponse::convertFromProduct);
    }

    @Override
    @Transactional
    public Product updateProduct(long id, ProductDTO productDTO) throws DataNotFoundException {

        Category existsCategory = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException("Category with id: " + productDTO.getCategoryId() + " not found!"));

        Product updatedProduct = getProductById(id);

        updatedProduct.setName(productDTO.getName());
        updatedProduct.setPrice(productDTO.getPrice());
        updatedProduct.setThumbnail(productDTO.getThumbnail());
        updatedProduct.setDescription(productDTO.getDescription());
        updatedProduct.setCategory(existsCategory);

        return productRepository.save(updatedProduct);

    }

    @Override
    @Transactional
    public void deleteProduct(long id) {
        Optional<Product> product = productRepository.findById(id); // check product exists or not ?
        product.ifPresent(productRepository::delete); // if exists => delete
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    @Transactional
    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws DataNotFoundException, InvalidParamException {
        Product existsProduct = productRepository.findById(productImageDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Product with id: " + productImageDTO.getProductId() + " not found!"));

        ProductImage newProductImage = ProductImage.builder()
                .product(existsProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();

        // check product images > 5 or < 5
        int sizeOfProductImages = productImageRepository.findByProductId(existsProduct.getId()).size();

        if (sizeOfProductImages >= StaticValues.MAX_IMAGES_PER_PRODUCT){
            throw new InvalidParamException("Number of product images must be <= 5");
        }

        return productImageRepository.save(newProductImage);
    }
}
