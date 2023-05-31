package ru.itis.tsisa.blockchain;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class BlockChainBootApplication implements CommandLineRunner {

    private final BlockChainConsoleApplication consoleApplication;

    public static void main(String[] args) {
        SpringApplication.run(BlockChainBootApplication.class, args);
    }

    @Override
    public void run(String... args) {
        consoleApplication.start();
    }
}
