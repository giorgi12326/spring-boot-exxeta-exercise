package org.example.productService.controller;

import org.example.productService.dto.ProductDTO;
import org.example.productService.dto.ReserveProductDTO;
import org.example.productService.dto.ReserveResponseDTO;
import org.example.productService.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {
    public final ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductByID(@PathVariable("id") Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getProductBetweenPeriod(
            @RequestParam(required = false) Integer startYear,
            @RequestParam(required = false) Integer endYear){
        List<ProductDTO> productsBetweenYears = productService.getProductsBetweenYears(startYear, endYear);
        return ResponseEntity.ok(productsBetweenYears);
    }

    @PostMapping("/reserve")
    ResponseEntity<List<ReserveResponseDTO>> getAndReserveProducts(@RequestBody List<ReserveProductDTO> reserveProductDTO){
        return ResponseEntity.ok(productService.getAndReserveProducts(reserveProductDTO));
    }

    @PostMapping
    public ResponseEntity<ProductDTO> addProduct(@RequestBody @Valid ProductDTO productDTO){
        return ResponseEntity.status(201).body(productService.createProduct(productDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.status(204).build();
    }
}