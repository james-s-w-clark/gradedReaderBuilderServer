package com.idiosApps.gradedReaderBuilder;

import com.idiosApps.gradedReaderBuilder.TeXStyling.Companion.SUPERSCRIPT_STYLING
import com.idiosApps.gradedReaderBuilder.TeXStyling.Companion.UNDERLINE_STYLING
import com.idiosApps.gradedReaderBuilderServer.TemporaryFile
import java.io.File
import java.io.PrintWriter

class BuilderPipeline(
        private val titleFile: File,
        private val storyFile: File,
        private val vocabFile: File,
        private val namesFile: File,
)  {


    fun buildGradedReader() {
        var languageUsed = "mandarin"

        val vocab = VocabUtils.splitIntoParts(vocabFile)
        val names = VocabUtils.splitIntoParts(namesFile)

        PrintWriter(Filenames.outputTexFilename, "UTF-8").use { texWriter ->
            TexUtils.copyToTex(texWriter, this.javaClass.getResource("/gradedReaderBuilder/texHeader").file)
            TexUtils.copyToTex(texWriter, titleFile)
            TexUtils.copyToTex(texWriter, storyFile)

            SummaryPageUtils.writeVocabSection(texWriter, vocab) // TODO add summary / grammar / names pages too

            texWriter.append("\\end{document}")
        }

        PDFUtils.xelatexToPDF()

        var pagesInfo = PDFUtils.getPdfPageInfo(vocab) // store where each page's last line of text is
        TexUtils.putTexLineNumbers(pagesInfo)

        TeXStyling.addStyling(vocab, SUPERSCRIPT_STYLING)
        TeXStyling.addStyling(names, UNDERLINE_STYLING)

        FooterUtils.addVocabFooters(
            pagesInfo,
            vocab,
            languageUsed
        )

        PDFUtils.xelatexToPDF()
    }
}