package com.example.encryptia

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncryptBook(
    onBack: () -> Unit,
    onSelectKey: (String) -> Unit,
    selectedKey: String?
) {
    val keys = listOf(
        "Palérinofu", "Murciélago", "Corrida",
        "Paquidermo", "Araucano", "Superamigos", "Vocalica",
        "Idioma X", "Dame tu pico", "Karlina Betfuse", "Morse"
    ).sorted()

    val bgColor = Color(0xFF121212)

    if (selectedKey == null) {
        // 📘 Pantalla de listado
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Listado de claves") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(bgColor)
                    .verticalScroll(rememberScrollState())
            ) {
                keys.forEach { key ->
                    Button(
                        onClick = { onSelectKey(key) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(key)
                    }
                }
            }
        }
    } else {
        // 📖 Pantalla de detalle de clave
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(selectedKey) },
                    navigationIcon = {
                        IconButton(onClick = { onSelectKey("") }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(bgColor)
                    .verticalScroll(rememberScrollState())
            ) {
                when (selectedKey) {
                    "Palérinofu" -> {
                        Text(
                            text = "Clave Palérinofu",
                            color = Color.White,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            text = "Aquí va la explicación detallada de la clave Palérinofu...",
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    "Murciélago" -> {
                        Text(
                            text = "Clave Murciélago",
                            color = Color.White,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            text = "Aquí va la explicación detallada de la clave Murciélago...",
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    "Corrida" -> {
                        Text(
                            text = "Clave Corrida",
                            color = Color.White,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            text = "Aquí va la explicación detallada de la clave Corrida...",
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    "Paquidermo" -> {
                        Text(
                            text = "Clave Paquidermo",
                            color = Color.White,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            text = "Aquí va la explicación detallada de la clave Paquidermo...",
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    "Araucano" -> {
                        Text(
                            text = "Clave Araucano",
                            color = Color.White,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            text = "Aquí va la explicación detallada de la clave Araucano...",
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    "Superamigos" -> {
                        Text(
                            text = "Clave Superamigos",
                            color = Color.White,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            text = "Aquí va la explicación detallada de la clave Superamigos...",
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    "Vocalica" -> {
                        Text(
                            text = "Clave Vocalica",
                            color = Color.White,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            text = "Aquí va la explicación detallada de la clave Vocalica...",
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    "Idioma X" -> {
                        Text(
                            text = "Clave Idioma X",
                            color = Color.White,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            text = "Aquí va la explicación detallada de la clave Idioma X...",
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    "Dame tu pico" -> {
                        Text(
                            text = "Clave Dame tu pico",
                            color = Color.White,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            text = "Aquí va la explicación detallada de la clave Dame tu pico...",
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    "Karlina Betfuse" -> {
                        Text(
                            text = "Clave Karlina Betfuse",
                            color = Color.White,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            text = "Aquí va la explicación detallada de la clave Karlina Betfuse...",
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    "Morse" -> {
                        Text(
                            text = "Clave Morse",
                            color = Color.White,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            text = "Aquí va la explicación detallada de la clave Morse...",
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    else -> {
                        Text(
                            text = "Clave desconocida",
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}
