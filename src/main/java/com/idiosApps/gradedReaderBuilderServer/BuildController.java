package com.idiosApps.gradedReaderBuilderServer;

import com.idiosApps.gradedReaderBuilder.BuilderPipeline;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class BuildController {

    @RequestMapping(value = "/build", method = RequestMethod.POST)
    public File build(@RequestParam(name="outputType", required=true) String outputType,
                      @RequestPart(name="titleFile", required=true) MultipartFile titleUpload,
                      @RequestPart(name="storyFile", required=true) MultipartFile storyUpload,
                      @RequestPart(name="vocabFile", required=true) MultipartFile vocabUpload,
                      @RequestPart(name="namesFile", required=true) MultipartFile namesUpload) throws IOException {

        try (TemporaryDirectory tempDirectory = new TemporaryDirectory()) {
            BuilderPipeline pipeline = new BuilderPipeline(
                    tempDirectory.getFileFromMultipart(titleUpload),
                    tempDirectory.getFileFromMultipart(storyUpload),
                    tempDirectory.getFileFromMultipart(vocabUpload),
                    tempDirectory.getFileFromMultipart(namesUpload));

            TemporaryFile texFile = tempDirectory.getFile(".tex");
            TemporaryFile pdfFile = tempDirectory.getFile(".pdf");
            pipeline.buildGradedReader(texFile, pdfFile);

            if ("pdf".equals(outputType))
                return pdfFile;
            TemporaryFile zip = tempDirectory.getFile(".zip");
            return ZipUtils.zipFiles(zip, List.of(pdfFile, texFile));
        }
    }
}