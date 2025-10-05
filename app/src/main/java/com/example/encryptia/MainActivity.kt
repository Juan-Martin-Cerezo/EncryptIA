package com.example.encryptia

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EncryptorApp()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEncryptorApp() {
    EncryptorApp()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncryptorApp() {
    var inputText by remember { mutableStateOf(TextFieldValue("")) }
    var outputText by remember { mutableStateOf("") }

    var selectedKey by remember { mutableStateOf("Seleccioná una clave") }
    var expandedKey by remember { mutableStateOf(false) }

    // second selector (generalized): puede contener "0"/"1", letras A-Z o "-" (deshabilitado)
    var selectedOption by remember { mutableStateOf("-") }
    var expandedOption by remember { mutableStateOf(false) }

    var encryptMode by remember { mutableStateOf(true) } // true = encriptar, false = desencriptar

    // keys: ahora incluye "Corrida"
    val keys = listOf(
        "Palérinofu", "Murciélago", "Corrida",
        "Paquidermo", "Araucano", "Superamigos", "Vocalica",
        "Idioma X", "Dame tu pico", "Karlina Betfuse"
    ).sorted()

    val murcTypes = listOf("0", "1")
    val letters = ('A'..'N').map { it.toString() } + "Ñ" + ('O'..'Z').map { it.toString() }
    val context = LocalContext.current

    val bgColor = Color(0xFF121212)
    val surfaceColor = Color(0xFF1E1E1E)
    val accentColor = Color(0xFFBB86FC)
    val primaryText = Color.White
    val secondaryText = Color(0xFFB0B0B0)
    val resultColor = Color(0xFF03DAC6)

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(surfaceColor)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) { }
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
                // Selector Clave
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
                                    selectedKey = key
                                    expandedKey = false
                                    outputText = ""
                                    encryptMode = true
                                    // set default for second selector depending on key
                                    selectedOption = when (key) {
                                        "Murciélago", "Paquidermo" -> "0"
                                        "Corrida" -> "E" // por defecto E
                                        else -> "-"
                                    }
                                }
                            )
                        }
                    }
                }

                // Segundo selector (siempre visible, pero deshabilitado si no hay opciones)
                val optionList = when (selectedKey) {
                    "Murciélago", "Paquidermo" -> murcTypes
                    "Corrida" -> letters
                    else -> listOf("-")
                }

                ExposedDropdownMenuBox(
                    expanded = expandedOption,
                    onExpandedChange = {
                        if (optionList.size > 1) expandedOption = !expandedOption
                    },
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
                                        outputText = ""
                                        encryptMode = true
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Input TextField (sin título, más alto)
            TextField(
                value = inputText,
                onValueChange = {
                    inputText = it
                    outputText = ""
                    encryptMode = true
                },
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


            // Botón circular violeta al medio
            IconButton(
                onClick = {
                    val method = selectedKey
                    val option = selectedOption
                    outputText = if (encryptMode) {
                        Encryptor.encrypt(inputText.text, method, option)
                    } else {
                        Encryptor.decrypt(inputText.text, method, option)
                    }
                    encryptMode = !encryptMode
                },
                modifier = Modifier
                    .size(64.dp)
                    .background(accentColor, CircleShape)
                    .align(Alignment.CenterHorizontally)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_arrow),
                    contentDescription = "Alternar Encriptar/Desencriptar"
                )
            }

            // Output (sin título)
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
