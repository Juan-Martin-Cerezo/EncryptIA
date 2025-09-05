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
    var resultText by remember { mutableStateOf("") }
    var selectedKey by remember { mutableStateOf("Palérinofu") }
    var expandedKey by remember { mutableStateOf(false) }

    var selectedMurcType by remember { mutableStateOf("0") }
    var expandedType by remember { mutableStateOf(false) }

    // TOGGLE: false initially so first press encrypts
    var encryptMode by remember { mutableStateOf(false) }

    val keys = listOf(
        "Palérinofu", "Murciélago", "Corrida en E",
        "Paquidermo 0", "Paquidermo 1",
        "Araucano", "Superamigos", "Vocalica",
        "Idioma X", "Dame tu pico", "Karlina Betfuse"
    )

    val murcTypes = listOf("0", "1")
    val context = LocalContext.current

    // 🎨 Dark palette
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
            ) { /* bottom bar empty */ }
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
                text = "Encriptador Completo",
                fontSize = 22.sp,
                color = primaryText
            )

            // --- Selectors + Button ---
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Key selector
                ExposedDropdownMenuBox(
                    expanded = expandedKey,
                    onExpandedChange = { expandedKey = !expandedKey },
                    modifier = Modifier.weight(if (selectedKey == "Murciélago") 0.6f else 1f)
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
                        modifier = if (selectedKey == "Murciélago") {
                            Modifier.menuAnchor().weight(0.7f)
                        } else {
                            Modifier.menuAnchor().weight(1f)
                        }
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
                                    // reset state on key change
                                    resultText = ""
                                    encryptMode = false
                                    if (key != "Murciélago") selectedMurcType = "0"
                                }
                            )
                        }
                    }
                }

                // Murciélago type selector
                if (selectedKey == "Murciélago") {
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
                                        // reset state on type change
                                        resultText = ""
                                        encryptMode = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Toggle button: press -> flips mode, then applies to CURRENT INPUT
                IconButton(
                    onClick = {
                        val fullKey =
                            if (selectedKey == "Murciélago") "Murciélago $selectedMurcType" else selectedKey

                        // flip mode first so first press = encrypt, second = decrypt
                        val newMode = !encryptMode
                        encryptMode = newMode

                        resultText = if (newMode) {
                            // ENCRYPT current input
                            Encryptor.encrypt(inputText.text, fullKey)
                        } else {
                            // DECRYPT current input
                            Encryptor.decrypt(inputText.text, fullKey)
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(accentColor, RoundedCornerShape(8.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_arrow),
                        contentDescription = "Encriptar/Desencriptar"
                    )
                }
            }

            // Input
            Text("Ingrese el texto:", color = primaryText)
            TextField(
                value = inputText,
                onValueChange = {
                    inputText = it
                    // reset toggle & result when editing input
                    resultText = ""
                    encryptMode = false
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

            // Result
            Text("Resultado:", color = primaryText)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, accentColor, RoundedCornerShape(8.dp))
                    .background(surfaceColor)
                    .padding(8.dp)
            ) {
                Text(
                    text = resultText,
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
                        val clip = ClipData.newPlainText("resultado", resultText)
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
