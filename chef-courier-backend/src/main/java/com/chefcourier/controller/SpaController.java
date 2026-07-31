package com.chefcourier.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {

    @GetMapping({
            "/",
            "/login",
            "/register",
            "/addresses",
            "/cart",
            "/orders",
            "/owner",
            "/delivery",
            "/admin",
            "/restaurants/{restaurantId}"
    })
    public String frontend() {
        return "forward:/index.html";
    }
}
