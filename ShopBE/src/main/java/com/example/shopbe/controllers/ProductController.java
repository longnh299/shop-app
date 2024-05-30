package com.example.shopbe.controllers;

import com.example.shopbe.responses.ProductListResponse;
import com.example.shopbe.responses.ProductResponse;
import com.example.shopbe.utils.StaticValues;
import com.example.shopbe.dtos.ProductDTO;
import com.example.shopbe.dtos.ProductImageDTO;
import com.example.shopbe.models.Product;
import com.example.shopbe.models.ProductImage;
import com.example.shopbe.repositories.ProductRepository;
import com.example.shopbe.services.IProductService;
import com.github.javafaker.Faker;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final ProductRepository productRepository;

    private IProductService productService;

    @Autowired
    public ProductController(IProductService productService, ProductRepository productRepository){
        this.productService = productService;
        this.productRepository = productRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable int id)  {
        try {
            Product product = productService.getProductById(id);

            ProductResponse productResponse = ProductResponse.convertFromProduct(product);

            return ResponseEntity.ok(productResponse);

        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }

    @PostMapping("")
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductDTO productDTO,
                                        BindingResult bindingResult){
        try{
            if(bindingResult.hasErrors()){
                List<String> errorMessages = bindingResult.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }

            Product newProduct = productService.createProduct(productDTO);

            return ResponseEntity.ok(newProduct);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping(value = "/uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(@ModelAttribute("files") List<MultipartFile> files, @PathVariable("id") int id) {

        try {
            if(files != null){ // check files is null and file empty when request.

                if(files.size() > StaticValues.MAX_IMAGES_PER_PRODUCT){
                    return ResponseEntity.badRequest().body("You can upload maximum 5 images!");
                }

                List<ProductImage> productImages = new ArrayList<>();

                for(MultipartFile file: files){

                    if (file != null && file.getSize() != 0 ){ // because thumbnail must not have
                        // check file size and file type
                        if(file.getSize() > 10 * 1024 * 1024){ // > 10Mb
                            //throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "File is too large");
                            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body( "File is too large, Maximun size is 10 MB.");
                        }

                        String contentType = file.getContentType();

                        if(contentType == null || !contentType.startsWith("image/")){
                            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("Media type must be image");
                        }
                    }

                    // save file and update thumbnail
                    String fileName = saveFile(file);

                    Product existsProduct = productService.getProductById(id);

                    // insert product images of a product into product_images table
                    ProductImage productImage = productService.createProductImage(existsProduct.getId(), new ProductImageDTO(existsProduct.getId(), fileName));
                    productImages.add(productImage);
                }

                return ResponseEntity.ok().body(productImages);

            }

            return null;

        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/images/")

    public ResponseEntity<?> viewImage() throws MalformedURLException {
        Path imagePath = Paths.get("uploads/notfound.png");

        UrlResource urlResource = new UrlResource(imagePath.toUri());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(urlResource);
    }

    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName){
        try {

            Path imagePath = Paths.get("uploads/" + imageName);

            UrlResource urlResource = new UrlResource(imagePath.toUri());

            if(urlResource.exists()){
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(urlResource);
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    // saveFile function
    private String saveFile(MultipartFile file) throws IOException {

            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

            // create unique file name using UUID
            String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;

            // path to directory using to dave file
            Path uploadDir = Paths.get("uploads");

            // check upload directory is exist
            if(!Files.exists(uploadDir)){
                Files.createDirectories(uploadDir);
            }

            // full path to destination file
            Path destinationFilePath = Paths.get(uploadDir.toString(), uniqueFileName);

            // copy file to upload directory
            Files.copy(file.getInputStream(), destinationFilePath, StandardCopyOption.REPLACE_EXISTING);

            return uniqueFileName;

    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id, @RequestBody ProductDTO productDTO) {

        try {
            productService.updateProduct(id, productDTO);
            return ResponseEntity.ok("Update product has id: " + id);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id) {

        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok("Delete product has id: " + id);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("")
    public ResponseEntity<ProductListResponse> getAllProduct(@RequestParam(defaultValue = "") String keyword,
                                                             @RequestParam(defaultValue = "0", name = "category_id") Long categoryId,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size){

        // pageNumber start from 0 in backend
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by("id").ascending());

        Page<ProductResponse> pageProduct =  productService.getAllProducts(keyword, categoryId, pageRequest);

        int totalPages = pageProduct.getTotalPages();

        List<ProductResponse> products = pageProduct.getContent();

        ProductListResponse productListResponse = new ProductListResponse(products, totalPages);

       return ResponseEntity.ok(productListResponse);
    }

    @PostMapping("/fakerProducts")
    public ResponseEntity<String> fakeProducts(){

        Faker faker = new Faker();

        for(int i = 0; i < 1000000; i++){
            String productName = faker.commerce().productName();
            float price = Float.parseFloat(faker.commerce().price());

            if(productService.existsByName(productName)){
                continue;
            }

            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .price(price)
                    .description(faker.lorem().sentence())
                    .thumbnail("")
                    .categoryId(faker.number().numberBetween(1, 3))
                    .build();
            try {
                productService.createProduct(productDTO);
            } catch (Exception e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }

        }

        return ResponseEntity.ok("Fake data successfully!");
    }
}
