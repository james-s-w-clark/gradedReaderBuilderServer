package com.idiosApps.gradedReaderBuilderServer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class BuildControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void getBuild() throws Exception {
        mvc.perform(MockMvcRequestBuilders.multipart("/build")
                .file(getMockMultipartFile("titleFile"))
                .file(getMockMultipartFile("storyFile"))
                .file(getMockMultipartFile("vocabFile"))
                .file(getMockMultipartFile("namesFile"))
                .queryParam("outputType", "pdf")
                .accept(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/pdf"));
    }

    private MockMultipartFile getMockMultipartFile(String resourceName) throws IOException {
        InputStream stream = this.getClass().getResourceAsStream("/gradedReaderBuilder/" + resourceName);
        return new MockMultipartFile(resourceName, stream);
    }
}