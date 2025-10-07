package com.example.encryptia

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncryptBook(
    onBack: () -> Unit,
    onSelectKey: (String?, Boolean) -> Unit,
    selectedKey: String?,
    onOpenBook: () -> Unit
) {
    val allKeys = listOf(
        "Palérinofu", "Murciélago", "Corrida",
        "Paquidermo", "Araucano", "Superamigos", "Vocalica",
        "Idioma X", "Dame tu pico", "Karlina Betfuse", "Morse"
    ).sorted()

    // Etiquetas por clave (2 random por cada una)
    val keyTags = mapOf(
        "Palérinofu" to listOf("Numérica", "Alfabética"),
        "Murciélago" to listOf("Parcial", "Numérica"),
        "Corrida" to listOf("Alfabética", "Parcial"),
        "Paquidermo" to listOf("Numérica", "Alfabética"),
        "Araucano" to listOf("Alfabética", "Parcial"),
        "Superamigos" to listOf("Parcial", "Numérica"),
        "Vocalica" to listOf("Alfabética", "Parcial"),
        "Idioma X" to listOf("Numérica", "Parcial"),
        "Dame tu pico" to listOf("Parcial", "Alfabética"),
        "Karlina Betfuse" to listOf("Alfabética", "Numérica"),
        "Morse" to listOf("Numérica", "Parcial")
    )

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Todas") }

    val accentColor = Color(0xFFBB86FC)
    val bgColor = Color(0xFF121212)
    val appBarColor = Color(0xFF1E1E1E)
    val surfaceColor = Color(0xFF1E1E1E)

    val isDetail = selectedKey != null

    // Filtrado
    val filteredKeys = allKeys.filter { key ->
        val matchesSearch = key.contains(searchQuery, ignoreCase = true)
        val matchesFilter = when (selectedFilter) {
            "Todas" -> true
            else -> keyTags[key]?.contains(selectedFilter.removeSuffix("s")) ?: true
        }
        matchesSearch && matchesFilter
    }

    if (!isDetail) {
        // Pantalla de lista
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor)
                .systemBarsPadding(),
            containerColor = bgColor,
            topBar = {
                Column(modifier = Modifier.background(appBarColor)) {
                    TopAppBar(
                        title = { Text("Libro de claves", color = Color.White) },
                        navigationIcon = {
                            IconButton(onClick = onBack) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = appBarColor)
                    )

                    // Barra de búsqueda + filtro
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .background(surfaceColor, RoundedCornerShape(8.dp))
                            .border(
                                1.dp,
                                if (searchQuery.isNotEmpty()) accentColor else Color.Gray,
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = if (searchQuery.isNotEmpty()) accentColor else Color.LightGray,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            cursorBrush = androidx.compose.ui.graphics.SolidColor(accentColor),
                            decorationBox = { innerTextField ->
                                if (searchQuery.isEmpty()) {
                                    Text("Buscar clave...", color = Color.Gray, fontSize = 16.sp)
                                }
                                innerTextField()
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        // Filtro desplegable
                        var expanded by remember { mutableStateOf(false) }
                        Box {
                            Text(
                                selectedFilter,
                                color = Color.White,
                                modifier = Modifier
                                    .clickable { expanded = true }
                                    .background(
                                        Color(0xFF2C2C2C),
                                        RoundedCornerShape(6.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                containerColor = Color(0xFF2C2C2C)
                            ) {
                                listOf("Todas", "Parciales", "Alfabéticas", "Numéricas").forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option, color = Color.White) },
                                        onClick = {
                                            selectedFilter = option
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(bgColor)
                    .verticalScroll(rememberScrollState())
            ) {
                filteredKeys.forEach { key ->
                    Button(
                        onClick = { onSelectKey(key, false) },
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
        // Pantalla de detalle
        var showTags by remember { mutableStateOf(false) }

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor)
                .systemBarsPadding(),
            containerColor = bgColor,
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(selectedKey ?: "", color = Color.White)
                            Image(
                                painter = painterResource(id = R.drawable.ic_tag),
                                contentDescription = "Ver etiquetas",
                                modifier = Modifier
                                    .size(28.dp)
                                    .offset(x = (-12).dp) // Move left by 8dp (negative moves left, positive moves right)
                                    .clickable { showTags = true }
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { onSelectKey(null, false) }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = appBarColor)
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { onSelectKey(selectedKey, true) },
                    containerColor = accentColor,
                    contentColor = Color.Black,
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
                        Text(
                            "Clave Palérinofu",
                            color = Color.White,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            "Aquí va la explicación detallada de la clave Palérinofu...",
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    "Murciélago" -> {
                        Text(
                            "Clave Murciélago",
                            color = Color.White,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            "Aquí va la explicación detallada de la clave Murciélago...",
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    "Corrida" -> {
                        Text(
                            "Clave Corrida",
                            color = Color.White,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            "Aquí va la explicación detallada de la clave Corrida...",
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    "Paquidermo" -> {
                        Text(
                            "Clave Paquidermo",
                            color = Color.White,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            "Aquí va la explicación detallada de la clave Paquidermo...",
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    "Araucano" -> {
                        Text(
                            "Clave Araucano",
                            color = Color.White,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            "Aquí va la explicación detallada de la clave Araucano...",
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    "Superamigos" -> {
                        Text(
                            "Clave Superamigos",
                            color = Color.White,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            "Aquí va la explicación detallada de la clave Superamigos...",
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    "Vocalica" -> {
                        Text(
                            "Clave Vocalica",
                            color = Color.White,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            "Aquí va la explicación detallada de la clave Vocalica...",
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    "Idioma X" -> {
                        Text(
                            "Clave Idioma X",
                            color = Color.White,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            "Aquí va la explicación detallada de la clave Idioma X...",
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    "Dame tu pico" -> {
                        Text(
                            "Clave Dame tu pico",
                            color = Color.White,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            "Aquí va la explicación detallada de la clave Dame tu pico...",
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    "Karlina Betfuse" -> {
                        Text(
                            "Clave Karlina Betfuse",
                            color = Color.White,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            "Aquí va la explicación detallada de la clave Karlina Betfuse...",
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    "Morse" -> {
                        Text(
                            "Clave Morse",
                            color = Color.White,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            "Aquí va la explicación detallada de la clave Morse...",
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            // Diálogo de etiquetas
            if (showTags) {
                AlertDialog(
                    onDismissRequest = { showTags = false },
                    confirmButton = {
                        TextButton(onClick = { showTags = false }) {
                            Text("Cerrar", color = accentColor)
                        }
                    },
                    title = { Text("Etiquetas de ${selectedKey}", color = Color.White) },
                    text = {
                        Column {
                            keyTags[selectedKey]?.forEach { tag ->
                                Text("• $tag", color = Color.LightGray, fontSize = 16.sp)
                            }
                        }
                    },
                    containerColor = Color(0xFF1E1E1E)
                )
            }
        }
    }
}
