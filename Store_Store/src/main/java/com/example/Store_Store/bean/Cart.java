package com.example.Store_Store.bean;

import com.example.Store_Store.converters.ProductConverter;
import com.example.Store_Store.dto.ProductDto;
import com.example.Store_Store.dto.ProductInCartDto;
import com.example.Store_Store.repository.ProductRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Data
public class Cart {
    private final ProductRepository productRepository;
    private final ProductConverter productConverter;
    private List<ProductInCartDto> products;

    @PostConstruct
    public void init() {
        products = new ArrayList<>();
    }

    public List<ProductInCartDto> addProductCartById(Long id) {
        ProductDto productDto = productConverter.entityToDto(productRepository.findById(id).get());
        int count = 0;
        for (ProductInCartDto product : products) {
            if (productConverter.ProductInCartDtoToProductDto(product).equals(productDto)) {
                count++;
                product.setQuantity(product.getQuantity() + 1);
            }
        }
        if (count == 0) {
            products.add(productConverter.productDtoToProductInCartDto(productDto, 1));
        }
        return products;
    }

    public List<ProductInCartDto> removeProductFromCartById(Long id) {
        products.remove(products.stream().filter(product -> product.getId().equals(id)).findFirst().orElseThrow(() -> new RuntimeException("Product not found")));
        return products;
    }

    public List<ProductInCartDto> getProducts() {
        return Collections.unmodifiableList(products);
    }
}