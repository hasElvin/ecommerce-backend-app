package com.example.dreamshops.controller;

import com.example.dreamshops.dto.ProductDto;
import com.example.dreamshops.exceptions.AlreadyExistsException;
import com.example.dreamshops.exceptions.ResourceNotFoundException;
import com.example.dreamshops.model.Product;
import com.example.dreamshops.request.AddProductRequest;
import com.example.dreamshops.request.ProductUpdateRequest;
import com.example.dreamshops.response.ApiResponse;
import com.example.dreamshops.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final IProductService productService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductDto> productDtos = productService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("Success!", productDtos));
    }

    @GetMapping("/product/{id}/product")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) {
        try {
            Product product = productService.getProductById(id);
            ProductDto productDto = productService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("success", productDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest request) {
        try {
            Product product = productService.addProduct(request);
            return ResponseEntity.ok(new ApiResponse("success", product));
        } catch (AlreadyExistsException e) {
            return ResponseEntity
                    .status(CONFLICT)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/product/{id}/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long id, @RequestBody ProductUpdateRequest request) {
        try {
            Product product = productService.updateProduct(request, id);
            return ResponseEntity.ok(new ApiResponse("success", product));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/product/{id}/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProductById(id);
            return ResponseEntity.ok(new ApiResponse("success", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/products/by/brand-and-name")
    public ResponseEntity<ApiResponse> getProductsByBrandAndName(@RequestParam String brandName,
                                                                 @RequestParam String name) {
        try {
            List<Product> products = productService.getProductsByBrandAndName(brandName, name);
            if (products.isEmpty()) {
                return ResponseEntity
                        .status(NOT_FOUND)
                        .body(new ApiResponse("No products found", null));
            }
            List<ProductDto> productDtos = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("success", productDtos));
        } catch (Exception e) {
            return ResponseEntity
                    .status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/products/by/category/{categoryName}/brand/{brandName}/product")
    public ResponseEntity<ApiResponse> getProductsByCategoryAndName(@PathVariable String categoryName,
                                                                    @PathVariable String brandName) {
        try {
            List<Product> products = productService.getProductsByCategoryAndBrand(categoryName, brandName);
            if (products.isEmpty()) {
                return ResponseEntity
                        .status(NOT_FOUND)
                        .body(new ApiResponse("No products found", null));
            }
            List<ProductDto> productDtos = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("success", productDtos));
        } catch (Exception e) {
            return ResponseEntity
                    .status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/products/{name}/products")
    public ResponseEntity<ApiResponse> getProductsByName(@PathVariable String name) {
        try {
            List<Product> products = productService.getProductsByName(name);
            if (products.isEmpty()) {
                return ResponseEntity
                        .status(NOT_FOUND)
                        .body(new ApiResponse("No products found", null));
            }
            List<ProductDto> productDtos = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("success", productDtos));
        } catch (Exception e) {
            return ResponseEntity
                    .status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/product/by-brand")
    public ResponseEntity<ApiResponse> getProductsByBrand(@RequestParam String brand) {
        try {
            List<Product> products = productService.getProductsByBrand(brand);
            if (products.isEmpty()) {
                return ResponseEntity
                        .status(NOT_FOUND)
                        .body(new ApiResponse("No products found", null));
            }
            List<ProductDto> productDtos = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("success", productDtos));
        } catch (Exception e) {
            return ResponseEntity
                    .status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/product/{category}/all/products")
    public ResponseEntity<ApiResponse> getProductsByCategory(@PathVariable String category) {
        try {
            List<Product> products = productService.getProductsByCategory(category);
            if (products.isEmpty()) {
                return ResponseEntity
                        .status(NOT_FOUND)
                        .body(new ApiResponse("No products found", null));
            }
            List<ProductDto> productDtos = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("success", productDtos));
        } catch (Exception e) {
            return ResponseEntity
                    .status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/product/count/by-brand/and-name")
    public ResponseEntity<ApiResponse> countProductsByBrandAndName(@RequestParam String brand,
                                                                   @RequestParam String name) {
        try {
            var productCount = productService.countProductsByBrandAndName(brand, name);
            return ResponseEntity.ok(new ApiResponse("Product Count", productCount));
        } catch (Exception e) {
            return ResponseEntity
                    .status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
}
