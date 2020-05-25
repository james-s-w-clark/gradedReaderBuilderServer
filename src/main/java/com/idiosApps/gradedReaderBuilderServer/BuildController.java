package com.idiosApps.gradedReaderBuilderServer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class BuildController {

    @RequestMapping(value = "/build", method = RequestMethod.POST)
    public void build(@RequestParam(name="outputType", required=true) String outputType,
                        @RequestPart(name="storyFile", required=true) MultipartFile storyFile,
                        @RequestPart(name="vocabFile", required=true) MultipartFile vocabFile) {

        int one = 1;
    }

}