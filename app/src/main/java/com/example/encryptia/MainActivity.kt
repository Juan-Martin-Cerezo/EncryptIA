package com.example.encryptia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncryptorApp() {
    var inputText by remember { mutableStateOf(TextFieldValue("")) }
    var resultText by remember { mutableStateOf("") }
    var selectedMethod by remember { mutableStateOf("Palérinofu") }

    val methods = listOf("Palérinofu", "Murciélago 0", "Corrida en E")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2C3E50))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Encriptador Completo",
            fontSize = 22.sp,
            color = Color.White
        )

        // Selector de método
        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedMethod,
                onValueChange = {},
                readOnly = true,
                label = { Text("Método") },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                methods.forEach { method ->
                    DropdownMenuItem(
                        text = { Text(method) },
                        onClick = {
                            selectedMethod = method
                            expanded = false
                        }
                    )
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

        // Botones
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    resultText = Encryptor.encrypt(inputText.text, selectedMethod)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF27AE60))
            ) {
                Text("Encriptar", color = Color.White)
            }

            Button(
                onClick = {
                    resultText = Encryptor.decrypt(inputText.text, selectedMethod)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC0392B))
            ) {
                Text("Desencriptar", color = Color.White)
            }
        }

        // Resultado
        Text("Resultado:", color = Color.White)
        Text(
            text = resultText,
            color = Color.Yellow,
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
    }
}