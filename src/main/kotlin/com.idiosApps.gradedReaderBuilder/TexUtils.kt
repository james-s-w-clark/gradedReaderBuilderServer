package com.idiosApps.gradedReaderBuilder;

import java.io.*
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.util.*

class TexUtils {

    companion object {
        fun putTexLineNumbers(
            texFile: File,
            pagesInfo: MutableList<PageInfo>
        ) {
            val scanner = Scanner(texFile, "UTF-8")
            var pdfPageLastSentenceIndexer = 0
            var lineCount = 0

            while (scanner.hasNextLine()) {
                var line: String = scanner.nextLine()
                if (pdfPageLastSentenceIndexer < pagesInfo.size) {
                    val pageInfo = pagesInfo[pdfPageLastSentenceIndexer]
                    if (line.contains(pageInfo.pdfPageLastSentence)) {
                        pageInfo.texLinesOfPDFPagesLastSentence = lineCount
                        pageInfo.texLineIndexOfPDFPageLastSentence = line.lastIndexOf(pageInfo.pdfPageLastSentence)
                        pdfPageLastSentenceIndexer++
                    }
                    lineCount++
                }
            }
            scanner.close()
        }

        fun copyToTex(outputStoryWriter: PrintWriter, text: String) {
            writeLineToTex(outputStoryWriter, text)
        }

        fun copyToTex(outputStoryWriter: PrintWriter, inputFile: File) {
            Files.readAllLines(inputFile.toPath()).forEach {
                writeLineToTex(outputStoryWriter, it)
            }
        }

        private fun writeLineToTex(outputStoryWriter: PrintWriter, line: String) {
            if (line.contains("Chapter")) {
                outputStoryWriter.println("\\clearpage")
                outputStoryWriter.println("{\\centering \\large")
                outputStoryWriter.println("{\\uline{" + line + "}}\\\\}")
            } else { // (for now) assume we have ordinary text
                outputStoryWriter.println(line)
            }
        }
    }
}