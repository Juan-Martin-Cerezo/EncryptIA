package com.example.encryptia

object Encryptor {

    fun encrypt(text: String, method: String, option: String = "-"): String {
        return when (method) {
            "Palérinofu" -> palefinoEncrypt(text)
            "Murciélago" -> {
                val type = option.toIntOrNull() ?: 0
                murcielagoEncrypt(text, type)
            }
            "Paquidermo" -> {
                val type = option.toIntOrNull() ?: 0
                paquidermoEncrypt(text, type)
            }
            "Corrida" -> corridaEncrypt(text, option)
            "Araucano" -> araucanoEncrypt(text)
            "Superamigos" -> superamigosEncrypt(text)
            "Vocalica" -> vocalicaEncrypt(text)
            "Idioma X" -> idiomaXEncrypt(text)
            "Dame tu pico" -> dameTuPicoEncrypt(text)
            "Morse" -> morseEncrypt(text, option)
            "Karlina Betfuse" -> karlinaEncrypt(text)
            else -> text
        }
    }

    fun decrypt(text: String, method: String, option: String = "-"): String {
        return when (method) {
            "Palérinofu" -> palefinoDecrypt(text)
            "Murciélago" -> {
                val type = option.toIntOrNull() ?: 0
                murcielagoDecrypt(text, type)
            }
            "Paquidermo" -> {
                val type = option.toIntOrNull() ?: 0
                paquidermoDecrypt(text, type)
            }
            "Corrida" -> corridaDecrypt(text, option)
            "Araucano" -> araucanoDecrypt(text)
            "Superamigos" -> superamigosDecrypt(text)
            "Vocalica" -> vocalicaDecrypt(text)
            "Idioma X" -> idiomaXDecrypt(text)
            "Dame tu pico" -> dameTuPicoDecrypt(text)
            "Morse" -> morseDecrypt(text, option)
            "Karlina Betfuse" -> karlinaDecrypt(text)
            else -> text
        }
    }

    // ---- Palérinofu ----
    private val palefinoDict = mapOf(
        'p' to 'a', 'a' to 'p', 'l' to 'e', 'e' to 'l',
        'r' to 'i', 'i' to 'r', 'n' to 'o', 'o' to 'n',
        'f' to 'u', 'u' to 'f'
    )

    private fun palefinoEncrypt(text: String): String =
        text.map { ch ->
            val mapped = palefinoDict[ch.lowercaseChar()] ?: ch
            if (ch.isUpperCase()) mapped.uppercaseChar() else mapped
        }.joinToString("")

    private fun palefinoDecrypt(text: String): String = palefinoEncrypt(text)

    // ---- Murciélago ----
    private val murcielago0 = mapOf(
        'm' to '0', 'u' to '1', 'r' to '2', 'c' to '3', 'i' to '4',
        'e' to '5', 'l' to '6', 'a' to '7', 'g' to '8', 'o' to '9',
        '0' to 'm', '1' to 'u', '2' to 'r', '3' to 'c', '4' to 'i',
        '5' to 'e', '6' to 'l', '7' to 'a', '8' to 'g', '9' to 'o'
    )
    private val murcielago1 = mapOf(
        'm' to '1', 'u' to '2', 'r' to '3', 'c' to '4', 'i' to '5',
        'e' to '6', 'l' to '7', 'a' to '8', 'g' to '9', 'o' to '0',
        '1' to 'm', '2' to 'u', '3' to 'r', '4' to 'c', '5' to 'i',
        '6' to 'e', '7' to 'l', '8' to 'a', '9' to 'g', '0' to 'o'
    )

    private fun murcielagoEncrypt(text: String, type: Int): String {
        val dict = if (type == 0) murcielago0 else murcielago1
        return text.map { ch ->
            val mapped = dict[ch.lowercaseChar()] ?: ch
            if (ch.isUpperCase() && mapped.isLetter()) mapped.uppercaseChar() else mapped
        }.joinToString("")
    }

