package com.wishstream.core;

import org.springframework.boot.SpringApplication;

public class TestWishStreamApplication {

    public static void main(String[] args) {
        SpringApplication.from(WishStreamApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
