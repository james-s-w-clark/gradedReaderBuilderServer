package com.idiosApps.gradedReaderBuilder

import java.io.File
import java.nio.file.Files

class VocabUtils {
    companion object {
        fun generateOrderIndicies(vocab: MutableList<Vocab>) { // TODO get from story rather than just vocab order
            vocab.forEachIndexed { index, vocabItem ->
                vocabItem.vocabOrderIndex = index
            }
        }

        fun getVocabOrderedByAppearance(storyFile: File, vocabs: MutableList<Vocab>): MutableList<Vocab> {
            val sortedVocabs = mutableListOf<Vocab>()

            Files.readAllLines(storyFile.toPath()).forEach { line ->
                val lineVocabs = vocabs.filter { line.contains(it.L2Word) }
                        .toList()
                        .sortedBy { line.indexOf(it.L2Word) }
                sortedVocabs.addAll(lineVocabs)
                vocabs.removeAll(lineVocabs)
            }

            return sortedVocabs
        }

        fun splitIntoParts(inputFile: File): MutableList<Vocab> {
            return Files.readAllLines(inputFile.toPath())
                    .map { entry -> entry.split('|') }
                    .map(::getVocabFromParts)
                    .toMutableList()
        }

        fun getVocabFromParts(parts: List<String>): Vocab {
            return when (parts.size) {
                2 -> Vocab(parts[0], null, parts[1])
                3 -> Vocab(parts[0], parts[1], parts[2])
                else -> Vocab(parts[0], parts[1], parts[2]) // don't handle 4, 5, whatever parts for now
            }
        }
    }
}