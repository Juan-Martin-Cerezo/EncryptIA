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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    var selectedKey by remember { mutableStateOf("Palérinofu") }  // Antes 'selectedMethod'
    var expandedKey by remember { mutableStateOf(false) }

    var selectedMurcType by remember { mutableStateOf("0") } // Tipo de Murcielago
    var expandedType by remember { mutableStateOf(false) }

    val keys = listOf("Palérinofu", "Murciélago", "Corrida en E")
    val murcTypes = listOf("0", "1")
    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1A252F))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        val fullKey = if (selectedKey == "Murciélago") "Murciélago $selectedMurcType" else selectedKey
                        resultText = Encryptor.encrypt(inputText.text, fullKey)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF27AE60))
                ) {
                    Text("Encriptar", color = Color.White)
                }

                Button(
                    onClick = {
                        val fullKey = if (selectedKey == "Murciélago") "Murciélago $selectedMurcType" else selectedKey
                        resultText = Encryptor.decrypt(inputText.text, fullKey)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC0392B))
                ) {
                    Text("Desencriptar", color = Color.White)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF2C3E50))
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Encriptador Completo",
                fontSize = 22.sp,
                color = Color.White
            )

            // --- Fila de selección de clave y tipo ---
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Selector Clave
                ExposedDropdownMenuBox(
                    expanded = expandedKey,
                    onExpandedChange = { expandedKey = !expandedKey }
                ) {
                    TextField(
                        value = selectedKey,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Clave") },
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
                                    // Reset tipo Murcielago al cambiar clave
                                    if (key != "Murciélago") selectedMurcType = "0"
                                }
                            )
                        }
                    }
                }

                // Selector de tipo Murcielago (solo si la clave es Murcielago)
                if (selectedKey == "Murciélago") {
                    ExposedDropdownMenuBox(
                        expanded = expandedType,
                        onExpandedChange = { expandedType = !expandedType }
                    ) {
                        TextField(
                            value = selectedMurcType,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Tipo") },
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
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Entrada de texto
            Text("Ingrese el texto:", color = Color.White)
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.fillMaxWidth()
            )

            // Resultado
            Text("Resultado:", color = Color.White)

            // Cuadro principal del resultado con botón dentro
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .background(Color(0xFF34495E))
                    .padding(8.dp)
            ) {
                // Texto del resultado
                Text(
                    text = resultText,
                    color = Color.Yellow,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                // Botón de copiar alineado abajo a la derecha
                IconButton(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
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