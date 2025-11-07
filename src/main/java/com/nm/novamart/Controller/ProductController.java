package com.nm.novamart.Controller;

import com.nm.novamart.Dto.ProductRequestDto;
import com.nm.novamart.Entity.Product;
import com.nm.novamart.Service.ProductServiceImpl;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
@RequestMapping("api/products")
public class ProductController {

    private final ProductServiceImpl productService;

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequestDto productReqDto) {
        Product product = productService.addProduct(productReqDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(){
        return ResponseEntity.status(HttpStatus.OK).body(productService.getAllProducts());
    }




}
