package com.asiainfo.km.test;

import com.asiainfo.km.pojo.KmException;
import com.asiainfo.km.service.PathService;
import com.asiainfo.km.service.VcsService;
import com.asiainfo.km.settings.SvnSettings;
import com.asiainfo.km.util.OsFileUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestSpringSome {
    @Autowired
    PathService pathService;
    @Autowired
    VcsService vcsService;
    @Autowired
    SvnSettings svnSettings;

    @Test
    public void testSome() throws KmException {
        vcsService.login("jyz","jyz");
        File a = new File("C:\\Users\\jiyuze\\Desktop\\新建文本文档.txt");
        //File b = OsFileUtil.newFileByOs(svnSettings.getLocalRoot(),"2222");
        vcsService.addFile(new File[]{a});
        vcsService.commit("");
    }
}
