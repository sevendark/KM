package com.asiainfo.km.runner;

import com.asiainfo.km.pojo.KmException;
import com.asiainfo.km.service.DocRepoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1)
@Component
public class ApplicationRunner implements CommandLineRunner {
    private final DocRepoService docRepoService;

    public ApplicationRunner(DocRepoService docRepoService) {
        this.docRepoService = docRepoService;
    }

    @Override
    public void run(String... args) throws KmException {
        docRepoService.readPath();
    }
}
