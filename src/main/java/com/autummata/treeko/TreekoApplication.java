package com.autummata.treeko;

import java.nio.file.Path;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.autummata.treeko.config.DotenvLoader;

/**
 * This'll be great!
 */
@SpringBootApplication
public class TreekoApplication {

	public static void main(String[] args) {
		DotenvLoader.loadIfPresent(Path.of(".env"));
		SpringApplication.run(TreekoApplication.class, args);
	}

}
