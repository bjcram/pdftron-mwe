Minimal working example of failing PDFTron integration into a spring boot application running in docker.

This example application uses the pdftron java sdk, the structured output module and the reflow module. \
The build is configured via maven which downloads the modules at build time and uses the sdk as a regular dependency. \
The docker image is build via the [jib plugin](https://github.com/GoogleContainerTools/jib/tree/master/jib-maven-plugin).

The application loads a pdf file and tries to convert it to html.

# Build
1. Set a license key in the [configuration file](src/main/resources/application.properties)
2. If docker is running locally:
   ```bash
   mvn clean package jib:dockerBuild
   ```
   If the image should be pushed to another registry:
   ```bash
   mvn clean package jib:build \
       -Djib.to.image=myregistry/com.umlaut.pdftron.mwe:latest \
       -Djib.to.auth.username=$USERNAME \
       -Djib.to.auth.password=$PASSWORD
   ```

# Run
```bash
docker run com.umlaut.pdftron.mwe:latest
```

# Error
```txt
2022-07-08 09:17:15.291  INFO 1 --- [           main] o.u.pdftron.mwe.PdfToHtmlParserService   : Setting PDFTron log level to: DEBUG

PDFNet is running in demo mode.
2022-07-08 09:17:16.515  INFO 1 --- [           main] o.u.pdftron.mwe.PdfToHtmlParserService   : Using pdftron version: 9.2.0-91be0af
2022-07-08 09:17:16.562  INFO 1 --- [           main] org.umlaut.pdftron.mwe.Application       : Started Application in 2.62 seconds (JVM running for 3.024)
Permission: reflow
Exception in thread "main" Exception: 
         Message: Incorrect command line argument(s).
         Conditional expression: exit_code == 0
         Filename   : SolidStructuredOutput.cpp
         Function   : DoConvert
         Linenumber : 689
         Error code : 0

        at com.pdftron.pdf.Convert.FileToHtml(Native Method)
        at com.pdftron.pdf.Convert.toHtml(Convert.java:2296)
        at org.umlaut.pdftron.mwe.PdfToHtmlParserService.parseToHtml(PdfToHtmlParserService.java:87)
        at org.umlaut.pdftron.mwe.PdfToHtmlParserService.convertToHtml(PdfToHtmlParserService.java:69)
        at org.umlaut.pdftron.mwe.Application.main(Application.java:27)

```