    private fun murcielagoDecrypt(text: String, type: Int): String {
        val dict = if (type == 0) murcielago0 else murcielago1
        val inv = dict.entries.associate { (k, v) -> v to k }
        return text.map { ch ->
            val mapped = inv[ch] ?: ch
            if (ch.isUpperCase() && mapped.isLetter()) mapped.uppercaseChar() else mapped
        }.joinToString("")
    }

    // ---- Corrida ----
    private val abc = listOf(
        'a','b','c','d','e','f','g','h','i','j','k','l','m','n','ñ','o','p','q','r','s','t','u','v','w','x','y','z'
    )

    private fun getShift(letter: String): Int {
        val first = letter.trim().ifEmpty { "E" }[0]
        val lower = first.lowercaseChar()
        return abc.indexOf(lower).takeIf { it >= 0 } ?: 4
    }

    private fun corridaEncrypt(text: String, letter: String): String {
        val shift = getShift(letter)
        return text.map { ch ->
            val lower = ch.lowercaseChar()
            if (lower in abc) {
                val idx = abc.indexOf(lower)
                val shifted = abc[(idx + shift) % abc.size]
                if (ch.isUpperCase()) shifted.uppercaseChar() else shifted
            } else ch
        }.joinToString("")
    }

    private fun corridaDecrypt(text: String, letter: String): String {
        val shift = getShift(letter)
        return text.map { ch ->
            val lower = ch.lowercaseChar()
            if (lower in abc) {
                val idx = abc.indexOf(lower)
                val shifted = abc[(idx - shift + abc.size) % abc.size]
                if (ch.isUpperCase()) shifted.uppercaseChar() else shifted
            } else ch
        }.joinToString("")
    }

    // ---- Paquidermo ----
    private val paquidermo0 = mapOf(
        'p' to '0', 'a' to '1', 'q' to '2', 'u' to '3',
        'i' to '4', 'd' to '5', 'e' to '6', 'r' to '7',
        'm' to '8', 'o' to '9',
        '0' to 'p', '1' to 'a', '2' to 'q', '3' to 'u',
        '4' to 'i', '5' to 'd', '6' to 'e', '7' to 'r',
        '8' to 'm', '9' to 'o'
    )
    private val paquidermo1 = mapOf(
        'p' to '1', 'a' to '2', 'q' to '3', 'u' to '4',
        'i' to '5', 'd' to '6', 'e' to '7', 'r' to '8',
        'm' to '9', 'o' to '0',
        '1' to 'p', '2' to 'a', '3' to 'q', '4' to 'u',
        '5' to 'i', '6' to 'd', '7' to 'e', '8' to 'r',
        '9' to 'm', '0' to 'o'
    )

    private fun paquidermoEncrypt(text: String, type: Int): String {
        val dict = if (type == 0) paquidermo0 else paquidermo1
        return text.map { ch ->
            val mapped = dict[ch.lowercaseChar()] ?: ch
            if (ch.isUpperCase() && mapped.isLetter()) mapped.uppercaseChar() else mapped
        }.joinToString("")
    }

    private fun paquidermoDecrypt(text: String, type: Int): String {
        val dict = if (type == 0) paquidermo0 else paquidermo1
        val inv = dict.entries.associate { (k, v) -> v to k }
        return text.map { ch ->
            val mapped = inv[ch] ?: ch
            if (ch.isUpperCase() && mapped.isLetter()) mapped.uppercaseChar() else mapped
        }.joinToString("")
    }

    // ---- Araucano ----
    private val araucanoDict = mapOf(
        'a' to 'o', 'r' to 'n', 'u' to 'c',
        'c' to 'u', 'n' to 'r', 'o' to 'a'
    )
    private val invAraucano = araucanoDict.entries.associate { (k, v) -> v to k }
    private fun araucanoEncrypt(text: String) =
        text.map { ch ->
            val mapped = araucanoDict[ch.lowercaseChar()] ?: ch
            if (ch.isUpperCase()) mapped.uppercaseChar() else mapped
        }.joinToString("")

