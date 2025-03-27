package com.example.asm_java6_api.service;

import com.example.asm_java6_api.configuration.AppVariablesConfig;
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
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
@Configuration
@EnableAsync
@RequiredArgsConstructor
public class CartService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final AppVariablesConfig appVariablesConfig;

    @NonFinal
    private ListOperations<String, Object> listOperations;
    @NonFinal
    private HashOperations<String, String, String> hashOperations;

    private String KEY_PREFIX_CART;

    private String KEY_SUFFIX_ORDER;

    @PostConstruct
    private void init() {
        listOperations = redisTemplate.opsForList();
        hashOperations = redisTemplate.opsForHash();
        KEY_PREFIX_CART = appVariablesConfig.getCartConfig().getKeyPrefixCart();
        KEY_SUFFIX_ORDER = appVariablesConfig.getCartConfig().getKeySuffixOrder();
    }

    public CartResponse getCart(HttpSession httpSession) {
        String hashKey = KEY_PREFIX_CART + httpSession.getId();
        String listKey = KEY_PREFIX_CART + httpSession.getId() + KEY_SUFFIX_ORDER;
        List<CartItemResponse> cartItemResponses = listOperations.range(listKey, 0, -1).stream()
                .map(new Function<Object, CartItemResponse>() {
                    @Override
                    public CartItemResponse apply(Object o) {
                        int productId = Integer.parseInt((String) o);
                        Optional<Product> product = productRepository.findById(productId);
                        if (product.isEmpty()) {
                            removeItem(productId, httpSession);
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

    public void saveItem(CartItemRequest cartItemRequest, HttpSession httpSession) {
        productRepository.findById(cartItemRequest.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        String hashKey = KEY_PREFIX_CART + httpSession.getId();
        String listKey = KEY_PREFIX_CART + httpSession.getId() + KEY_SUFFIX_ORDER;
        String productId = cartItemRequest.getProductId() + "";
        String currentQuantity = hashOperations.get(hashKey, productId);
        if (currentQuantity == null) {
            listOperations.leftPush(listKey, productId);
        }
        hashOperations.increment(hashKey, productId, 1);
        setTimeOutCart(httpSession);
    }

    public void removeItem(int productId, HttpSession httpSession) {
        String hashKey = KEY_PREFIX_CART + httpSession.getId();
        String listKey = KEY_PREFIX_CART + httpSession.getId() + KEY_SUFFIX_ORDER;
        hashOperations.delete(hashKey, productId + "");
        listOperations.remove(listKey, 0, productId + "");
    }

    @Async
    public void setTimeOutCart(HttpSession httpSession) {
        String hashKey = KEY_PREFIX_CART + httpSession.getId();
        String listKey = KEY_PREFIX_CART + httpSession.getId() + KEY_SUFFIX_ORDER;
        if (redisTemplate.hasKey(listKey)) {
            redisTemplate.expire(hashKey, 30, TimeUnit.MINUTES);
            redisTemplate.expire(listKey, 30, TimeUnit.MINUTES);
        }
    }
}