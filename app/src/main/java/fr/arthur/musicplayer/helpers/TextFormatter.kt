package fr.arthur.musicplayer.helpers

object TextFormatter {
    
    fun format(input: String): String {
        if (input.isBlank()) return ""

        return (input
            .split("\\s+".toRegex())
            .map { word ->
                if (word.length <= 2) {
                    word.lowercase()
                } else {
                    word.substring(0, 1).uppercase() + word.substring(1).lowercase()
                }
            }).joinToString(" ").replaceFirstChar { it.uppercase() }
    }
}