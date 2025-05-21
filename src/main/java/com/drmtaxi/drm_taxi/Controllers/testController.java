package com.drmtaxi.drm_taxi.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/web/v1")
public class testController {
    @GetMapping("/test")
    public String test() {
        return "test works fine";
    }
    
    
}
