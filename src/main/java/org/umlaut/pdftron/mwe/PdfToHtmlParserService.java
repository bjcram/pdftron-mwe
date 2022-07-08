package org.umlaut.pdftron.mwe;

import com.pdftron.common.PDFNetException;
import com.pdftron.pdf.Convert;
import com.pdftron.pdf.PDF2HtmlReflowParagraphsModule;
import com.pdftron.pdf.PDFNet;
import com.pdftron.pdf.StructuredOutputModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class PdfToHtmlParserService {
    private static final Logger log = LoggerFactory.getLogger(PdfToHtmlParserService.class);
    private final PdfParserConfiguration pdfParserConfiguration;

    @Autowired
    public PdfToHtmlParserService(PdfParserConfiguration pdfParserConfiguration) {
        this.pdfParserConfiguration = pdfParserConfiguration;
    }

    @PostConstruct
    void initializePdftronApi() throws PDFNetException {
        final PdfParserConfiguration.LogLevel logLevel = pdfParserConfiguration.getPdfTronLogLevel();

        if (logLevel != null) {
            log.info("Setting PDFTron log level to: {}", logLevel);
            PDFNet.setLogLevel(logLevel.getPdfTronLogLevelCode());
        }

        PDFNet.initialize(pdfParserConfiguration.getPdfTronLicenceKey());
        log.info("Using pdftron version: {}", PDFNet.getVersionString());

        // docker image paths
        PDFNet.addResourceSearchPath(Path.of("app", "Lib", "Linux").toString());
        PDFNet.addResourceSearchPath(Path.of("app", "PDF2HtmlReflowParagraphsModuleLinux").toString());

        if (!StructuredOutputModule.isModuleAvailable()) {
            throw new RuntimeException("StructuredOutputModule is not available. "
                    + "If you run locally refer to the services README.MD to solve this issue."
            );
        }
        if (!PDF2HtmlReflowParagraphsModule.isModuleAvailable()) {
            throw new RuntimeException("PDF2HtmlReflowParagraphsModule is not available. "
                    + "If you run locally refer to the services README.MD to solve this issue."
            );
        }
    }

    /**
     * Converts the give pdf document to HTML. To achieve this the payload needs and conversion is written to the
     * filesystem intermittently.
     *
     * @param pdfDocument the document to convert
     * @return the HTML
     * @throws IOException if the parsing or filesystem interactions fail
     */
    public String convertToHtml(final byte[] pdfDocument) throws Exception {
        final Path tempFileDirectory = Files.createTempDirectory("pdftron_mwe").toAbsolutePath();
        return parseToHtml(pdfDocument, tempFileDirectory);
    }

    private String parseToHtml(final byte[] pdfDocument, final Path tempFileDirectory) throws Exception {
        final File inputFile = new File(tempFileDirectory.toFile(), "input.pdf");
        final File outputFile = new File(tempFileDirectory.toFile(), "output.html");

        try (FileOutputStream outputStream = new FileOutputStream(inputFile)) {
            outputStream.write(pdfDocument);
        } catch (final IOException e) {
            throw new IOException("Failed to write input pdf to: %s".formatted(inputFile), e);
        }

        final Convert.HTMLOutputOptions htmlOutputOptions = new Convert.HTMLOutputOptions();
        htmlOutputOptions.setEmbedImages(true);
        htmlOutputOptions.setInternalLinks(true);
        htmlOutputOptions.setSimplifyText(true);
        htmlOutputOptions.setContentReflowSetting(Convert.HTMLOutputOptions.e_reflow_full);
        Convert.toHtml(inputFile.toString(), outputFile.toString(), htmlOutputOptions);

        return Files.readString(outputFile.toPath());

    }
}
