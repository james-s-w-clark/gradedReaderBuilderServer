package com.idiosApps.gradedReaderBuilder.languageUtils

import java.lang.StringBuilder

class ChineseUtils {
    companion object {
        private val VOWELS_UNACCENTED = charArrayOf('a', 'e', 'i', 'o', 'u', 'ü')
        private val A_ACCENTs = charArrayOf('a', 'ā', 'á', 'ǎ', 'à')
        private val E_ACCENTs = charArrayOf('e', 'ē', 'é', 'ě', 'è')
        private val I_ACCENTs = charArrayOf('i', 'ī', 'í', 'ǐ', 'ì')
        private val O_ACCENTs = charArrayOf('o', 'ō', 'ó', 'ǒ', 'ò')
        private val U_ACCENTs = charArrayOf('u', 'ū', 'ú', 'ǔ', 'ù')
        private val U_UMLAUT_ACCENTs = charArrayOf('ü', 'ǖ', 'ǘ', 'ǚ', 'ǜ')

        private val PINYIN_SPLITTER_REGEX = "(?<=\\d)(?=\\D)".toRegex()

        private fun getAccentedChar(char: Char, tone: Int): Char {
            return when (char) {
                A_ACCENTs[0] -> A_ACCENTs[tone]
                E_ACCENTs[0] -> E_ACCENTs[tone]
                I_ACCENTs[0] -> I_ACCENTs[tone]
                O_ACCENTs[0] -> O_ACCENTs[tone]
                U_ACCENTs[0] -> U_ACCENTs[tone]
                U_UMLAUT_ACCENTs[0] -> U_UMLAUT_ACCENTs[tone]
                else -> char
            }
        }

        // e.g. ni3 hao3 -> nĭ hăo
        fun getAccentedPinyin(numberedPinyin: String): String {
            val stringBuilder = StringBuilder()
            for (syllable in numberedPinyin.split(PINYIN_SPLITTER_REGEX)) {
                val accentableChar = syllable[syllable.indexOfAny(VOWELS_UNACCENTED)]
                val toneChar = syllable[syllable.length - 1]
                val tone = Character.getNumericValue(toneChar)

                val accentedChar = getAccentedChar(accentableChar, tone)

                stringBuilder.append(syllable
                        .replace(accentableChar, accentedChar)
                        .substring(0, syllable.length - 1))
            }
            return stringBuilder.toString()
        }
    }
}