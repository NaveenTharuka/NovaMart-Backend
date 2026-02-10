package com.nm.novamart.Service;

import com.nm.novamart.Dto.Product.ProductRequestDto;
import com.nm.novamart.Dto.Product.ProductResponseDto;
import com.nm.novamart.Dto.Product.ProductUpdateReqDto;
import com.nm.novamart.Entity.Category;
import com.nm.novamart.Entity.OrderItems;
import com.nm.novamart.Entity.Product;
import com.nm.novamart.Exeptions.DuplicateProductException;
import com.nm.novamart.Exeptions.InvalidCategoryException;
import com.nm.novamart.Exeptions.ProductNotFoundException;
import com.nm.novamart.Mapper.ProductMapper;
import com.nm.novamart.Repository.CartItemRepository;
import com.nm.novamart.Repository.CategoryRepository;
import com.nm.novamart.Repository.OrderItemRepository;
import com.nm.novamart.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartServiceImpl cartService;
    private final ProductMapper productMapper;

    @Transactional
    public ProductResponseDto addProduct(ProductRequestDto productReqDto) {
        if(productRepository.existsByName(productReqDto.getName())) {
            throw new DuplicateProductException(productReqDto.getName());
        }
        Product newProduct =  productMapper.toProduct(productReqDto);
        productRepository.save(newProduct);

        return productMapper.toResponse(newProduct);
    }

    @Transactional
    public ProductResponseDto updateProduct(ProductUpdateReqDto productReqDto) {

        Product product =  productRepository.findById(productReqDto.getId())
                .orElseThrow(() -> new ProductNotFoundException(productReqDto.getId()));

        if(productRepository.existsByNameAndIdNot(productReqDto.getName(), productReqDto.getId())) {
            throw new DuplicateProductException(productReqDto.getName());
        }

        Product updateProduct = productMapper.updateProduct(product, productReqDto);

        cartService.updateAllCartItems(updateProduct);

        Product updatedProduct = productRepository.save(updateProduct);

        return productMapper.toResponse(updatedProduct);
    }

    @Transactional
    public void deleteProduct(UUID productId) {
        if(productRepository.findById(productId).isEmpty()) {
            throw new ProductNotFoundException(productId);
        }
        productRepository.deleteById(productId);
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDto> getAllProducts() {

        List<Product> products = productRepository.findAll();
        List<ProductResponseDto> productResponses = new ArrayList<>();
        for(Product product : products) {
            ProductResponseDto productResponseDto = productMapper.toResponse(product);
            productResponses.add(productResponseDto);
        }

        return productResponses;
    }

    public ProductResponseDto getProductByOrderItemId(Long id){
        Optional<OrderItems> item = orderItemRepository.findById(id);

        Product product = item.get().getProduct();
        return productMapper.toResponse(product);
    }

    @Transactional(readOnly = true)
    public ProductResponseDto getProductByName(String productName) {

        if (!(productRepository.existsByNameIgnoreCase(productName))) {
            throw new ProductNotFoundException(productName);
        }

        return productMapper.toResponse(productRepository.getProductByName(productName));
    }

    public List<ProductResponseDto> getProductsByCategory(String categoryName) {
        if(!(categoryRepository.existsByName(categoryName))) {
            throw new InvalidCategoryException(categoryName);
        }
        Category category = categoryRepository.getCategoryByName( categoryName);

        List<Product> products = productRepository.getProductByCategoryId(category.getId());
        List<ProductResponseDto> productResponses = new ArrayList<>();

        for(Product product : products) {
            ProductResponseDto productResponseDto = productMapper.toResponse(product);
            productResponses.add(productResponseDto);
        }

        return productResponses;
    }

    public ProductResponseDto getProductById(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        return productMapper.toResponse(productRepository.getProductById(id));
    }
}
