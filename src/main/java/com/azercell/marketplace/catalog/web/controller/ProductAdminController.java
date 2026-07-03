package com.azercell.marketplace.catalog.web.controller;

import com.azercell.marketplace.catalog.application.service.ProductService;
import com.azercell.marketplace.catalog.web.controller.api.ProductAdminApi;
import com.azercell.marketplace.catalog.web.dto.request.AddProductRequest;
import com.azercell.marketplace.catalog.web.dto.request.UpdateProductRequest;
import com.azercell.marketplace.common.dto.ApiResponse;
import com.azercell.marketplace.catalog.web.dto.response.ProductCreatedResponse;
import com.azercell.marketplace.catalog.web.dto.response.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.UUID;

import static com.azercell.marketplace.common.dto.MessageCodes.CREATED_SUCCESSFULLY;
import static com.azercell.marketplace.common.dto.MessageCodes.UPDATED_SUCCESSFULLY;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/products")
public class ProductAdminController implements ProductAdminApi {
    private final ProductService productService;
    private final MessageSource messageSource;


    @PostMapping
    public ResponseEntity<ApiResponse<ProductCreatedResponse>> addProduct(@RequestBody AddProductRequest request,
                                                                          Locale locale) {
        var insertedId = productService.addProduct(request);
        String message = messageSource.getMessage(
                CREATED_SUCCESSFULLY.name(),
                null,
                locale);

        return new ResponseEntity<>(ApiResponse.created(
                new ProductCreatedResponse(message,
                        insertedId.toString())),
                HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable UUID id) {
        var product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.ok(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateProductRequest request,
            Locale locale) {
        String message = messageSource.getMessage(
                UPDATED_SUCCESSFULLY.name(),
                null,
                locale);
        productService.updateProduct(id, request);
        return ResponseEntity.ok(
                ApiResponse.ok(null, message));
    }


}
