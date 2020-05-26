package com.idiosApps.gradedReaderBuilderServer;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class BuildControllerTest {

    @Autowired
    private MockMvc mvc;

    @ParameterizedTest
    @ValueSource(strings = {"chinese", "japanese"})
    void getBuild(String language) throws Exception {
        mvc.perform(MockMvcRequestBuilders.multipart("/build")
                .file(getMockMultipartFile(language, "storyFile"))
                .file(getMockMultipartFile(language, "vocabFile"))
//                .file(getMockMultipartFile(language, "namesFile"))
                .queryParam("outputType", "pdf")
                .accept(MediaType.MULTIPART_FORM_DATA)
                .param("title", "Test for " + language)
                .param("author","idiosapps"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/pdf"));
    }

    private MockMultipartFile getMockMultipartFile(String folder, String file) throws IOException {
        Path resource = Path.of("/gradedReaderBuilder", folder, file);
        InputStream stream = this.getClass().getResourceAsStream(resource.toString());
        return new MockMultipartFile(file, stream);
    }
}