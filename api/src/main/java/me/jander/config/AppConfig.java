package me.jander.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public class AppConfig {

    @Autowired
    Environment environment;

    String getProperty(String key) {
        return this.environment.getProperty(key);
    }
}
