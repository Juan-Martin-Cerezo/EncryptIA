package com.example.encryptia

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class DateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 6) text.text.substring(0..5) else text.text
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i % 2 == 1 && i < 4) out += "/"
        }

        val dateOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 1) return offset
                if (offset <= 3) return offset + 1
                if (offset <= 5) return offset + 2
                return 8
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 5) return offset - 1
                if (offset <= 8) return offset - 2
                return 6
            }
        }

        return TransformedText(AnnotatedString(out), dateOffsetTranslator)
    }
}

class MainActivity : ComponentActivity() {

    private lateinit var classifier: EncryptionClassifier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        classifier = EncryptionClassifier(this)

        setContent {
            val keys = listOf(
                "Palérinofu", "Murciélago", "Corrida",
                "Corrida intrínseca", "Autocorrida", "Abecedárica", "Fechada",
                "Paquidermo", "Araucano", "Superamigos", "Vocalica",
                "Idioma X", "Dame tu pico", "Karlina Betfuse", "Morse"
            ).sorted()

            var showBook by remember { mutableStateOf(false) }
            var showCompass by remember { mutableStateOf(false) }
            var selectedInfoKey by remember { mutableStateOf<String?>(null) }
            var selectedKey by remember { mutableStateOf("Seleccioná una clave") }
            var selectedOption by remember { mutableStateOf("-") }
            var inputText by remember { mutableStateOf(TextFieldValue("")) }
            var encryptMode by remember { mutableStateOf(true) }

            when {
                showBook -> {
                    BackHandler {
                        if (selectedInfoKey != null) {
                            selectedInfoKey = null
                        } else {
                            showBook = false
                        }
                    }
                    EncryptBook(
                        onBack = {
                            if (selectedInfoKey != null) {
                                selectedInfoKey = null
                            } else {
                                showBook = false
                            }
                        },
                        onSelectKey = { key: String?, usarClave: Boolean ->
                            if (usarClave && key != null) {
                                selectedKey = key
                                // Establecer opción por defecto según la clave
                                selectedOption = when (key) {
                                    "Murciélago", "Paquidermo" -> "0"
                                    "Corrida" -> "E"
                                    "Fechada" -> "240510"
                                    "Morse" -> "Normal"
                                    "Corrida intrínseca" -> "Simple"
                                    "Autocorrida" -> "Por palabra"
                                    else -> "-"
                                }
                                showBook = false
                                selectedInfoKey = null
                            } else {
                                selectedInfoKey = key
                            }
                        },
                        selectedKey = selectedInfoKey,
                        onOpenBook = {
                            selectedInfoKey = null
                            showBook = true
                        }
                    )
                }

                showCompass -> {
                    BackHandler {
                        showCompass = false
                    }
                    EncryptoCompass(
                        onBack = { showCompass = false },
                        classifier = classifier,
                        onApplyKey = { keyFromModel, text ->
                            val uiKey = when (keyFromModel.lowercase()) {
                                "araucano" -> "Araucano"
                                "corrida" -> "Corrida"
                                "corridaintrinseca" -> "Corrida intrínseca"
                                "autocorrida" -> "Autocorrida"
                                "dame" -> "Dame tu pico"
                                "idiomax" -> "Idioma X"
                                "karlina" -> "Karlina Betfuse"
                                "morse" -> "Morse"
                                "murcielago" -> "Murciélago"
                                "palefino" -> "Palérinofu"
                                "paquidermo" -> "Paquidermo"
                                "superamigos" -> "Superamigos"
                                "vocalica" -> "Vocalica"
                                else -> keyFromModel
                            }
                            selectedKey = uiKey
                            inputText = TextFieldValue(text)
                            encryptMode = false
                            showCompass = false
                        }
                    )
                }

                else -> {
                    BackHandler(enabled = true) { finish() }

                    EncryptorApp(
                        selectedKey = selectedKey,
                        onChangeKey = { selectedKey = it },
                        onOpenBook = {
                            selectedInfoKey = null
                            showBook = true
                        },
                        onOpenCompass = { showCompass = true },
                        inputText = inputText,
                        onInputTextChange = { inputText = it },
                        keys = keys,
                        encryptMode = encryptMode,
                        onEncryptModeChange = { encryptMode = it }
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        classifier.close()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncryptorApp(
    selectedKey: String,
    onChangeKey: (String) -> Unit,
    onOpenBook: () -> Unit,
    onOpenCompass: () -> Unit,
    inputText: TextFieldValue,
    onInputTextChange: (TextFieldValue) -> Unit,
    keys: List<String>,
    encryptMode: Boolean,
    onEncryptModeChange: (Boolean) -> Unit
) {
    var expandedKey by remember { mutableStateOf(false) }

    var selectedOption by remember { mutableStateOf("-") }
    var expandedOption by remember { mutableStateOf(false) }

    val murcTypes = listOf("0", "1")
    val letters = ('A'..'N').map { it.toString() } + "Ñ" + ('O'..'Z').map { it.toString() }
    val morseTypes = listOf("Normal", "Extendido")
    val corridaIntrinsecaTypes = listOf("Simple", "Compuesta")
    val autocorridaTypes = listOf("Por palabra", "Por inicial")

    val context = LocalContext.current
    val isInPreview = LocalInspectionMode.current
    val recognizer = remember(isInPreview) {
        if (isInPreview) null
        else TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let {
            val image = InputImage.fromBitmap(it, 0)
            recognizer?.process(image)
                ?.addOnSuccessListener { visionText ->
                    onInputTextChange(TextFieldValue(visionText.text))
                }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            try {
                val image = InputImage.fromFilePath(context, uri)
                recognizer?.process(image)
                    ?.addOnSuccessListener { visionText ->
                        onInputTextChange(TextFieldValue(visionText.text))
                    }
                    ?.addOnFailureListener { e -> e.printStackTrace() }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    val bgColor = Color(0xFF121212)
    val surfaceColor = Color(0xFF1E1E1E)
    val accentColor = Color(0xFFBB86FC)
    val primaryText = Color.White
    val secondaryText = Color(0xFFB0B0B0)
    val resultColor = Color(0xFF03DAC6)

    val outputText = remember(inputText.text, selectedKey, selectedOption, encryptMode) {
        if (selectedKey == "Seleccioná una clave") ""
        else if (encryptMode) Encryptor.encrypt(inputText.text, selectedKey, selectedOption)
        else Encryptor.decrypt(inputText.text, selectedKey, selectedOption)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("EncryptIA", color = primaryText) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = surfaceColor)
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(surfaceColor)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onOpenBook,
                    modifier = Modifier.size(64.dp).background(accentColor, CircleShape)
                ) {
                    Image(painter = painterResource(id = R.drawable.ic_book), contentDescription = "Listado de claves", modifier = Modifier.size(48.dp))
                }

                IconButton(
                    onClick = onOpenCompass,
                    modifier = Modifier.size(64.dp).background(accentColor, CircleShape)
                ) {
                    Image(painter = painterResource(id = R.drawable.ic_compass), contentDescription = "EncryptoCompass", modifier = Modifier.size(58.dp))
                }

                IconButton(
                    onClick = { cameraLauncher.launch(null) },
                    modifier = Modifier.size(64.dp).background(accentColor, CircleShape)
                ) {
                    Image(painter = painterResource(id = R.drawable.ic_camera), contentDescription = "Escanear texto (cámara)", modifier = Modifier.size(34.dp))
                }

                IconButton(
                    onClick = { galleryLauncher.launch("image/*") },
                    modifier = Modifier.size(64.dp).background(accentColor, CircleShape)
                ) {
                    Image(painter = painterResource(id = R.drawable.ic_gallery), contentDescription = "Seleccionar desde galería", modifier = Modifier.size(34.dp))
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(bgColor)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // --- Selectores ---
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                ExposedDropdownMenuBox(
                    expanded = expandedKey,
                    onExpandedChange = { expandedKey = !expandedKey },
                    modifier = Modifier.weight(1f)
                ) {
                    TextField(
                        value = selectedKey,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Clave", color = secondaryText) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = surfaceColor,
                            unfocusedContainerColor = surfaceColor,
                            focusedTextColor = primaryText,
                            unfocusedTextColor = primaryText,
                            cursorColor = accentColor
                        ),
                        modifier = Modifier.menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expandedKey,
                        onDismissRequest = { expandedKey = false },
                        modifier = Modifier.background(surfaceColor)
                    ) {
                        keys.forEach { key ->
                            DropdownMenuItem(
                                text = { Text(key, color = primaryText) },
                                onClick = {
                                    onChangeKey(key)
                                    expandedKey = false
                                    onEncryptModeChange(true)
                                    selectedOption = when (key) {
                                        "Murciélago", "Paquidermo" -> "0"
                                        "Corrida" -> "E"
                                        "Fechada" -> "240510"
                                        "Morse" -> "Normal"
                                        "Corrida intrínseca" -> "Simple"
                                        "Autocorrida" -> "Por palabra"
                                        else -> "-"
                                    }
                                }
                            )
                        }
                    }
                }

                val optionList = when (selectedKey) {
                    "Murciélago", "Paquidermo" -> murcTypes
                    "Corrida" -> letters
                    "Morse" -> morseTypes
                    "Corrida intrínseca" -> corridaIntrinsecaTypes
                    "Autocorrida" -> autocorridaTypes
                    "Fechada" -> listOf("240510", "ddmmyy")
                    else -> listOf("-")
                }

                ExposedDropdownMenuBox(
                    expanded = expandedOption,
                    onExpandedChange = { if (optionList.size > 1) expandedOption = !expandedOption },
                    modifier = Modifier.weight(0.4f)
                ) {
                    if (selectedKey == "Fechada") {
                        TextField(
                            value = selectedOption,
                            onValueChange = {
                                if (it.length <= 6) {
                                    selectedOption = it.filter { char -> char.isDigit() }
                                }
                            },
                            label = { Text("ddmmyy") },
                            visualTransformation = DateVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = surfaceColor,
                                unfocusedContainerColor = surfaceColor,
                                focusedTextColor = primaryText,
                                unfocusedTextColor = primaryText,
                                cursorColor = accentColor
                            ),
                        )
                    } else {
                        TextField(
                            value = selectedOption,
                            onValueChange = { selectedOption = it },
                            readOnly = true,
                            enabled = optionList.size > 1,
                            placeholder = { Text("Tipo", color = secondaryText) },
                            colors = TextFieldDefaults.colors(
                                disabledTextColor = Color.Gray,
                                disabledContainerColor = surfaceColor,
                                focusedContainerColor = surfaceColor,
                                unfocusedContainerColor = surfaceColor,
                                focusedTextColor = primaryText,
                                unfocusedTextColor = primaryText,
                                cursorColor = accentColor
                            ),
                            modifier = Modifier.menuAnchor()
                        )

                        if (optionList.size > 1) {
                            ExposedDropdownMenu(
                                expanded = expandedOption,
                                onDismissRequest = { expandedOption = false },
                                modifier = Modifier.background(surfaceColor)
                            ) {
                                optionList.forEach { opt ->
                                    DropdownMenuItem(
                                        text = { Text(opt, color = primaryText) },
                                        onClick = {
                                            selectedOption = opt
                                            expandedOption = false
                                            onEncryptModeChange(true)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Text(
                text = if (encryptMode) "Español" else "Encriptado",
                color = secondaryText,
                fontSize = 14.sp
            )
            TextField(
                value = inputText,
                onValueChange = { onInputTextChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp),
                minLines = 4,
                placeholder = { Text("Texto a procesar", color = Color.Gray) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = surfaceColor,
                    unfocusedContainerColor = surfaceColor,
                    focusedTextColor = primaryText,
                    unfocusedTextColor = primaryText,
                    cursorColor = accentColor
                )
            )

            IconButton(
                onClick = {
                    onInputTextChange(TextFieldValue(outputText))
                    onEncryptModeChange(!encryptMode)
                },
                modifier = Modifier
                    .size(64.dp)
                    .background(accentColor, CircleShape)
                    .align(Alignment.CenterHorizontally)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_arrow),
                    contentDescription = "Intercambiar idioma",
                    modifier = Modifier.size(44.dp)
                )
            }

            Text(
                text = if (encryptMode) "Encriptado" else "Español",
                color = secondaryText,
                fontSize = 14.sp
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, accentColor, RoundedCornerShape(8.dp))
                    .background(surfaceColor)
                    .padding(8.dp)
            ) {
                Text(
                    text = outputText,
                    color = resultColor,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                IconButton(
                    onClick = {
                        val clipboard =
                            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("resultado", outputText)
                        clipboard.setPrimaryClip(clip)
                    },
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_copy),
                        contentDescription = "Copiar",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val keys = listOf(
        "Palérinofu", "Murciélago", "Corrida",
        "Corrida intrínseca", "Autocorrida", "Abecedárica", "Fechada",
        "Paquidermo", "Araucano", "Superamigos", "Vocalica",
        "Idioma X", "Dame tu pico", "Karlina Betfuse", "Morse"
    ).sorted()
    var inputText by remember { mutableStateOf(TextFieldValue("")) }
    var encryptMode by remember { mutableStateOf(true) }
    EncryptorApp(
        inputText = inputText,
        onInputTextChange = { inputText = it },
        keys = keys,
        selectedKey = "Seleccioná una clave",
        onChangeKey = {},
        onOpenBook = {},
        onOpenCompass = {},
        encryptMode = encryptMode,
        onEncryptModeChange = { encryptMode = it }
    )
}
