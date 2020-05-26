package com.idiosApps.gradedReaderBuilder;

import com.idiosApps.gradedReaderBuilder.TeXStyling.Companion.SUPERSCRIPT_STYLING
import com.idiosApps.gradedReaderBuilder.TeXStyling.Companion.UNDERLINE_STYLING
import com.idiosApps.gradedReaderBuilderServer.TemporaryFile
import java.io.File
import java.io.PrintWriter

class BuilderPipeline(
        private val title: String,
        private val author: String,
        private val storyFile: File,
        private val vocabFile: File,
        private val namesFile: File,
) {
    companion object {
        private val TEX_BEGIN = "% Begin Document\n" +
                "\\begin{document}\n" +
                "\\maketitle\n" +
                "\\clearpage"


    }

    fun buildGradedReader(texFile: TemporaryFile, pdfFile: TemporaryFile) {
        var languageUsed = "mandarin"

        val vocab = VocabUtils.splitIntoParts(vocabFile)
        val names = VocabUtils.splitIntoParts(namesFile)

        PrintWriter(texFile, "UTF-8").use { texWriter ->
            TexUtils.copyToTex(texWriter, File(this.javaClass.getResource("/gradedReaderBuilder/texHeader").path))
            TexUtils.copyToTex(texWriter, "\\title{$title}\n\\author{$author}")
            TexUtils.copyToTex(texWriter, TEX_BEGIN)
            TexUtils.copyToTex(texWriter, storyFile)

            SummaryPageUtils.writeVocabSection(texWriter, vocab) // TODO add summary / grammar / names pages too

            texWriter.append("\\end{document}")
        }

        PDFUtils.xelatexToPDF(texFile, pdfFile)

        var pagesInfo = PDFUtils.getPdfPageInfo(vocab, pdfFile) // store where each page's last line of text is
        TexUtils.putTexLineNumbers(texFile, pagesInfo)

        TeXStyling.addStyling(texFile, vocab, SUPERSCRIPT_STYLING)
        TeXStyling.addStyling(texFile, names, UNDERLINE_STYLING)

        FooterUtils.addVocabFooters(
            texFile,
            pdfFile,
            pagesInfo,
            vocab,
            languageUsed
        )

        PDFUtils.xelatexToPDF(texFile, pdfFile)
    }
}