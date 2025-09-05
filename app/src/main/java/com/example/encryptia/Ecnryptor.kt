package com.example.encryptia

object Encryptor {

    fun encrypt(text: String, method: String): String {
        return when (method) {
            "Palérinofu" -> palefinoEncrypt(text)
            "Murciélago 0" -> murcielagoEncrypt(text, 0)
            "Murciélago 1" -> murcielagoEncrypt(text, 1)
            "Corrida en E" -> corridaEncrypt(text)
            "Paquidermo 0" -> paquidermoEncrypt(text, 0)
            "Paquidermo 1" -> paquidermoEncrypt(text, 1)
            "Araucano" -> araucanoEncrypt(text)
            "Superamigos" -> superamigosEncrypt(text)
            "Vocalica" -> vocalicaEncrypt(text)
            "Idioma X" -> idiomaXEncrypt(text)
            "Dame tu pico" -> dameTuPicoEncrypt(text)
            "Karlina Betfuse" -> karlinaEncrypt(text)
            else -> text
        }
    }

    fun decrypt(text: String, method: String): String {
        return when (method) {
            "Palérinofu" -> palefinoDecrypt(text)
            "Murciélago 0" -> murcielagoDecrypt(text, 0)
            "Murciélago 1" -> murcielagoDecrypt(text, 1)
            "Corrida en E" -> corridaDecrypt(text)
            "Paquidermo 0" -> paquidermoDecrypt(text, 0)
            "Paquidermo 1" -> paquidermoDecrypt(text, 1)
            "Araucano" -> araucanoDecrypt(text)
            "Superamigos" -> superamigosDecrypt(text)
            "Vocalica" -> vocalicaDecrypt(text)
            "Idioma X" -> idiomaXDecrypt(text)
            "Dame tu pico" -> dameTuPicoDecrypt(text)
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
        text.map { palefinoDict[it.lowercaseChar()] ?: it }.joinToString("")

    private fun palefinoDecrypt(text: String): String = palefinoEncrypt(text)

    // ---- Murciélago 0----
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
        return text.map { dict[it.lowercaseChar()] ?: it }.joinToString("")
    }

    private fun murcielagoDecrypt(text: String, type: Int): String {
        val dict = if (type == 0) murcielago0 else murcielago1
        val inv = dict.entries.associate { (k, v) -> v to k }
        return text.map { inv[it] ?: it }.joinToString("")
    }

    // ---- Corrida en E ---- (shift +4 Caesar)
    private val abc = ('a'..'z').toList()
    private val corridaDict = abc.mapIndexed { i, c -> c to abc[(i + 4) % 26] }.toMap()
    private val invCorridaDict = corridaDict.entries.associate { (k, v) -> v to k }
    private fun corridaEncrypt(text: String) =
        text.map { corridaDict[it.lowercaseChar()] ?: it }.joinToString("")
    private fun corridaDecrypt(text: String) =
        text.map { invCorridaDict[it.lowercaseChar()] ?: it }.joinToString("")

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
        return text.map { dict[it.lowercaseChar()] ?: it }.joinToString("")
    }

    private fun paquidermoDecrypt(text: String, type: Int): String {
        val dict = if (type == 0) paquidermo0 else paquidermo1
        val inv = dict.entries.associate { (k, v) -> v to k }
        return text.map { inv[it] ?: it }.joinToString("")
    }

    // ---- Araucano ----
    private val araucanoDict = mapOf(
        'a' to 'o', 'r' to 'n', 'u' to 'c',
        'c' to 'u', 'n' to 'r', 'o' to 'a'
    )
    private val invAraucano = araucanoDict.entries.associate { (k, v) -> v to k }
    private fun araucanoEncrypt(text: String) =
        text.map { araucanoDict[it.lowercaseChar()] ?: it }.joinToString("")
    private fun araucanoDecrypt(text: String) =
        text.map { invAraucano[it.lowercaseChar()] ?: it }.joinToString("")

    // ---- Superamigos ----
    private val superamigosDict = mapOf(
        'u' to 'o', 'p' to 'g', 'e' to 'i', 'r' to 'm',
        'm' to 'r', 'i' to 'e', 'g' to 'p', 'o' to 'u'
    )
    private val invSuperamigos = superamigosDict.entries.associate { (k, v) -> v to k }
    private fun superamigosEncrypt(text: String) =
        text.map { superamigosDict[it.lowercaseChar()] ?: it }.joinToString("")
    private fun superamigosDecrypt(text: String) =
        text.map { invSuperamigos[it.lowercaseChar()] ?: it }.joinToString("")

    // ---- Vocalica ----
    private val vocalicaDict = mapOf(
        'a' to '1', 'e' to '2', 'i' to '3', 'o' to '4', 'u' to '5',
        '1' to 'a', '2' to 'e', '3' to 'i', '4' to 'o', '5' to 'u'
    )
    private val invVocalica = vocalicaDict.entries.associate { (k, v) -> v to k }
    private fun vocalicaEncrypt(text: String) =
        text.map { vocalicaDict[it.lowercaseChar()] ?: it }.joinToString("")
    private fun vocalicaDecrypt(text: String) =
        text.map { invVocalica[it] ?: it }.joinToString("")

    // ---- Idioma X ----
    private val idiomaXDict = mapOf('a' to 'u', 'e' to 'o', 'i' to 'i', 'o' to 'e', 'u' to 'a')
    private val invIdiomaX = idiomaXDict.entries.associate { (k, v) -> v to k }
    private fun idiomaXEncrypt(text: String) =
        text.map { idiomaXDict[it.lowercaseChar()] ?: it }.joinToString("")
    private fun idiomaXDecrypt(text: String) =
        text.map { invIdiomaX[it.lowercaseChar()] ?: it }.joinToString("")

    // ---- Dame tu pico ----
    private val dameDict = mapOf(
        'd' to 'a', 'a' to 'd', 'm' to 'e', 'e' to 'm',
        't' to 'u', 'u' to 't', 'p' to 'i', 'i' to 'p',
        'c' to 'o', 'o' to 'c'
    )
    private fun dameTuPicoEncrypt(text: String) =
        text.map { dameDict[it.lowercaseChar()] ?: it }.joinToString("")
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
        text.map { karlinaDict[it.lowercaseChar()] ?: it }.joinToString("")
    private fun karlinaDecrypt(text: String) =
        text.map { karlinaDict[it.lowercaseChar()] ?: it }.joinToString("")

}
