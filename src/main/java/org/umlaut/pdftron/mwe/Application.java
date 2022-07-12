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
import java.util.List;
import java.util.stream.Stream;

@SpringBootApplication(scanBasePackages = "org.umlaut.pdftron.mwe", exclude = {DataSourceAutoConfiguration.class})
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        final ConfigurableApplicationContext context = SpringApplication.run(Application.class);
        final PdfToHtmlParserService parserService = context.getBean(PdfToHtmlParserService.class);

        for(String fileName : List.of("Test1.pdf", "Test2.pdf", "Test3.pdf", "Test4.pdf")){
            File file = ResourceUtils.getFile("classpath:" + fileName);
            try (FileInputStream inputStream = new FileInputStream(file)) {
                log.info(parserService.convertToHtml(inputStream.readAllBytes()));
            } catch (Exception e) {
                log.error("Failed to parse file {}.", file, e);
            }
        }
    }
}
