package com.idiosApps.gradedReaderBuilderServer;

import com.idiosApps.gradedReaderBuilder.BuilderPipeline;
import org.apache.pdfbox.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Controller
public class BuildController {

    @RequestMapping(value = "/build", method = RequestMethod.POST)
    public void build(@RequestParam(name="outputType", required=true) String outputType,
                      @RequestPart(name="titleFile", required=true) MultipartFile titleUpload,
                      @RequestPart(name="storyFile", required=true) MultipartFile storyUpload,
                      @RequestPart(name="vocabFile", required=true) MultipartFile vocabUpload,
                      @RequestPart(name="namesFile", required=true) MultipartFile namesUpload,
                      HttpServletResponse response) throws IOException {

        try (TemporaryDirectory tempDirectory = new TemporaryDirectory()) {
            BuilderPipeline pipeline = new BuilderPipeline(
                    tempDirectory.getFileFromMultipart(titleUpload),
                    tempDirectory.getFileFromMultipart(storyUpload),
                    tempDirectory.getFileFromMultipart(vocabUpload),
                    tempDirectory.getFileFromMultipart(namesUpload));

            TemporaryFile texFile = tempDirectory.getFile(".tex");
            TemporaryFile pdfFile = tempDirectory.getFile(".pdf");
            pipeline.buildGradedReader(texFile, pdfFile);

            TemporaryFile outFile;
            String contentType;
            if ("pdf".equals(outputType)) {
                outFile = pdfFile;
                contentType = "application/pdf";
            }
            else {
                outFile = tempDirectory.getFile(".zip");
                ZipUtils.zipFiles(outFile, List.of(pdfFile, texFile));
                contentType = "application/zip";
            }

            IOUtils.copy(new FileInputStream(outFile), response.getOutputStream());
            response.flushBuffer();
            response.setContentType(contentType);
            return;
        }
    }
}