    private fun araucanoDecrypt(text: String) =
        text.map { ch ->
            val mapped = invAraucano[ch.lowercaseChar()] ?: ch
            if (ch.isUpperCase()) mapped.uppercaseChar() else mapped
        }.joinToString("")

    // ---- Superamigos ----
    private val superamigosDict = mapOf(
        'u' to 'o', 'p' to 'g', 'e' to 'i', 'r' to 'm',
        'm' to 'r', 'i' to 'e', 'g' to 'p', 'o' to 'u'
    )
    private val invSuperamigos = superamigosDict.entries.associate { (k, v) -> v to k }
    private fun superamigosEncrypt(text: String) =
        text.map { ch ->
            val mapped = superamigosDict[ch.lowercaseChar()] ?: ch
            if (ch.isUpperCase()) mapped.uppercaseChar() else mapped
        }.joinToString("")

    private fun superamigosDecrypt(text: String) =
        text.map { ch ->
            val mapped = invSuperamigos[ch.lowercaseChar()] ?: ch
            if (ch.isUpperCase()) mapped.uppercaseChar() else mapped
        }.joinToString("")

    // ---- Vocalica ----
    private val vocalicaDict = mapOf(
        'a' to '1', 'e' to '2', 'i' to '3', 'o' to '4', 'u' to '5',
        '1' to 'a', '2' to 'e', '3' to 'i', '4' to 'o', '5' to 'u'
    )
    private val invVocalica = vocalicaDict.entries.associate { (k, v) -> v to k }
    private fun vocalicaEncrypt(text: String) =
        text.map { ch ->
            val mapped = vocalicaDict[ch.lowercaseChar()] ?: ch
            if (ch.isUpperCase() && mapped.isLetter()) mapped.uppercaseChar() else mapped
        }.joinToString("")

    private fun vocalicaDecrypt(text: String) =
        text.map { ch ->
            val mapped = invVocalica[ch] ?: ch
            if (ch.isUpperCase() && mapped.isLetter()) mapped.uppercaseChar() else mapped
        }.joinToString("")

    // ---- Idioma X ----
    private val idiomaXDict = mapOf('a' to 'u', 'e' to 'o', 'i' to 'i', 'o' to 'e', 'u' to 'a')
    private val invIdiomaX = idiomaXDict.entries.associate { (k, v) -> v to k }
    private fun idiomaXEncrypt(text: String) =
        text.map { ch ->
            val mapped = idiomaXDict[ch.lowercaseChar()] ?: ch
            if (ch.isUpperCase()) mapped.uppercaseChar() else mapped
        }.joinToString("")

    private fun idiomaXDecrypt(text: String) =
        text.map { ch ->
            val mapped = invIdiomaX[ch.lowercaseChar()] ?: ch
            if (ch.isUpperCase()) mapped.uppercaseChar() else mapped
        }.joinToString("")

    // ---- Dame tu pico ----
    private val dameDict = mapOf(
        'd' to 'a', 'a' to 'd', 'm' to 'e', 'e' to 'm',
        't' to 'u', 'u' to 't', 'p' to 'i', 'i' to 'p',
        'c' to 'o', 'o' to 'c'
    )
    private fun dameTuPicoEncrypt(text: String) =
        text.map { ch ->
            val mapped = dameDict[ch.lowercaseChar()] ?: ch
            if (ch.isUpperCase()) mapped.uppercaseChar() else mapped
        }.joinToString("")

    private fun dameTuPicoDecrypt(text: String) = dameTuPicoEncrypt(text)

