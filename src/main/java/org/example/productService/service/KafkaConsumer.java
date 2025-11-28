package org.example.productService.service;

import jakarta.transaction.Transactional;
import org.example.productService.dto.Event;
import org.example.productService.dto.UpdateQuantityFromInventory;
import org.example.productService.entity.Product;
import org.example.productService.exception.ResourceNotFoundException;
import org.example.productService.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "true", matchIfMissing = true)
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);
    private final ProductRepository productRepository;

    public KafkaConsumer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @KafkaListener(topics = "test-topic", groupId = "my-group")
    public void listen(String message) {
        log.info("Received message: {}", message);
    }

    @KafkaListener(topics = "inventory-topic", groupId = "my-group")
    @Transactional
    public void listenInventory(Event event) {
        Object rawPayload = event.getPayload();
        UpdateQuantityFromInventory payload;

        if (rawPayload instanceof Map map) {
            payload = new UpdateQuantityFromInventory();
            payload.setProductId(((Number) map.get("productId")).longValue());
            payload.setQuantity(((Number) map.get("quantity")).intValue());
        } else if (rawPayload instanceof UpdateQuantityFromInventory dto) {
            payload = dto;
        } else {
            throw new IllegalArgumentException("Unexpected payload type: " + rawPayload.getClass());
        }

        Product product = productRepository.findById(payload.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("product not found"));
        product.setQuantity(payload.getQuantity());
        productRepository.save(product);

        log.info("Updated Quantity Info From Inventory: {} {}", payload.getProductId(), payload.getQuantity());
    }

}