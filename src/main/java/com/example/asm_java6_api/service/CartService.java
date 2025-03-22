package com.example.asm_java6_api.service;

import com.example.asm_java6_api.dto.request.cart.CartItemRequest;
import com.example.asm_java6_api.dto.response.cart.CartItemResponse;
import com.example.asm_java6_api.dto.response.cart.CartResponse;
import com.example.asm_java6_api.dto.response.product.ProductResponse;
import com.example.asm_java6_api.entity.Product;
import com.example.asm_java6_api.exception.AppException;
import com.example.asm_java6_api.exception.ErrorCode;
import com.example.asm_java6_api.mapper.ProductMapper;
import com.example.asm_java6_api.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartService {
    private static final String KEY_PREFIX = "cart:";
    private static final String ORDER_SUFFIX = ":order";
    private final RedisTemplate<String, Object> redisTemplate;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    @NonFinal
    private ListOperations<String, Object> listOperations;
    @NonFinal
    private HashOperations<String, String, String> hashOperations;

    @PostConstruct
    private void init() {
        listOperations = redisTemplate.opsForList();
        hashOperations = redisTemplate.opsForHash();
    }

    int userId = 1;

    public CartResponse getCart() {
        String hashKey = KEY_PREFIX + userId;
        String listKey = KEY_PREFIX + userId + ORDER_SUFFIX;
        List<CartItemResponse> cartItemResponses =  listOperations.range(listKey, 0, -1).stream().map(new Function<Object, CartItemResponse>() {
            @Override
            public CartItemResponse apply(Object o) {
                int productId = Integer.parseInt((String)o);
                Optional<Product> product = productRepository.findById(productId);
                if (product.isEmpty()) {
                    removeItem(productId);
                }
                return CartItemResponse.builder()
                        .quantity(Integer.parseInt(hashOperations.get(hashKey, productId + "")))
                        .product(productMapper.toProductResponse(product.get()))
                        .build();
            }
        }).toList();
        return CartResponse.builder()
                .total(cartItemResponses.size())
                .items(cartItemResponses)
                .build();
    }

    public void saveItem(CartItemRequest cartItemRequest) {
        productRepository.findById(cartItemRequest.getProductId()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        String hashKey = KEY_PREFIX + userId;
        String listKey = KEY_PREFIX + userId + ORDER_SUFFIX;
        String productId = cartItemRequest.getProductId() + "";
        String currentQuantity = hashOperations.get(hashKey, productId);
        if ( currentQuantity == null ) {
            listOperations.leftPush(listKey, productId);
        }
        hashOperations.increment(hashKey, productId, 1);
    }


    public void removeItem(int productId) {
        String hashKey = KEY_PREFIX + userId;
        String listKey = KEY_PREFIX + userId + ORDER_SUFFIX;
        hashOperations.delete(hashKey, productId + "");
        listOperations.remove(listKey, 0, productId + "");
    }
}