package com.example.encryptia

object Encryptor {

    fun encrypt(text: String, method: String): String {
        return when (method) {
            "Palérinofu" -> palefinoEncrypt(text)
            "Murciélago 0" -> murcielagoEncrypt(text)
            "Corrida en E" -> corridaEncrypt(text)
            else -> text
        }
    }

    fun decrypt(text: String, method: String): String {
        return when (method) {
            "Palérinofu" -> palefinoDecrypt(text)
            "Murciélago 0" -> murcielagoDecrypt(text)
            "Corrida en E" -> corridaDecrypt(text)
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

    // ---- Murciélago ----
    private val murcielagoDict: Map<Char, Char> = mapOf(
        'm' to '0', 'u' to '1', 'r' to '2', 'c' to '3', 'i' to '4',
        'e' to '5', 'l' to '6', 'a' to '7', 'g' to '8', 'o' to '9'
    )

    private val invMurcielagoDict: Map<Char, Char> =
        murcielagoDict.entries.associate { (k, v) -> v to k }

    private fun murcielagoEncrypt(text: String): String =
        text.map { murcielagoDict[it.lowercaseChar()] ?: it }.joinToString("")

    private fun murcielagoDecrypt(text: String): String =
        text.map { invMurcielagoDict[it] ?: it }.joinToString("")
    // ---- Corrida en E ----
    private val abc = ('a'..'z').toList()
    private val corridaDict = abc.mapIndexed { i, c -> abc[(i + 4) % 26] to c }.toMap()
    private val invCorridaDict = corridaDict.entries.associate { (k, v) -> v to k }

    private fun corridaEncrypt(text: String): String =
        text.map { corridaDict[it.lowercaseChar()] ?: it }.joinToString("")

    private fun corridaDecrypt(text: String): String =
        text.map { invCorridaDict[it.lowercaseChar()] ?: it }.joinToString("")
}