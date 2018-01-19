package com.asiainfo.km.runner;

import com.asiainfo.km.pojo.KmException;
import com.asiainfo.km.service.DocRepoService;
import com.asiainfo.km.service.PathService;
import com.asiainfo.km.service.VcsService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1)
@Component
public class ApplicationRunner implements CommandLineRunner {
    private final PathService pathService;
    private final VcsService vcsService;
    private final DocRepoService docRepoService;

    public ApplicationRunner(PathService pathService, VcsService vcsService, DocRepoService docRepoService) {
        this.pathService = pathService;
        this.vcsService = vcsService;
        this.docRepoService = docRepoService;
    }

    @Override
    public void run(String... args) throws KmException {
        vcsService.login("jyz","jyz");
        vcsService.update();
        docRepoService.readPath();
    }
}
