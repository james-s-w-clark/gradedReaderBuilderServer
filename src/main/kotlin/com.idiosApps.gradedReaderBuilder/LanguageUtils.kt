package com.idiosApps.gradedReaderBuilder;

import com.github.pemistahl.lingua.api.Language
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder
import java.io.File
import java.nio.file.Files

class LanguageUtils {
    companion object {
        // always keep detector in memory
        private val detector = LanguageDetectorBuilder.fromAllBuiltInLanguages().build()

        fun detectLanguage(text: String): Language {
            val detected = detector.detectLanguageOf(text)
            val name = detected.name
            return detected
        }

        fun detectLanguage(file: File): Language {
            val text = Files.readAllLines(file.toPath())
                    .take(50)
                    .joinToString()
            return detectLanguage(text)
        }

        fun getStartMarker(language: Language): String {
            return when (language) {
                Language.CHINESE -> "\\pinyin{"
                Language.ARABIC -> "\\arabicfont{"
                else -> "" // assume no marker
            }
        }

        // Try to get marking without latex if possible, e.g. Chinese tone -> unicode char
        // This will reduce latex headers/dependencies (xpinyin) and give users cleaner .tex files
        fun getMarkedL2Extra(vocabItem: Vocab, language: Language): String{
            if (vocabItem.L2Extra.isNullOrEmpty())
                return ""

            // TODO switch with (Language.CHINESE) "ChineseUtils"
            val markerStart = getStartMarker(language)
            val markerClose = if (markerStart.isEmpty()) "" else "}"
            return "(${markerStart + vocabItem.L2Extra + markerClose})"
        }
    }
}