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

    var selectedKey by remember { mutableStateOf("Palérinofu") }
    var expandedKey by remember { mutableStateOf(false) }

    var selectedMurcType by remember { mutableStateOf("0") }
    var expandedType by remember { mutableStateOf(false) }

    var encryptMode by remember { mutableStateOf(true) } // true = encriptar, false = desencriptar

    val keys = listOf(
        "Palérinofu", "Murciélago", "Corrida en E",
        "Paquidermo", "Araucano", "Superamigos", "Vocalica",
        "Idioma X", "Dame tu pico", "Karlina Betfuse"
    )
    val murcTypes = listOf("0", "1")
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
            Text(
                text = "Encriptador Traductor",
                fontSize = 22.sp,
                color = primaryText
            )

            // --- Selectores + Flecha ---
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
                        label = { Text("Clave", color = secondaryText) },
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
                                    if (key != "Murciélago" && key != "Paquidermo") selectedMurcType = "0"
                                }
                            )
                        }
                    }
                }

                // Selector de tipo Murciélago o Paquidermo
                if (selectedKey == "Murciélago" || selectedKey == "Paquidermo") {
                    ExposedDropdownMenuBox(
                        expanded = expandedType,
                        onExpandedChange = { expandedType = !expandedType },
                        modifier = Modifier.weight(0.4f)
                    ) {
                        TextField(
                            value = selectedMurcType,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Tipo", color = secondaryText) },
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
                            expanded = expandedType,
                            onDismissRequest = { expandedType = false }
                        ) {
                            murcTypes.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type) },
                                    onClick = {
                                        selectedMurcType = type
                                        expandedType = false
                                        outputText = ""
                                        encryptMode = true
                                    }
                                )
                            }
                        }
                    }
                }

                // Botón Flecha
                IconButton(
                    onClick = {
                        val fullKey = when (selectedKey) {
                            "Murciélago", "Paquidermo" -> "$selectedKey $selectedMurcType"
                            else -> selectedKey
                        }

                        outputText = if (encryptMode) {
                            Encryptor.encrypt(inputText.text, fullKey)
                        } else {
                            Encryptor.decrypt(inputText.text, fullKey)
                        }
                        encryptMode = !encryptMode
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(accentColor, RoundedCornerShape(8.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_arrow),
                        contentDescription = "Alternar Encriptar/Desencriptar"
                    )
                }
            }

            // Input TextField
            Text("Texto a procesar:", color = primaryText)
            TextField(
                value = inputText,
                onValueChange = {
                    inputText = it
                    outputText = ""
                    encryptMode = true
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = surfaceColor,
                    unfocusedContainerColor = surfaceColor,
                    focusedTextColor = primaryText,
                    unfocusedTextColor = primaryText,
                    cursorColor = accentColor
                )
            )

            // Output TextField
            Text(
                text = if (encryptMode) "Texto Encriptado:" else "Texto Desencriptado:",
                color = primaryText
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

