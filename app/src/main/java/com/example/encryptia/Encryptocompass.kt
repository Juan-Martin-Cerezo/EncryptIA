package com.example.encryptia

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncryptoCompass(
    onBack: () -> Unit
) {
    val bgColor = Color(0xFF121212)
    val surfaceColor = Color(0xFF1E1E1E)
    val accentColor = Color(0xFFBB86FC)
    val textColor = Color.White

    var inputText by remember { mutableStateOf("") }
    val outputText = "Aquí se mostrará el resultado generado por la IA."

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("EncryptoCompass", color = textColor) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = textColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = surfaceColor
                )
            )
        },
        modifier = Modifier.background(bgColor)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- Encabezado ---
            Text(
                "Explorador de Encriptaciones",
                color = accentColor,
                fontSize = 22.sp
            )
            Text(
                "Esta herramienta usará nuestro modelo de IA para analizar y transformar texto.",
                color = textColor,
                fontSize = 16.sp
            )
            Text(
                "Podrás escribir cualquier texto, y la IA devolverá su interpretación o encriptado correspondiente.",
                color = textColor,
                fontSize = 16.sp
            )
            Text(
                "Por ahora, solo estamos desarrollando la interfaz visual.",
                color = textColor,
                fontSize = 16.sp
            )

            // --- Input ---
            Text(
                "Texto de entrada:",
                color = accentColor,
                fontSize = 18.sp
            )
            BasicTextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp)
                    .background(surfaceColor, shape = MaterialTheme.shapes.medium)
                    .padding(12.dp),
                textStyle = TextStyle(color = textColor, fontSize = 16.sp),
                decorationBox = { innerTextField ->
                    if (inputText.isEmpty()) {
                        Text("Escribí tu texto aquí...", color = Color.Gray, fontSize = 16.sp)
                    }
                    innerTextField()
                }
            )

            // --- Output ---
            Text(
                "Salida:",
                color = accentColor,
                fontSize = 18.sp
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(surfaceColor, shape = MaterialTheme.shapes.medium)
                    .padding(12.dp)
            ) {
                Text(outputText, color = Color(0xFF03DAC6), fontSize = 16.sp)
            }
        }
    }
}
