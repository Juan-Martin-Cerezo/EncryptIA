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
    onSelectKey: (String?, Boolean) -> Unit, // String? para permitir null al volver
    selectedKey: String?,                     // clave seleccionada actualmente
    onOpenBook: () -> Unit                    // callback para abrir el libro desde Main
) {
    val keys = listOf(
        "Palérinofu", "Murciélago", "Corrida",
        "Paquidermo", "Araucano", "Superamigos", "Vocalica",
        "Idioma X", "Dame tu pico", "Karlina Betfuse", "Morse"
    ).sorted()

    val accentColor = Color(0xFFBB86FC)
    val bgColor = Color(0xFF121212)
    val appBarColor = Color(0xFF1E1E1E) // fondo sólido para TopAppBar

    // Definir qué pantalla mostrar
    // Si selectedKey es null → lista de claves
    val isDetail = selectedKey != null

    if (!isDetail) {
        // 📘 Pantalla de listado
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor)
                .systemBarsPadding(),
            topBar = {
                TopAppBar(
                    title = { Text("Libro de claves", color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = appBarColor
                    )
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
                        onClick = { onSelectKey(key, false) }, // abrir detalle
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = accentColor,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(key, color = Color.Black)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    } else {
        // 📖 Pantalla de detalle de clave con FloatingActionButton
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor)
                .systemBarsPadding(),
            topBar = {
                TopAppBar(
                    title = { Text(selectedKey, color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = { onSelectKey(null, false) }) { // volver al listado
                            Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = appBarColor
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        onSelectKey(selectedKey, true) // usar clave y volver a Main
                        // no llamamos a onOpenBook() aquí, así la lista se verá la próxima vez
                    },
                    containerColor = accentColor,
                    contentColor = Color.White,
                    modifier = Modifier.width(160.dp)
                ) {
                    Text("Usar clave", fontSize = 14.sp)
                }
            },
            floatingActionButtonPosition = FabPosition.End
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
                        Text("Clave Palérinofu", color = Color.White, fontSize = 22.sp, modifier = Modifier.padding(16.dp))
                        Text("Aquí va la explicación detallada de la clave Palérinofu...", color = Color.White, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                    }
                    "Murciélago" -> {
                        Text("Clave Murciélago", color = Color.White, fontSize = 22.sp, modifier = Modifier.padding(16.dp))
                        Text("Aquí va la explicación detallada de la clave Murciélago...", color = Color.White, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                    }
                    "Corrida" -> {
                        Text("Clave Corrida", color = Color.White, fontSize = 22.sp, modifier = Modifier.padding(16.dp))
                        Text("Aquí va la explicación detallada de la clave Corrida...", color = Color.White, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                    }
                    "Paquidermo" -> {
                        Text("Clave Paquidermo", color = Color.White, fontSize = 22.sp, modifier = Modifier.padding(16.dp))
                        Text("Aquí va la explicación detallada de la clave Paquidermo...", color = Color.White, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                    }
                    "Araucano" -> {
                        Text("Clave Araucano", color = Color.White, fontSize = 22.sp, modifier = Modifier.padding(16.dp))
                        Text("Aquí va la explicación detallada de la clave Araucano...", color = Color.White, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                    }
                    "Superamigos" -> {
                        Text("Clave Superamigos", color = Color.White, fontSize = 22.sp, modifier = Modifier.padding(16.dp))
                        Text("Aquí va la explicación detallada de la clave Superamigos...", color = Color.White, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                    }
                    "Vocalica" -> {
                        Text("Clave Vocalica", color = Color.White, fontSize = 22.sp, modifier = Modifier.padding(16.dp))
                        Text("Aquí va la explicación detallada de la clave Vocalica...", color = Color.White, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                    }
                    "Idioma X" -> {
                        Text("Clave Idioma X", color = Color.White, fontSize = 22.sp, modifier = Modifier.padding(16.dp))
                        Text("Aquí va la explicación detallada de la clave Idioma X...", color = Color.White, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                    }
                    "Dame tu pico" -> {
                        Text("Clave Dame tu pico", color = Color.White, fontSize = 22.sp, modifier = Modifier.padding(16.dp))
                        Text("Aquí va la explicación detallada de la clave Dame tu pico...", color = Color.White, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                    }
                    "Karlina Betfuse" -> {
                        Text("Clave Karlina Betfuse", color = Color.White, fontSize = 22.sp, modifier = Modifier.padding(16.dp))
                        Text("Aquí va la explicación detallada de la clave Karlina Betfuse...", color = Color.White, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                    }
                    "Morse" -> {
                        Text("Clave Morse", color = Color.White, fontSize = 22.sp, modifier = Modifier.padding(16.dp))
                        Text("Aquí va la explicación detallada de la clave Morse...", color = Color.White, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
