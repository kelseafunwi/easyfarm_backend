package com.easyfarm.easyfarm.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandCenter implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(CommandCenter.class);

    @Override
    public void run(String... args) throws Exception {
        log.info("Started");
    }
}
