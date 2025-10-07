package com.example.encryptia

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncryptoCompass(
    onBack: () -> Unit,
    classifier: EncryptionClassifier,
    onApplyKey: (String, String) -> Unit
) {
    val bgColor = Color(0xFF121212)
    val surfaceColor = Color(0xFF1E1E1E)
    val accentColor = Color(0xFFBB86FC)
    val textColor = Color.White

    var inputText by remember { mutableStateOf("") }
    var results by remember { mutableStateOf<List<Pair<String, Float>>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }
    var visibleItems by remember { mutableStateOf(0) }

    LaunchedEffect(results) {
        if (results.isNotEmpty()) {
            visibleItems = 0
            for (i in 1..results.take(3).size) {
                delay(150)
                visibleItems = i
            }
        } else {
            visibleItems = 0
        }
    }

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
                    results = emptyList() // Reset for animation
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
                Column {
                    results.take(3).forEachIndexed { index, (method, prob) ->
                        AnimatedVisibility(
                            visible = index < visibleItems,
                            enter = slideInHorizontally(initialOffsetX = { it / 2 }) + fadeIn(),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${index + 1}. $method",
                                    color = Color(0xFF03DAC6),
                                    fontSize = 16.sp,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = "%.2f%%".format(prob * 100),
                                    color = Color(0xFF03DAC6),
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.End
                                )
                            }
                        }
                    }
                }

                AnimatedVisibility(
                    visible = visibleItems >= results.take(3).size && results.isNotEmpty(),
                    enter = fadeIn(animationSpec = tween(delayMillis = 100))
                ) {
                    if (results.isNotEmpty()) {
                        Column {
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { onApplyKey(results.first().first, inputText) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = accentColor.copy(alpha = 0.8f))
                            ) {
                                Text("Usar ${results.first().first}")
                            }
                        }
                    }
                }
            }
        }
    }
}
