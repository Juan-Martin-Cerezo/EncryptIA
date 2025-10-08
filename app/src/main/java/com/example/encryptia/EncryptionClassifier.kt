package com.example.encryptia

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

data class TokenizerConfig(val word_index: Map<String, Int>)
data class LabelEncoderConfig(val classes: List<String>)

class EncryptionClassifier(private val context: Context) {

    companion object {
        private const val TAG = "EncryptionClassifier"
        private const val MODEL_FILE = "best_model.tflite"
        private const val TOKENIZER_FILE = "tokenizer.json"
        private const val LABEL_FILE = "label_encoder.json"
        private const val MAX_LEN = 80
    }

    private var interpreter: Interpreter? = null
    private var tokenizer: TokenizerConfig? = null
    private var labelEncoder: LabelEncoderConfig? = null

    init {
        try {
            Log.d(TAG, "🚀 Inicializando clasificador...")

            // Cargar JSONs primero
            tokenizer = loadJson(TOKENIZER_FILE, TokenizerConfig::class.java)
            labelEncoder = loadJson(LABEL_FILE, LabelEncoderConfig::class.java)

            Log.d(TAG, "📚 Tokenizer: ${tokenizer?.word_index?.size} tokens")
            Log.d(TAG, "🏷️  Clases: ${labelEncoder?.classes?.size} métodos")

            // Cargar modelo con opciones
            val modelBuffer = loadModelFile(MODEL_FILE)
            val options = Interpreter.Options().apply {
                setNumThreads(4)
            }
            interpreter = Interpreter(modelBuffer, options)

            Log.d(TAG, "✅ Modelo cargado exitosamente")

        } catch (e: Exception) {
            Log.e(TAG, "❌ Error en inicialización", e)
        }
    }

    private fun loadModelFile(filename: String): MappedByteBuffer {
        context.assets.openFd(filename).use { fd ->
            FileInputStream(fd.fileDescriptor).use { input ->
                return input.channel.map(
                    FileChannel.MapMode.READ_ONLY,
                    fd.startOffset,
                    fd.declaredLength
                )
            }
        }
    }

    private fun <T> loadJson(filename: String, clazz: Class<T>): T {
        val json = context.assets.open(filename).bufferedReader().use { it.readText() }
        return Gson().fromJson(json, clazz)
    }

    private fun preprocess(text: String): Array<IntArray> {
        val wordIndex = tokenizer?.word_index ?: emptyMap()
        val sequence = IntArray(MAX_LEN) { 0 }

        val cleanText = text.lowercase().trim()

        // Tokenizar carácter por carácter
        for (i in cleanText.indices) {
            if (i >= MAX_LEN) break
            sequence[i] = wordIndex[cleanText[i].toString()] ?: 1 // <UNK> = 1
        }

        // ✅ DEVOLVER INT32, NO FLOAT32
        return Array(1) { sequence }
    }

    fun predictWithProbabilities(text: String): List<Pair<String, Float>> {
        return try {
            if (interpreter == null) {
                Log.e(TAG, "⚠️ Intérprete no inicializado")
                return emptyList()
            }

            val classes = labelEncoder?.classes ?: return emptyList()

            // ✅ Input como INT32
            val input = preprocess(text)
            val output = Array(1) { FloatArray(classes.size) }

            interpreter?.run(input, output)

            val predictions = output[0]
            val results = classes.indices
                .map { i -> classes[i] to predictions[i] }
                .sortedByDescending { it.second }
                .take(5)

            Log.d(TAG, "🔮 Predicciones:")
            results.take(3).forEach { (method, prob) ->
                Log.d(TAG, "  $method: ${(prob * 100).format(1)}%")
            }

            results
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error en predicción", e)
            emptyList()
        }
    }

    fun close() {
        interpreter?.close()
        interpreter = null
        Log.d(TAG, "🧹 Recursos liberados")
    }
}

// Extension para formatear Float
private fun Float.format(decimals: Int): String = "%.${decimals}f".format(this)