package fr.arthur.musicplayer.helpers

import java.text.Normalizer
import kotlin.math.max

object SmartSearch {

    fun <T> search(
        list: List<T>,
        keyword: String,
        nameSelector: (T) -> String
    ): List<T> {
        if (keyword.isBlank()) return list

        val normalizedInput = normalize(keyword)
        val inputWords = normalizedInput.split(" ").filter { it.length > 2 }

        val scoredResults = mutableListOf<Pair<T, Double>>()

        list.forEach { item ->
            val field = normalize(nameSelector(item))

            when {
                field == normalizedInput -> scoredResults.add(item to 3.0) // Exact match

                inputWords.all { word -> field.contains(word) } -> {
                    val score = 2.0 + relevanceScore(field, inputWords) / 10.0
                    scoredResults.add(item to score) // Partial match
                }

                else -> {
                    val fuzzyScore = inputWords.sumOf { word ->
                        field.split(" ").maxOfOrNull { fieldWord ->
                            fuzzySimilarity(fieldWord, word)
                        } ?: 0.0
                    }
                    if (fuzzyScore > 0.5 * inputWords.size) {
                        scoredResults.add(item to fuzzyScore) // Fuzzy match
                    }
                }
            }
        }

        return scoredResults
            .distinctBy { it.first } // éviter doublons
            .sortedByDescending { it.second }
            .map { it.first }
            .take(10)
    }

    private fun normalize(text: String): String {
        return Normalizer.normalize(text, Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
            .lowercase()
            .replace("[^a-z0-9 ]".toRegex(), " ")
            .split(" ")
            .filter { it.length > 2 }
            .joinToString(" ")
    }

    private fun relevanceScore(field: String, keywords: List<String>): Int {
        val fieldWords = field.split(" ")
        return keywords.count { it in fieldWords }
    }

    private fun fuzzySimilarity(s1: String, s2: String): Double {
        val distance = levenshtein(s1, s2)
        return if (max(s1.length, s2.length) == 0) 0.0
        else 1.0 - distance.toDouble() / max(s1.length, s2.length)
    }

    private fun levenshtein(lhs: String, rhs: String): Int {
        val lhsLen = lhs.length + 1
        val rhsLen = rhs.length + 1
        val cost = Array(lhsLen) { IntArray(rhsLen) }

        for (i in 0 until lhsLen) cost[i][0] = i
        for (j in 0 until rhsLen) cost[0][j] = j

        for (i in 1 until lhsLen) {
            for (j in 1 until rhsLen) {
                cost[i][j] = minOf(
                    cost[i - 1][j] + 1,
                    cost[i][j - 1] + 1,
                    cost[i - 1][j - 1] + if (lhs[i - 1] == rhs[j - 1]) 0 else 1
                )
            }
        }
        return cost[lhsLen - 1][rhsLen - 1]
    }
}
