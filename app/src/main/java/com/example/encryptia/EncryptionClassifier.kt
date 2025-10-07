package com.example.encryptia

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

// Configuración del tokenizador
data class TokenizerConfig(
    val word_index: Map<String, Int>
)

// Configuración del codificador de etiquetas
data class LabelEncoderConfig(
    val classes: List<String>
)

class EncryptionClassifier(private val context: Context) {

    private var interpreter: Interpreter? = null
    private var tokenizer: TokenizerConfig? = null
    private var labelEncoder: LabelEncoderConfig? = null
    private val maxLen = 80

    init {
        try {
            val modelBuffer = loadModelFile("best_model.tflite")
            interpreter = Interpreter(modelBuffer)
            tokenizer = loadJson("tokenizer.json", TokenizerConfig::class.java)
            labelEncoder = loadJson("label_encoder.json", LabelEncoderConfig::class.java)

            Log.d("EncryptionClassifier", "✅ Modelo cargado correctamente")
            Log.d("EncryptionClassifier", "Tokenizer con ${tokenizer?.word_index?.size} tokens")
            Log.d("EncryptionClassifier", "Clases detectadas: ${labelEncoder?.classes}")
        } catch (e: Exception) {
            Log.e("EncryptionClassifier", "❌ Error cargando modelo o archivos JSON", e)
        }
    }

    /** Carga el modelo .tflite desde assets */
    private fun loadModelFile(filename: String): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(filename)
        FileInputStream(fileDescriptor.fileDescriptor).use { input ->
            val channel = input.channel
            return channel.map(FileChannel.MapMode.READ_ONLY, fileDescriptor.startOffset, fileDescriptor.declaredLength)
        }
    }

    /** Carga un archivo JSON y lo convierte al objeto especificado */
    private fun <T> loadJson(filename: String, clazz: Class<T>): T {
        val json = context.assets.open(filename).bufferedReader().use { it.readText() }
        return Gson().fromJson(json, clazz)
    }

    /** Convierte texto a una secuencia numérica para el modelo */
    private fun textToSequence(text: String): IntArray {
        val wordIndex = tokenizer?.word_index ?: return IntArray(maxLen)
        val sequence = mutableListOf<Int>()
        val lowerText = text.lowercase()

        lowerText.forEach { char ->
            val index = wordIndex[char.toString()] ?: 0
            sequence.add(index)
        }

        val padded = IntArray(maxLen)
        for (i in 0 until minOf(sequence.size, maxLen)) {
            padded[i] = sequence[i]
        }

        return padded
    }

    /** Ejecuta la predicción en el modelo y devuelve las 5 más probables */
    fun predictWithProbabilities(text: String): List<Pair<String, Float>> {
        return try {
            val inputArray = Array(1) { FloatArray(maxLen) }
            val sequence = textToSequence(text)
            for (i in sequence.indices) inputArray[0][i] = sequence[i].toFloat()

            val numClasses = labelEncoder?.classes?.size ?: 0
            val outputArray = Array(1) { FloatArray(numClasses) }

            interpreter?.run(inputArray, outputArray)

            val predictions = outputArray[0]
            val classes = labelEncoder?.classes ?: emptyList()

            classes.indices.map { i -> classes[i] to predictions[i] }
                .sortedByDescending { it.second }
                .take(5)
        } catch (e: Exception) {
            Log.e("EncryptionClassifier", "❌ Error al predecir", e)
            emptyList()
        }
    }

    fun close() {
        interpreter?.close()
    }
}
