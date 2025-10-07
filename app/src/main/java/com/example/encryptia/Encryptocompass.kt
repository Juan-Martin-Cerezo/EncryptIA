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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncryptoCompass(
    onBack: () -> Unit,
    classifier: EncryptionClassifier
) {
    val bgColor = Color(0xFF121212)
    val surfaceColor = Color(0xFF1E1E1E)
    val accentColor = Color(0xFFBB86FC)
    val textColor = Color.White

    var inputText by remember { mutableStateOf("") }
    var results by remember { mutableStateOf<List<Pair<String, Float>>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("EncryptoCompass", color = textColor) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = textColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = surfaceColor)
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
            Text("Explorador de Encriptaciones", color = accentColor, fontSize = 22.sp)

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

            Button(
                onClick = {
                    loading = true
                    results = classifier.predictWithProbabilities(inputText)
                    loading = false
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = accentColor)
            ) {
                Text(if (loading) "Analizando..." else "Detectar método")
            }

            Text("Resultados del modelo:", color = accentColor, fontSize = 18.sp)

            if (results.isEmpty() && !loading) {
                Text("Esperando texto o predicción...", color = textColor)
            } else {
                results.forEachIndexed { index, (method, prob) ->
                    Text(
                        text = "${index + 1}. $method → ${(prob * 100).toInt()}%",
                        color = Color(0xFF03DAC6),
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}
