package com.drmtaxi.drm_taxi.Controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1")
public class TestController {
    @GetMapping("/test")
    public String test() {
        return "test";
    }

}
