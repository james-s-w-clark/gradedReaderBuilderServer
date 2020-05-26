package com.idiosApps.gradedReaderBuilderServer;

import com.idiosApps.gradedReaderBuilder.BuilderPipeline;
import org.apache.pdfbox.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Controller
public class BuildController {

    @RequestMapping(value = "/build", method = RequestMethod.POST)
    public void build(@RequestParam(name="outputType") String outputType,
                      @RequestParam(name="title") String title,
                      @RequestParam(name="author") String author,
                      @RequestPart(name="storyFile") MultipartFile storyUpload,
                      @RequestPart(name="vocabFile") MultipartFile vocabUpload,
                      @RequestPart(name="namesFile", required = false) MultipartFile namesUpload,
                      HttpServletResponse response) throws IOException {

        try (TemporaryDirectory tempDirectory = new TemporaryDirectory()) {
            BuilderPipeline pipeline = new BuilderPipeline(
                    title,
                    author,
                    tempDirectory.getFileFromMultipart(storyUpload),
                    tempDirectory.getFileFromMultipart(vocabUpload),
                    tempDirectory.getFileFromMultipart(namesUpload));

            TemporaryFile texFile = tempDirectory.getFile(".tex");
            TemporaryFile pdfFile = tempDirectory.getFile(".pdf");
            pipeline.buildGradedReader(texFile, pdfFile);

            TemporaryFile outFile = getOutputFile(outputType, pdfFile, texFile, tempDirectory);
            IOUtils.copy(new FileInputStream(outFile), response.getOutputStream());
            response.flushBuffer();

            String contentType = Files.probeContentType(outFile.toPath());
            response.setContentType(contentType);
        }
    }

    private TemporaryFile getOutputFile(String outputType, TemporaryFile pdfFile, TemporaryFile texFile, TemporaryDirectory tempDirectory) throws IOException {
        if ("pdf".equals(outputType))
            return pdfFile;

        TemporaryFile outFile = tempDirectory.getFile(".zip");
        return ZipUtils.zipFiles(outFile, List.of(pdfFile, texFile));
    }
}