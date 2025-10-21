package org.example.productService.feign;

import org.example.productService.dto.FullUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "${user.service.url}")
public interface UserClient {
    @GetMapping("/api/auth")
    FullUserDTO getUserByUsername(@RequestParam("username") String username);
}