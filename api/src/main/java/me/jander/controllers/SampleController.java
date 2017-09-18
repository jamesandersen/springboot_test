package me.jander.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
public class SampleController {

    @Autowired
    private Environment environment;

    @Value("${spring.application.name}")
    private String app_name;

    @RequestMapping("/")
    @ResponseBody
    String home() {
        String bar = environment.getProperty("foo.bar");
        return "Hello James!" + bar + app_name;
    }


}
