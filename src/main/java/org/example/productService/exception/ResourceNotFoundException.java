package org.example.productService.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String s) {
        super(s);
    }
}
