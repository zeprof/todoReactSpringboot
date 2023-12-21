package com.lacouf.reacttodo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    // Get the value of api.url from application.properties file
    @Value("${api.url}")
    private String apiUrl;

    @GetMapping("/")
    public String index(Model model) {

        // Call Thymeleaf to set the data value in the .html file
        model.addAttribute("apiurl", apiUrl);

        return "index";
    }


}
