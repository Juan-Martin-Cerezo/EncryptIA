package com.example.encryptia

object Encryptor {

    // Alfabeto español con 27 letras (incluye la Ñ)
    private val ALPHABET = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ"

    // Mapa para obtener la posición de cada letra (1-27)
    private val alphabetMap = ALPHABET.withIndex().associate { it.value.lowercaseChar() to it.index + 1 }

    fun encrypt(text: String, method: String, option: String = "-"): String {
        return when (method) {
            "Palérinofu" -> palefinoEncrypt(text)
            "Murciélago" -> murcielagoEncrypt(text, option.toIntOrNull() ?: 0)
            "Paquidermo" -> paquidermoEncrypt(text, option.toIntOrNull() ?: 0)
            "Corrida" -> corridaEncrypt(text, option)
            "Corrida intrínseca" -> {
                when (option.lowercase()) {
                    "simple" -> corridaIntrinsecaSimpleEncrypt(text)
                    "compuesta" -> corridaIntrinsecaCompuestaEncrypt(text)
                    else -> corridaIntrinsecaSimpleEncrypt(text)
                }
            }
            "Autocorrida" -> {
                when (option.lowercase()) {
                    "por palabra" -> autocorridaPorPalabraEncrypt(text)
                    "por inicial" -> autocorridaPorInicialEncrypt(text)
                    else -> autocorridaPorPalabraEncrypt(text)
                }
            }
            "Abecedárica" -> abecedaricaEncrypt(text)
            "Fechada" -> fechadaEncrypt(text, option)
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
            "Murciélago" -> murcielagoDecrypt(text, option.toIntOrNull() ?: 0)
            "Paquidermo" -> paquidermoDecrypt(text, option.toIntOrNull() ?: 0)
            "Corrida" -> corridaDecrypt(text, option)
            "Corrida intrínseca" -> {
                when (option.lowercase()) {
                    "simple" -> corridaIntrinsecaSimpleDecrypt(text)
                    "compuesta" -> corridaIntrinsecaCompuestaDecrypt(text)
                    else -> corridaIntrinsecaSimpleDecrypt(text)
                }
            }
            "Autocorrida" -> {
                when (option.lowercase()) {
                    "por palabra" -> autocorridaPorPalabraDecrypt(text)
                    "por inicial" -> autocorridaPorInicialDecrypt(text)
                    else -> autocorridaPorPalabraDecrypt(text)
                }
            }
            "Abecedárica" -> abecedaricaDecrypt(text)
            "Fechada" -> fechadaDecrypt(text, option)
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

    // ---- Nuevos Métodos ----

    private fun shiftChar(c: Char, shift: Int, decrypt: Boolean = false): Char {
        val lowerC = c.lowercaseChar()
        if (lowerC !in alphabetMap.keys) return c

        val pos = alphabetMap.getValue(lowerC) - 1
        val newPos = if (decrypt) {
            (pos - shift).mod(ALPHABET.length)
        } else {
            (pos + shift).mod(ALPHABET.length)
        }

        val newChar = ALPHABET[newPos]
        return if (c.isUpperCase()) newChar else newChar.lowercaseChar()
    }

    private fun corridaIntrinsecaSimpleEncrypt(text: String): String {
        val result = StringBuilder()
        var lastOriginalLetter: Char? = null

        for (originalChar in text) {
            if (!originalChar.isLetter()) {
                result.append(originalChar)
                continue
            }

            val encryptedChar = if (lastOriginalLetter == null) {
                originalChar
            } else {
                val shift = alphabetMap[lastOriginalLetter.lowercaseChar()] ?: 0
                shiftChar(originalChar, shift)
            }
            result.append(encryptedChar)
            lastOriginalLetter = originalChar
        }
        return result.toString()
    }

    private fun corridaIntrinsecaSimpleDecrypt(text: String): String {
        val result = StringBuilder()
        var lastDecryptedLetter: Char? = null

        for (encryptedChar in text) {
            if (!encryptedChar.isLetter()) {
                result.append(encryptedChar)
                continue
            }

            val decryptedChar = if (lastDecryptedLetter == null) {
                encryptedChar
            } else {
                val shift = alphabetMap[lastDecryptedLetter.lowercaseChar()] ?: 0
                shiftChar(encryptedChar, shift, decrypt = true)
            }
            result.append(decryptedChar)
            lastDecryptedLetter = decryptedChar
        }
        return result.toString()
    }

    private fun corridaIntrinsecaCompuestaEncrypt(text: String): String {
        val result = StringBuilder()
        var lastEncryptedLetter: Char? = null

        for (originalChar in text) {
            if (!originalChar.isLetter()) {
                result.append(originalChar)
                continue
            }

            val encryptedChar = if (lastEncryptedLetter == null) {
                originalChar
            } else {
                val shift = alphabetMap[lastEncryptedLetter.lowercaseChar()] ?: 0
                shiftChar(originalChar, shift)
            }
            result.append(encryptedChar)
            lastEncryptedLetter = encryptedChar
        }
        return result.toString()
    }

    private fun corridaIntrinsecaCompuestaDecrypt(text: String): String {
        val result = StringBuilder()
        var lastEncryptedLetter: Char? = null

        for (encryptedChar in text) {
            if (!encryptedChar.isLetter()) {
                result.append(encryptedChar)
                continue
            }

            val decryptedChar = if (lastEncryptedLetter == null) {
                encryptedChar
            } else {
                val shift = alphabetMap[lastEncryptedLetter.lowercaseChar()] ?: 0
                shiftChar(encryptedChar, shift, decrypt = true)
            }
            result.append(decryptedChar)
            lastEncryptedLetter = encryptedChar
        }
        return result.toString()
    }

    private fun autocorridaPorPalabraEncrypt(text: String): String {
        return text.split(" ").joinToString(" ") { word ->
            val shift = word.length
            word.map { char -> shiftChar(char, shift) }.joinToString("")
        }
    }

    private fun autocorridaPorPalabraDecrypt(text: String): String {
        return text.split(" ").joinToString(" ") { word ->
            // La longitud no cambia, por lo que podemos usar la palabra encriptada
            val shift = word.length
            word.map { char -> shiftChar(char, shift, decrypt = true) }.joinToString("")
        }
    }

    private fun autocorridaPorInicialEncrypt(text: String): String {
        return text.split(" ").joinToString(" ") { word ->
            if (word.isEmpty()) return@joinToString ""
            val shift = alphabetMap[word[0].lowercaseChar()] ?: 0
            word[0] + word.drop(1).map { char -> shiftChar(char, shift) }.joinToString("")
        }
    }

    private fun autocorridaPorInicialDecrypt(text: String): String {
        return text.split(" ").joinToString(" ") { word ->
            if (word.isEmpty()) return@joinToString ""
            val shift = alphabetMap[word[0].lowercaseChar()] ?: 0
            word[0] + word.drop(1).map { char -> shiftChar(char, shift, decrypt = true) }.joinToString("")
        }
    }

    private fun abecedaricaEncrypt(text: String): String {
        val reversedAlphabet = ALPHABET.reversed()
        return text.map { char ->
            val index = ALPHABET.indexOf(char.uppercaseChar())
            if (index != -1) {
                val newChar = reversedAlphabet[index]
                if (char.isUpperCase()) newChar else newChar.lowercaseChar()
            } else {
                char
            }
        }.joinToString("")
    }

    private fun abecedaricaDecrypt(text: String): String = abecedaricaEncrypt(text) // Es simétrico

    private fun fechadaEncrypt(text: String, date: String): String {
        val digits = date.filter { it.isDigit() }.map { it.toString().toInt() }
        if (digits.isEmpty()) return text

        var digitIndex = 0
        return text.map { char ->
            if (char.isLetter()) {
                val shift = digits[digitIndex % digits.size]
                digitIndex++
                shiftChar(char, shift)
            } else {
                char
            }
        }.joinToString("")
    }

    private fun fechadaDecrypt(text: String, date: String): String {
        val digits = date.filter { it.isDigit() }.map { it.toString().toInt() }
        if (digits.isEmpty()) return text

        var digitIndex = 0
        return text.map { char ->
            if (char.isLetter()) {
                val shift = digits[digitIndex % digits.size]
                digitIndex++
                shiftChar(char, shift, decrypt = true)
            } else {
                char
            }
        }.joinToString("")
    }


    // ---- Métodos existentes ----

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

    private val murcielago0 = mapOf(
        'm' to '0', 'u' to '1', 'r' to '2', 'c' to '3', 'i' to '4',
        'e' to '5', 'l' to '6', 'a' to '7', 'g' to '8', 'o' to '9'
    )
    private val murcielago1 = mapOf(
        'm' to '1', 'u' to '2', 'r' to '3', 'c' to '4', 'i' to '5',
        'e' to '6', 'l' to '7', 'a' to '8', 'g' to '9', 'o' to '0'
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

    private val abc_legacy = listOf(
        'a','b','c','d','e','f','g','h','i','j','k','l','m','n','ñ','o','p','q','r','s','t','u','v','w','x','y','z'
    )

    private fun getShiftLegacy(letter: String): Int {
        val first = letter.trim().ifEmpty { "E" }[0]
        val lower = first.lowercaseChar()
        return abc_legacy.indexOf(lower).takeIf { it >= 0 } ?: 4
    }

    private fun corridaEncrypt(text: String, letter: String): String {
        val shift = getShiftLegacy(letter)
        return text.map { ch ->
            val lower = ch.lowercaseChar()
            if (lower in abc_legacy) {
                val idx = abc_legacy.indexOf(lower)
                val shifted = abc_legacy[(idx + shift) % abc_legacy.size]
                if (ch.isUpperCase()) shifted.uppercaseChar() else shifted
            } else ch
        }.joinToString("")
    }

    private fun corridaDecrypt(text: String, letter: String): String {
        val shift = getShiftLegacy(letter)
        return text.map { ch ->
            val lower = ch.lowercaseChar()
            if (lower in abc_legacy) {
                val idx = abc_legacy.indexOf(lower)
                val shifted = abc_legacy[(idx - shift + abc_legacy.size) % abc_legacy.size]
                if (ch.isUpperCase()) shifted.uppercaseChar() else shifted
            } else ch
        }.joinToString("")
    }

    private val paquidermo0 = mapOf(
        'p' to '0', 'a' to '1', 'q' to '2', 'u' to '3',
        'i' to '4', 'd' to '5', 'e' to '6', 'r' to '7',
        'm' to '8', 'o' to '9'
    )
    private val paquidermo1 = mapOf(
        'p' to '1', 'a' to '2', 'q' to '3', 'u' to '4',
        'i' to '5', 'd' to '6', 'e' to '7', 'r' to '8',
        'm' to '9', 'o' to '0'
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

    private val araucanoDict = mapOf(
        'a' to 'o', 'r' to 'n', 'u' to 'c',
        'c' to 'u', 'n' to 'r', 'o' to 'a'
    )

    private fun araucanoEncrypt(text: String) =
        text.map { ch ->
            val mapped = araucanoDict[ch.lowercaseChar()] ?: ch
            if (ch.isUpperCase()) mapped.uppercaseChar() else mapped
        }.joinToString("")

    private fun araucanoDecrypt(text: String): String = araucanoEncrypt(text)

    private val superamigosDict = mapOf(
        'u' to 'o', 'p' to 'g', 'e' to 'i', 'r' to 'm',
        'm' to 'r', 'i' to 'e', 'g' to 'p', 'o' to 'u'
    )

    private fun superamigosEncrypt(text: String) =
        text.map { ch ->
            val mapped = superamigosDict[ch.lowercaseChar()] ?: ch
            if (ch.isUpperCase()) mapped.uppercaseChar() else mapped
        }.joinToString("")

    private fun superamigosDecrypt(text: String): String = superamigosEncrypt(text)

    private val vocalicaDict = mapOf(
        'a' to '1', 'e' to '2', 'i' to '3', 'o' to '4', 'u' to '5'
    )

    private fun vocalicaEncrypt(text: String) =
        text.map { ch ->
            val mapped = vocalicaDict[ch.lowercaseChar()]?.toString() ?: ch.toString()
            if (ch.isUpperCase()) mapped.uppercase() else mapped
        }.joinToString("")

    private fun vocalicaDecrypt(text: String): String {
        val inv = vocalicaDict.entries.associate { (k, v) -> v.toString() to k.toString() }
        return text.map { ch ->
            inv[ch.toString()] ?: ch.toString()
        }.joinToString("")
    }

    private val idiomaXDict = mapOf('a' to 'u', 'e' to 'o', 'i' to 'i', 'o' to 'e', 'u' to 'a')

    private fun idiomaXEncrypt(text: String) =
        text.map { ch ->
            val mapped = idiomaXDict[ch.lowercaseChar()] ?: ch
            if (ch.isUpperCase()) mapped.uppercaseChar() else mapped
        }.joinToString("")

    private fun idiomaXDecrypt(text: String): String = idiomaXEncrypt(text)

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

    private fun dameTuPicoDecrypt(text: String): String = dameTuPicoEncrypt(text)

    private val karlinaPairs = listOf(
        'k' to 'b', 'a' to 'e', 'r' to 't', 'l' to 'f', 'i' to 'u', 'n' to 's'
    )
    private val karlinaDict: Map<Char, Char> =
        (karlinaPairs + karlinaPairs.map { (a, b) -> b to a }).toMap()

    private fun karlinaEncrypt(text: String) =
        text.map { ch ->
            val mapped = karlinaDict[ch.lowercaseChar()] ?: ch
            if (ch.isUpperCase()) mapped.uppercaseChar() else mapped
        }.joinToString("")

    private fun karlinaDecrypt(text: String): String = karlinaEncrypt(text)

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

    private fun morseEncrypt(text: String, option: String = "Normal"): String {
        val t = text.lowercase()
        val result = StringBuilder()
        var i = 0
        while (i < t.length) {
            val ch = t[i]
            when (option) {
                "Normal" -> {
                    if (morseDict.containsKey(ch)) {
                        if (result.isNotEmpty() && result.last() != '/') result.append(" ")
                        result.append(morseDict[ch])
                    } else {
                        result.append(ch)
                    }
                }
                "Extendido" -> {
                    if (morseDict.containsKey(ch)) {
                        if (result.isNotEmpty() && !result.endsWith("/")) result.append("/")
                        result.append(morseDict[ch])
                    } else {
                        result.append(ch)
                    }
                }
            }
            i++
        }
        return result.toString()
    }

    private fun morseDecrypt(text: String, option: String = "Normal"): String {
        val separator = if (option == "Normal") " " else "/"
        return text.split(separator).mapNotNull { code ->
            invMorseDict[code]
        }.joinToString("")
    }
}