package org.umlaut.pdftron.mwe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

@SpringBootApplication(scanBasePackages = "org.umlaut.pdftron.mwe", exclude = {DataSourceAutoConfiguration.class})
public class Application {

    private static Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        final ConfigurableApplicationContext context = SpringApplication.run(Application.class);
        final PdfToHtmlParserService parserService = context.getBean(PdfToHtmlParserService.class);
        final File file = ResourceUtils.getFile("classpath:Test.pdf");

        try (FileInputStream inputStream = new FileInputStream(file)) {
            log.info(parserService.convertToHtml(inputStream.readAllBytes()));
        }
    }
}
