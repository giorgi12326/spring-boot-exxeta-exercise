package exercise.controller;

import exercise.dto.ProductDTO;
import exercise.service.ProductService;
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

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getProductBetweenPeriod(
            @RequestParam(required = false) Integer startYear,
            @RequestParam(required = false) Integer endYear){
        System.out.println("got INININ");
        return ResponseEntity.ok(productService.getProductsBetweenYears(startYear,endYear));
    }

    @PostMapping
    public ResponseEntity<ProductDTO> addProduct(@RequestBody @Valid ProductDTO productDTO){
        return ResponseEntity.status(201).body(productService.createProduct(productDTO));
    }

    @PostMapping("/batch")
    public ResponseEntity<List<ProductDTO>> addProduct(@RequestBody @Valid List<ProductDTO> productDTOs){
        return ResponseEntity.status(201).body(productService.createProducts(productDTOs));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.status(204).build();
    }
}