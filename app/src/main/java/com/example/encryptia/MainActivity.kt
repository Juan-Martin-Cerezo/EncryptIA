package com.example.encryptia

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var showBook by remember { mutableStateOf(false) }
            var showCompass by remember { mutableStateOf(false) }
            var selectedInfoKey by remember { mutableStateOf<String?>(null) }
            var selectedKey by remember { mutableStateOf("Seleccioná una clave") }

            when {
                showBook -> {
                    EncryptBook(
                        onBack = {
                            showBook = false
                            selectedInfoKey = null
                        },
                        onSelectKey = { key: String?, usarClave: Boolean ->
                            if (usarClave && key != null) {
                                selectedKey = key
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
                    EncryptoCompass(onBack = { showCompass = false })
                }

                else -> {
                    EncryptorApp(
                        selectedKey = selectedKey,
                        onChangeKey = { newKey -> selectedKey = newKey },
                        onOpenBook = {
                            selectedInfoKey = null
                            showBook = true
                        },
                        onOpenCompass = { showCompass = true }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncryptorApp(
    selectedKey: String = "Seleccioná una clave",
    onChangeKey: (String) -> Unit = {},
    onOpenBook: () -> Unit = {},
    onOpenCompass: () -> Unit = {}
) {
    var inputText by remember { mutableStateOf(TextFieldValue("")) }
    var expandedKey by remember { mutableStateOf(false) }

    var selectedOption by remember { mutableStateOf("-") }
    var expandedOption by remember { mutableStateOf(false) }

    var encryptMode by remember { mutableStateOf(true) }

    val keys = listOf(
        "Palérinofu", "Murciélago", "Corrida",
        "Paquidermo", "Araucano", "Superamigos", "Vocalica",
        "Idioma X", "Dame tu pico", "Karlina Betfuse", "Morse"
    ).sorted()

    val murcTypes = listOf("0", "1")
    val letters = ('A'..'N').map { it.toString() } + "Ñ" + ('O'..'Z').map { it.toString() }
    val morseTypes = listOf("Normal", "Extendido")

    val context = LocalContext.current
    val recognizer = remember { TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let {
            val image = InputImage.fromBitmap(it, 0)
            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    inputText = TextFieldValue(visionText.text)
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
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(surfaceColor)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 📘 Botón Libro
                IconButton(
                    onClick = onOpenBook,
                    modifier = Modifier
                        .size(64.dp)
                        .background(accentColor, CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_book),
                        contentDescription = "Listado de claves",
                        modifier = Modifier.size(40.dp)
                    )
                }

                // 🧭 Nuevo botón Compass
                IconButton(
                    onClick = onOpenCompass,
                    modifier = Modifier
                        .size(64.dp)
                        .background(accentColor, CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_compass),
                        contentDescription = "EncryptoCompass",
                        modifier = Modifier.size(50.dp)
                    )
                }

                // 📷 Botón Cámara
                IconButton(
                    onClick = { cameraLauncher.launch(null) },
                    modifier = Modifier
                        .size(64.dp)
                        .background(accentColor, CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_camera),
                        contentDescription = "Escanear texto",
                        modifier = Modifier.size(28.dp)
                    )
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
                        onDismissRequest = { expandedKey = false }
                    ) {
                        keys.forEach { key ->
                            DropdownMenuItem(
                                text = { Text(key) },
                                onClick = {
                                    onChangeKey(key)
                                    expandedKey = false
                                    encryptMode = true
                                    selectedOption = when (key) {
                                        "Murciélago", "Paquidermo" -> "0"
                                        "Corrida" -> "E"
                                        "Morse" -> "Normal"
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
                    else -> listOf("-")
                }

                ExposedDropdownMenuBox(
                    expanded = expandedOption,
                    onExpandedChange = { if (optionList.size > 1) expandedOption = !expandedOption },
                    modifier = Modifier.weight(0.4f)
                ) {
                    TextField(
                        value = selectedOption,
                        onValueChange = {},
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
                            onDismissRequest = { expandedOption = false }
                        ) {
                            optionList.forEach { opt ->
                                DropdownMenuItem(
                                    text = { Text(opt) },
                                    onClick = {
                                        selectedOption = opt
                                        expandedOption = false
                                        encryptMode = true
                                    }
                                )
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
                onValueChange = { inputText = it },
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
                    inputText = TextFieldValue(outputText)
                    encryptMode = !encryptMode
                },
                modifier = Modifier
                    .size(64.dp)
                    .background(accentColor, CircleShape)
                    .align(Alignment.CenterHorizontally)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_arrow),
                    contentDescription = "Intercambiar idioma",
                    modifier = Modifier.size(46.dp)
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
