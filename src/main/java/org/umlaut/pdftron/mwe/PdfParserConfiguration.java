package org.umlaut.pdftron.mwe;

import com.pdftron.pdf.PDFNet;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Configuration
@ConfigurationProperties(prefix = "pdf-parser")
@EnableConfigurationProperties
@Validated
public class PdfParserConfiguration {
    @NotEmpty(message = "The PDFTron Licence key is not set. Use the environment variable "
        + "PDF_PARSER_PDF_TRON_LICENCE_KEY or the spring property pdf-parser.pdf-tron-licence-key.")
    private String pdfTronLicenceKey;

    private LogLevel pdfTronLogLevel;

    public String getPdfTronLicenceKey() {
        return pdfTronLicenceKey;
    }

    public void setPdfTronLicenceKey(String pdfTronLicenceKey) {
        this.pdfTronLicenceKey = pdfTronLicenceKey;
    }

    public LogLevel getPdfTronLogLevel() {
        return pdfTronLogLevel;
    }

    public void setPdfTronLogLevel(LogLevel pdfTronLogLevel) {
        this.pdfTronLogLevel = pdfTronLogLevel;
    }

    public enum LogLevel {
        OFF(PDFNet.e_LogLevel_Off),
        DEBUG(PDFNet.e_LogLevel_Debug),
        TRACE(PDFNet.e_LogLevel_Trace),
        INFO(PDFNet.e_LogLevel_Info),
        WARNING(PDFNet.e_LogLevel_Warning),
        ERROR(PDFNet.e_LogLevel_Error),
        FATAL(PDFNet.e_LogLevel_Fatal);

        private final int pdfTronLogLevelCode;

        LogLevel(final int pdfTronLogLevelCode) {
            this.pdfTronLogLevelCode = pdfTronLogLevelCode;
        }

        public int getPdfTronLogLevelCode() {
            return pdfTronLogLevelCode;
        }
    }
}
