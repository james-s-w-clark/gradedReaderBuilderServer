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

        fun getMarker(language: Language): String {
            return when (language) {
                Language.CHINESE -> "\\pinyin{"
                Language.ARABIC -> "\\arabicfont{"
                else -> "" // assume no marker
            }
        }

        fun closeMarker(marker: String): String {
            if (marker.isEmpty()) return ""
            return "}"
        }

        fun getMarkedL2Extra(vocabItem: Vocab, language: Language): String{
            return if (vocabItem.L2Extra == null || vocabItem.L2Extra.trim() == "")
                ""
            else {
                val marker = getMarker(language)
                val markerClose = closeMarker(marker)
                return "(${marker + vocabItem.L2Extra + markerClose})"
            }
        }
    }
}