    // ---- Karlina Betfuse ----
    private val karlinaPairs = listOf(
        'k' to 'b',
        'a' to 'e',
        'r' to 't',
        'l' to 'f',
        'i' to 'u',
        'n' to 's'
    )
    private val karlinaDict: Map<Char, Char> =
        (karlinaPairs + karlinaPairs.map { (a, b) -> b to a }).toMap()
    private fun karlinaEncrypt(text: String) =
        text.map { ch ->
            val mapped = karlinaDict[ch.lowercaseChar()] ?: ch
            if (ch.isUpperCase()) mapped.uppercaseChar() else mapped
        }.joinToString("")

    private fun karlinaDecrypt(text: String) =
        text.map { ch ->
            val mapped = karlinaDict[ch.lowercaseChar()] ?: ch
            if (ch.isUpperCase()) mapped.uppercaseChar() else mapped
        }.joinToString("")

    // ---- Morse ----
    private val morseDict = mapOf(
        'a' to ".-", 'b' to "-...", 'c' to "-.-.", 'd' to "-..",
        'e' to ".", 'f' to "..-.", 'g' to "--.", 'h' to "....",
        'i' to "..", 'j' to ".---", 'k' to "-.-", 'l' to ".-..",
        'm' to "--", 'n' to "-.", 'ñ' to "--.--", 'o' to "---",
        'p' to ".--.", 'q' to "--.-", 'r' to ".-.", 's' to "...",
        't' to "-", 'u' to "..-", 'v' to "...-", 'w' to ".--",
        'x' to "-..-", 'y' to "-.--", 'z' to "--..",
        '0' to "-----", '1' to ".----", '2' to "..---",
        '3' to "...--", '4' to "....-", '5' to ".....",
        '6' to "-....", '7' to "--...", '8' to "---..", '9' to "----."
    )
    private val invMorseDict = morseDict.entries.associate { (k, v) -> v to k }

    // --- Encrypt ---
    private fun morseEncrypt(text: String, option: String = "Normal"): String {
        val t = text.lowercase()
        val result = StringBuilder()
        var i = 0
        while (i < t.length) {
            val ch = t[i]
            val next = t.getOrNull(i + 1)
            when (option) {
                "Normal" -> {
                    when {
                        ch == '.' && next == ' ' -> {
                            result.append("//")
                            i++ // saltar el espacio siguiente
                        }
                        ch == '.' -> result.append("///")
                        ch == ' ' -> result.append("/")
                        morseDict.containsKey(ch) -> {
                            if (result.isNotEmpty() && result.last() != '/') result.append(" ")
                            result.append(morseDict[ch])
                        }
                        else -> result.append(ch)
                    }
                }
                "Extendido" -> {
                    when {
                        ch == '.' && next == ' ' -> {
                            result.append("///")
                            i++ // saltar el espacio siguiente
                        }
                        ch == '.' -> result.append("////")
                        ch == ' ' -> result.append("//")
                        morseDict.containsKey(ch) -> {
                            if (result.isNotEmpty() && !result.endsWith("/")) result.append("/")
                            result.append(morseDict[ch])
                        }
                        else -> result.append(ch)
                    }
                }
            }
            i++
        }
        return result.toString()
    }

    // --- Decrypt ---
    private fun morseDecrypt(text: String, option: String = "Normal"): String {
        return if (option == "Normal") {
            val t = text.replace("///", "._dot_")  // punto sin espacio
                .replace("//", "._dot_space_")      // punto seguido de espacio
            t.split(" ", "/").mapNotNull { code ->
                when (code) {
                    "_dot_" -> "."
                    "_dot_space_" -> ". "
                    "" -> null
                    else -> invMorseDict[code] ?: null
                }
            }.joinToString("")
        } else {
            val t = text.replace("////", "._dot_")  // punto sin espacio
                .replace("///", "._dot_space_")     // punto seguido de espacio
            t.split("/").mapNotNull { code ->
                when (code) {
                    "_dot_" -> "."
                    "_dot_space_" -> ". "
                    "" -> null
                    else -> invMorseDict[code] ?: null
                }
            }.joinToString("")
        }
    }
}
