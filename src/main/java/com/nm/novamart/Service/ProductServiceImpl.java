package com.nm.novamart.Service;

import com.nm.novamart.Dto.ProductRequestDto;
import com.nm.novamart.Entity.Product;
import com.nm.novamart.Mapper.ProductMapper;
import com.nm.novamart.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public Product addProduct(ProductRequestDto productReqDto) {
        if(!(productReqDto.getName().isEmpty())){
            throw new RuntimeException("Product Already Exist");
        }
        Product newProduct =  productMapper.toProduct(productReqDto);
        return productRepository.save(newProduct);
    }

    public Product updateProduct(UUID id, ProductRequestDto productReqDto) {
        if((productRepository.findById(id).isEmpty())) {
            throw new RuntimeException("Product not found");
        }
        Product newProduct =  productMapper.toProduct(productReqDto);
        return productRepository.save(newProduct);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

}
