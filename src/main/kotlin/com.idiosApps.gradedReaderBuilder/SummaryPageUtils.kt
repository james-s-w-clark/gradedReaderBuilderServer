package com.idiosApps.gradedReaderBuilder;

import com.github.pemistahl.lingua.api.Language
import java.io.PrintWriter

class SummaryPageUtils {
    // TODO fun writeTeXGrammarSection
    // TODO fun writeTeXQuestionsSection
    // TODO fun writeNamesSection
    companion object {
        const val endLine = "\\\\"
        fun writeVocabSection(
            storyLanguage: Language,
            outputStoryWriter: PrintWriter,
            vocab: MutableList<Vocab>
        ) {
            outputStoryWriter.println("\\clearpage")
            outputStoryWriter.println("\\setlength{\\parindent}{0ex}") // remove indenting
            outputStoryWriter.println("\\centerline{Vocabulary}")     // add page title

            vocab.forEach {
                var L2Extra = LanguageUtils.getMarkedL2Extra(it, storyLanguage)
                val vocabLine = "${it.L2Word} $L2Extra ${it.L1Word}$endLine"
                outputStoryWriter.println(vocabLine)
            }
        }
    }
}