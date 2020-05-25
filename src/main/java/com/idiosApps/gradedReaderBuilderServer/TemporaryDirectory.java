package com.idiosApps.gradedReaderBuilderServer;

import org.springframework.web.multipart.MultipartFile;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class TemporaryDirectory implements Closeable {
    private static final String TMP_EXT = ".tmp";
    private Path directory;

    public TemporaryDirectory() throws IOException {
        directory = Files.createTempDirectory(null);
    }

    @Override
    public void close() throws IOException {
        directory.toFile().delete();
    }

    public TemporaryFile getFile() throws IOException {
        return new TemporaryFile((Files.createTempFile(directory, UUID.randomUUID().toString(), TMP_EXT)));
    }

    public TemporaryFile getFileFromMultipart(MultipartFile multipartFile) throws IOException {
        TemporaryFile temporaryFile = new TemporaryFile((Files.createTempFile(directory, UUID.randomUUID().toString(), TMP_EXT)));
        multipartFile.transferTo(temporaryFile);
        return temporaryFile;
    }
}
