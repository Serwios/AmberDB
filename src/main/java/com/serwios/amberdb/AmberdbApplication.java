package com.serwios.amberdb;

import com.serwios.amberdb.shell.AmberShell;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class AmberdbApplication implements CommandLineRunner {
    private final AmberShell shell;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AmberdbApplication.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        shell.start();
    }
}
