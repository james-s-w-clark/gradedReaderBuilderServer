package com.idiosApps.gradedReaderBuilderServer;

import org.apache.pdfbox.io.IOUtils;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {
    public static File zipFiles(File zip, List<File> files) throws IOException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        try (ZipOutputStream zipOut= new ZipOutputStream(bo)) {
            for (File file : files) {
                ZipEntry zipEntry = new ZipEntry(file.getName()); // probably get from query param
                zipOut.putNextEntry(zipEntry);
                zipOut.write(IOUtils.toByteArray(new FileInputStream(file)));
                zipOut.closeEntry();
            }
        }

        try(OutputStream zipOutStream = new FileOutputStream(zip)) {
            bo.writeTo(zipOutStream);
        }
        return zip;
    }
}
