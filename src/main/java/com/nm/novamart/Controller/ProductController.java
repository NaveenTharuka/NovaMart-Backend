package com.nm.novamart.Controller;

import com.nm.novamart.Dto.Product.ProductRequestDto;
import com.nm.novamart.Dto.Product.ProductResponseDto;
import com.nm.novamart.Dto.Product.ProductUpdateReqDto;
import com.nm.novamart.Entity.Product;
import com.nm.novamart.Service.ProductServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
@RequestMapping("api/products")
public class ProductController {

    private final ProductServiceImpl productService;

    @Transactional
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequestDto productReqDto) {
        Product product = productService.addProduct(productReqDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @Transactional
    @PutMapping
    public ResponseEntity<ProductResponseDto> updateProduct(@RequestBody ProductUpdateReqDto productReqDto) {
        ProductResponseDto responseDto = productService.updateProduct(productReqDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts(){
        return ResponseEntity.status(HttpStatus.FOUND).body(productService.getAllProducts());
    }

    @GetMapping("{productName}")
    public ResponseEntity<ProductResponseDto> getProductByName(@PathVariable String productName) {
        return ResponseEntity.status(HttpStatus.FOUND).body(productService.getProductByName(productName));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.FOUND).body(productService.getProductById(id));
    }

    @GetMapping("category/{category}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByCategory(@PathVariable String category) {
        return ResponseEntity.status(HttpStatus.FOUND).body(productService.getProductsByCategory(category));
    }

    @Transactional
    @DeleteMapping("{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }




}
