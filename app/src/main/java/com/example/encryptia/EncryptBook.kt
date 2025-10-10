package com.example.encryptia

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncryptBook(
    onBack: () -> Unit,
    onSelectKey: (String?, Boolean) -> Unit,
    selectedKey: String?,
    onOpenBook: () -> Unit
) {
    val allKeys = listOf(
        "Abecedárica",
        "Araucano",
        "Autocorrida",
        "Corrida",
        "Corrida intrínseca",
        "Dame tu pico",
        "Fechada",
        "Idioma X",
        "Karlina Betfuse",
        "Morse",
        "Murciélago",
        "Palérinofu",
        "Paquidermo",
        "Superamigos",
        "Vocalica"
    ).sorted()

    val keyTags = mapOf(
        "Abecedárica" to listOf("Alfabética"),
        "Araucano" to listOf("Parcial"),
        "Autocorrida" to listOf("Alfabética"),
        "Corrida" to listOf("Alfabética"),
        "Corrida intrínseca" to listOf("Alfabética"),
        "Dame tu pico" to listOf("Parcial"),
        "Fechada" to listOf("Alfabética"),
        "Idioma X" to listOf("Parcial"),
        "Karlina Betfuse" to listOf("Parcial"),
        "Morse" to listOf("Alfabética"),
        "Murciélago" to listOf("Parcial","Numérica"),
        "Palérinofu" to listOf("Parcial"),
        "Paquidermo" to listOf("Parcial","Numérica"),
        "Superamigos" to listOf("Parcial"),
        "Vocalica" to listOf("Parcial", "Numérica")
    )

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Todas") }

    val accentColor = Color(0xFFBB86FC)
    val bgColor = Color(0xFF121212)
    val appBarColor = Color(0xFF1E1E1E)
    val surfaceColor = Color(0xFF1E1E1E)

    val isDetail = selectedKey != null

    // 🔥 BACKHANDER CORREGIDO
    BackHandler(enabled = true) {
        if (selectedKey != null) {
            // Si estamos en detalle de clave, volver a la lista
            onSelectKey(null, false)
        } else {
            // Si estamos en la lista, volver a pantalla principal
            onBack()
        }
    }

    // Filtrado
    val filteredKeys = allKeys.filter { key ->
        val matchesSearch = key.contains(searchQuery, ignoreCase = true)
        val matchesFilter = when (selectedFilter) {
            "Todas" -> true
            else -> {
                val singularTag = if (selectedFilter.endsWith("es")) {
                    selectedFilter.removeSuffix("es")
                } else {
                    selectedFilter.removeSuffix("s")
                }
                keyTags[key]?.contains(singularTag) ?: true
            }
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
                            // 🔥 CORREGIDO: Usar onBack en lugar de onSelectKey
                            IconButton(
                                onClick = { onBack() }
                            ) {
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
                            contentColor = Color.Black,
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
                                    .offset(x = (-12).dp)
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
                    "Abecedárica" -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Título Funcionamiento
                            Text(
                                text = "Funcionamiento",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp)
                            )

                            // Descripción
                            Text(
                                text = "Cada letra se reemplaza por su opuesta simétrica en el alfabeto. Es una inversión completa del alfabeto, por lo tanto la letra que se quiera encriptar se busca primero en el abecedario de arriba y se ve cual es su equivalencia en el de abajo.",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 16.sp,
                                lineHeight = 20.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 24.dp)
                            )

                            // Abecedarios en un contenedor destacado
                            Surface(
                                color = Color.White.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "A B C D E F G H I J K L M N Ñ O P Q R S T U V W X Y Z",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily.Monospace,
                                        letterSpacing = (-4.1).sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Divider(
                                        color = Color.White.copy(alpha = 0.3f),
                                        thickness = 1.dp,
                                        modifier = Modifier
                                            .fillMaxWidth(0.9f)
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = "Z Y X W V U T S R Q P O Ñ N M L K J I H G F E D C B A",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily.Monospace,
                                        letterSpacing = (-4.1).sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(32.dp))

                            // Título Ejemplo
                            Text(
                                text = "Ejemplo",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                            )

                            // Ejemplo en un contenedor especial
                            Surface(
                                color = Color(0xFF4CAF50).copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, Color(0xFF4CAF50)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "TEXTO ORIGINAL",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Text(
                                        text = "LIBRO DE CLAVES",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        text = "TEXTO CODIFICADO",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Text(
                                        text = "ORYIL WV XOZEVH",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                            // Nota adicional
                            Spacer(modifier = Modifier.height(16.dp))

                            Surface(
                                color = Color(0xFFBB86FC).copy(alpha = 0.1f),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, Color(0xFFBB86FC).copy(alpha = 0.3f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                            ) {
                                Text(
                                    text = "💡 Nota: todavia no hay nota, troll",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }
                    "Araucano" -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Título Funcionamiento
                            Text(
                                text = "Funcionamiento",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp)
                            )

                            // Descripción
                            Text(
                                text = "Se basa en intercambiar letras según el siguiente mapeo fijo. Las letras se intercambian con sus pares correspondientes. Las letras que no están en este conjunto no se modifican. La codificación y decodificación son idénticas (simétricas).",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 16.sp,
                                lineHeight = 20.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 24.dp)
                            )

                            // Mapeo en un contenedor destacado CON COLOR DIFERENCIADO CORREGIDO
                            Surface(
                                color = Color.White.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = buildAnnotatedString {
                                            append("A R ")
                                            withStyle(style = SpanStyle(color = Color.White.copy(alpha = 0.4f))) {
                                                append("A")
                                            }
                                            append(" U C ")
                                            withStyle(style = SpanStyle(color = Color.White.copy(alpha = 0.4f))) {
                                                append("A")
                                            }
                                            append(" N O")
                                        },
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily.Monospace,
                                        letterSpacing = 2.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Divider(
                                        color = Color.White.copy(alpha = 0.3f),
                                        thickness = 1.dp,
                                        modifier = Modifier
                                            .fillMaxWidth(0.9f)
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = buildAnnotatedString {
                                            append("O N ")
                                            withStyle(style = SpanStyle(color = Color.White.copy(alpha = 0.4f))) {
                                                append("A")
                                            }
                                            append(" C U ")
                                            withStyle(style = SpanStyle(color = Color.White.copy(alpha = 0.4f))) {
                                                append("A")
                                            }
                                            append(" R A")
                                        },
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily.Monospace,
                                        letterSpacing = 2.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))


                                }
                            }

                            Spacer(modifier = Modifier.height(32.dp))

                            // Título Ejemplo
                            Text(
                                text = "Ejemplo",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                            )

                            // Ejemplo en un contenedor especial
                            Surface(
                                color = Color(0xFF4CAF50).copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, Color(0xFF4CAF50)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "TEXTO ORIGINAL",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Text(
                                        text = "LIBRO DE CLAVES",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        text = "TEXTO CODIFICADO",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Text(
                                        text = "LIBNA DE ULOVES",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                            // Nota adicional
                            Spacer(modifier = Modifier.height(16.dp))

                            Surface(
                                color = Color(0xFFBB86FC).copy(alpha = 0.1f),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, Color(0xFFBB86FC).copy(alpha = 0.3f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                            ) {
                                Text(
                                    text = "💡 Nota: Las letras 'A' que se encuentran en el medio de la palabra Araucano se suelen tachar y no se usan.",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }
                    "Autocorrida" -> {
                        Text(
                            "Clave Autocorrida por inicial",
                            color = Color.White,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            "Aquí va la explicación detallada de la clave Autocorrida por inicial...",
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
                    "Corrida intrínseca" -> {
                        Text(
                            "Clave Corrida intrínseca compuesta",
                            color = Color.White,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            "Aquí va la explicación detallada de la clave Corrida intrínseca compuesta...",
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    "Dame tu pico" -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Título Funcionamiento
                            Text(
                                text = "Funcionamiento",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp)
                            )

                            // Descripción
                            Text(
                                text = "Se basa en intercambiar letras según el siguiente mapeo fijo. Las letras se intercambian con sus pares correspondientes. Las letras que no están en este conjunto no se modifican. La codificación y decodificación son idénticas (simétricas).",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 16.sp,
                                lineHeight = 20.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 24.dp)
                            )

                            // Mapeo en un contenedor destacado
                            Surface(
                                color = Color.White.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = buildAnnotatedString {
                                            append("D A M E   T U   P I C O")
                                        },
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily.Monospace,
                                        letterSpacing = 1.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Divider(
                                        color = Color.White.copy(alpha = 0.3f),
                                        thickness = 1.dp,
                                        modifier = Modifier
                                            .fillMaxWidth(0.9f)
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = buildAnnotatedString {
                                            append("A D E M   U T   I P O C")
                                        },
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily.Monospace,
                                        letterSpacing = 1.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }

                            Spacer(modifier = Modifier.height(32.dp))

                            // Título Ejemplo
                            Text(
                                text = "Ejemplo",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                            )

                            // Ejemplo en un contenedor especial
                            Surface(
                                color = Color(0xFF4CAF50).copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, Color(0xFF4CAF50)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "TEXTO ORIGINAL",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Text(
                                        text = "LIBRO DE CLAVES",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        text = "TEXTO CODIFICADO",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Text(
                                        text = "LPBRC AM OLDVMS",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                            // Nota adicional
                            Spacer(modifier = Modifier.height(16.dp))

                            Surface(
                                color = Color(0xFFBB86FC).copy(alpha = 0.1f),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, Color(0xFFBB86FC).copy(alpha = 0.3f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                            ) {
                                Text(
                                    text = "💡 Nota: Cada letra se intercambia con su par correspondiente según el mapeo fijo de 'DAME TU PICO'.",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }
                    "Fechada" -> {
                        Text(
                            "Clave Fechada",
                            color = Color.White,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            "Aquí va la explicación detallada de la clave Fechada...",
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
                    "Murciélago" -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Título Funcionamiento
                            Text(
                                text = "Funcionamiento",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp)
                            )

                            // Descripción
                            Text(
                                text = "Se reemplazan las letras de la palabra MURCIELAGO por números según su posición. Se escribe la palabra normal y debajo se empareja cada letra con un numero del 0-9. Esta clave tiene dos variantes pero el comportamiento es el mismo. Las letras que no están en MURCIELAGO no se cambian.",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 16.sp,
                                lineHeight = 20.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 24.dp)
                            )

                            // PRIMER DICCIONARIO - Murciélago 0
                            Surface(
                                color = Color.White.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Murciélago 0",
                                        color = accentColor,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        text = "M U R C I E L A G O",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily.Monospace,
                                        letterSpacing = 1.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Divider(
                                        color = Color.White.copy(alpha = 0.3f),
                                        thickness = 1.dp,
                                        modifier = Modifier
                                            .fillMaxWidth(0.9f)
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = "0 1 2 3 4 5 6 7 8 9",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily.Monospace,
                                        letterSpacing = 1.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // SEGUNDO DICCIONARIO - Murciélago 1
                            Surface(
                                color = Color.White.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Murciélago 1",
                                        color = accentColor,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        text = "M U R C I E L A G O",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily.Monospace,
                                        letterSpacing = 1.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Divider(
                                        color = Color.White.copy(alpha = 0.3f),
                                        thickness = 1.dp,
                                        modifier = Modifier
                                            .fillMaxWidth(0.9f)
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = "1 2 3 4 5 6 7 8 9 0",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily.Monospace,
                                        letterSpacing = 1.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(32.dp))

                            // Título Ejemplo
                            Text(
                                text = "Ejemplo",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                            )

                            // PRIMER EJEMPLO - Murciélago 0
                            Surface(
                                color = Color(0xFF4CAF50).copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, Color(0xFF4CAF50)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "MURCIÉLAGO 0",
                                        color = accentColor,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "TEXTO ORIGINAL",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Text(
                                        text = "LIBRO DE CLAVES",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "TEXTO CODIFICADO",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Text(
                                        text = "64B29 D5 367V5S",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // SEGUNDO EJEMPLO - Murciélago 1
                            Surface(
                                color = Color(0xFF4CAF50).copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, Color(0xFF4CAF50)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "MURCIÉLAGO 1",
                                        color = accentColor,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "TEXTO ORIGINAL",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Text(
                                        text = "LIBRO DE CLAVES",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "TEXTO CODIFICADO",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Text(
                                        text = "75B30 D6 478V6S",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                            // Nota adicional
                            Spacer(modifier = Modifier.height(16.dp))

                            Surface(
                                color = Color(0xFFBB86FC).copy(alpha = 0.1f),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, Color(0xFFBB86FC).copy(alpha = 0.3f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                            ) {
                                Text(
                                    text = "💡 Nota: La diferencia entre las variantes es el número con el que empiezan, si el 0 o el 1.",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }
                    "Palérinofu" -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Título Funcionamiento
                            Text(
                                text = "Funcionamiento",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp)
                            )

                            // Descripción
                            Text(
                                text = "Se basa en intercambiar letras según el siguiente mapeo fijo. Las letras se intercambian con sus pares correspondientes. Se escribe PALERINOFU y cada dos letras se hace una linea vertical, debajo se escriben las letras dadas vueltas dentro de cada esapcio formado. Las letras que no están en este conjunto no se modifican. La codificación y decodificación son iguales.",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 16.sp,
                                lineHeight = 20.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 24.dp)
                            )

                            // Mapeo en un contenedor destacado
                            Surface(
                                color = Color.White.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = buildAnnotatedString {
                                            append("P A")
                                            append(" │ ")
                                            append("L E")
                                            append(" │ ")
                                            append("R I")
                                            append(" │ ")
                                            append("N O")
                                            append(" │ ")
                                            append("F U")
                                        },
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily.Monospace,
                                        letterSpacing = 2.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Divider(
                                        color = Color.White.copy(alpha = 0.3f),
                                        thickness = 1.dp,
                                        modifier = Modifier
                                            .fillMaxWidth(0.9f)
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = buildAnnotatedString {
                                            append("A P")
                                            append(" │ ")
                                            append("E L")
                                            append(" │ ")
                                            append("I R")
                                            append(" │ ")
                                            append("O N")
                                            append(" │ ")
                                            append("U F")
                                        },
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily.Monospace,
                                        letterSpacing = 2.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }

                            Spacer(modifier = Modifier.height(32.dp))

                            // Título Ejemplo
                            Text(
                                text = "Ejemplo",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                            )

                            // Ejemplo en un contenedor especial
                            Surface(
                                color = Color(0xFF4CAF50).copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, Color(0xFF4CAF50)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "TEXTO ORIGINAL",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Text(
                                        text = "LIBRO DE CLAVES",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        text = "TEXTO CODIFICADO",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Text(
                                        text = "ERBIN DL CEPVLS",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                            // Nota adicional
                            Spacer(modifier = Modifier.height(16.dp))

                            Surface(
                                color = Color(0xFFBB86FC).copy(alpha = 0.1f),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, Color(0xFFBB86FC).copy(alpha = 0.3f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                            ) {
                                Text(
                                    text = "💡 Nota: Cuando se hace en papel se suelen dividir con lineas verticales para una mejor visualización.",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }
                    "Paquidermo" -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Título Funcionamiento
                            Text(
                                text = "Funcionamiento",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp)
                            )

                            // Descripción
                            Text(
                                text = "Se reemplazan las letras de la palabra PAQUIDERMO por números según su posición. Se escribe la palabra normal y debajo se empareja cada letra con un numero del 0-9. Esta clave tiene dos variantes pero el comportamiento es el mismo. Las letras que no están en PAQUIDERMO no se cambian.",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 16.sp,
                                lineHeight = 20.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 24.dp)
                            )

                            // PRIMER DICCIONARIO - Paquidermo 0
                            Surface(
                                color = Color.White.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Paquidermo 0",
                                        color = accentColor,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        text = "P A Q U I D E R M O",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily.Monospace,
                                        letterSpacing = 1.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Divider(
                                        color = Color.White.copy(alpha = 0.3f),
                                        thickness = 1.dp,
                                        modifier = Modifier
                                            .fillMaxWidth(0.9f)
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = "0 1 2 3 4 5 6 7 8 9",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily.Monospace,
                                        letterSpacing = 1.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // SEGUNDO DICCIONARIO - Paquidermo 1
                            Surface(
                                color = Color.White.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Paquidermo 1",
                                        color = accentColor,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        text = "P A Q U I D E R M O",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily.Monospace,
                                        letterSpacing = 1.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Divider(
                                        color = Color.White.copy(alpha = 0.3f),
                                        thickness = 1.dp,
                                        modifier = Modifier
                                            .fillMaxWidth(0.9f)
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = "1 2 3 4 5 6 7 8 9 0",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily.Monospace,
                                        letterSpacing = 1.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(32.dp))

                            // Título Ejemplo
                            Text(
                                text = "Ejemplo",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                            )

                            // PRIMER EJEMPLO - Paquidermo 0
                            Surface(
                                color = Color(0xFF4CAF50).copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, Color(0xFF4CAF50)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "PAQUIDERMO 0",
                                        color = accentColor,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "TEXTO ORIGINAL",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Text(
                                        text = "LIBRO DE CLAVES",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "TEXTO CODIFICADO",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Text(
                                        text = "L4B79 56 CL1V6S",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // SEGUNDO EJEMPLO - Paquidermo 1
                            Surface(
                                color = Color(0xFF4CAF50).copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, Color(0xFF4CAF50)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "PAQUIDERMO 1",
                                        color = accentColor,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "TEXTO ORIGINAL",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Text(
                                        text = "LIBRO DE CLAVES",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "TEXTO CODIFICADO",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Text(
                                        text = "L5B80 67 CL2V7S",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                            // Nota adicional
                            Spacer(modifier = Modifier.height(16.dp))

                            Surface(
                                color = Color(0xFFBB86FC).copy(alpha = 0.1f),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, Color(0xFFBB86FC).copy(alpha = 0.3f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                            ) {
                                Text(
                                    text = "💡 Nota: La diferencia entre las variantes es el número con el que empiezan, si el 0 o el 1.",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }
                    "Superamigos" -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Título Funcionamiento
                            Text(
                                text = "Funcionamiento",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp)
                            )

                            // Descripción
                            Text(
                                text = "Se basa en la palabra SUPERAMIGOS, donde las letras se intercambian en pares simétricos. Se escribe una vez la palabra normal y debajo su versión invertida. Las demás letras del texto original fuera de este conjunto quedan igual. La codificación y decodificación son el mismo proceso.",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 16.sp,
                                lineHeight = 20.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 24.dp)
                            )

                            // Mapeo en un contenedor destacado
                            Surface(
                                color = Color.White.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "S U P E R A M I G O S",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily.Monospace,
                                        letterSpacing = 1.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Divider(
                                        color = Color.White.copy(alpha = 0.3f),
                                        thickness = 1.dp,
                                        modifier = Modifier
                                            .fillMaxWidth(0.9f)
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = "S O G I M A R E P U S",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily.Monospace,
                                        letterSpacing = 1.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(32.dp))

                            // Título Ejemplo
                            Text(
                                text = "Ejemplo",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                            )

                            // Ejemplo en un contenedor especial
                            Surface(
                                color = Color(0xFF4CAF50).copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, Color(0xFF4CAF50)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "TEXTO ORIGINAL",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Text(
                                        text = "LIBRO DE CLAVES",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        text = "TEXTO CODIFICADO",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Text(
                                        text = "LEBMU DI CLAVIS",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                            // Nota adicional
                            Spacer(modifier = Modifier.height(16.dp))

                            Surface(
                                color = Color(0xFFBB86FC).copy(alpha = 0.1f),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, Color(0xFFBB86FC).copy(alpha = 0.3f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                            ) {
                                Text(
                                    text = "💡 Nota: Las letras S y A al su par se la misma letra no se cambian, así que no hace falta fijarse mucho en ellas.",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }
                    "Vocalica" -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Título Funcionamiento
                            Text(
                                text = "Funcionamiento",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp)
                            )

                            // Descripción
                            Text(
                                text = "Reemplaza cada vocal por un número específico. Las consonantes y otros caracteres permanecen sin cambios. La codificación y decodificación utilizan mapeos inversos.",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 16.sp,
                                lineHeight = 20.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 24.dp)
                            )

                            // Mapeo en un contenedor destacado
                            Surface(
                                color = Color.White.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = buildAnnotatedString {
                                            append("A   E   I   O   U")
                                        },
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily.Monospace,
                                        letterSpacing = 2.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Divider(
                                        color = Color.White.copy(alpha = 0.3f),
                                        thickness = 1.dp,
                                        modifier = Modifier
                                            .fillMaxWidth(0.9f)
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = buildAnnotatedString {
                                            append("1   2   3   4   5")
                                        },
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily.Monospace,
                                        letterSpacing = 2.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }

                            Spacer(modifier = Modifier.height(32.dp))

                            // Título Ejemplo
                            Text(
                                text = "Ejemplo",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                            )

                            // Ejemplo en un contenedor especial
                            Surface(
                                color = Color(0xFF4CAF50).copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, Color(0xFF4CAF50)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "TEXTO ORIGINAL",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Text(
                                        text = "LIBRO DE CLAVES",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        text = "TEXTO CODIFICADO",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Text(
                                        text = "L3BR4 D2 CL1V2S",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                            // Nota adicional
                            Spacer(modifier = Modifier.height(16.dp))

                            Surface(
                                color = Color(0xFFBB86FC).copy(alpha = 0.1f),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, Color(0xFFBB86FC).copy(alpha = 0.3f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                            ) {
                                Text(
                                    text = "💡 Nota: Esta clave es facil de leer a simple vista y usalmente no  necesita hacer en papel",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }

                    else -> {
                        Text(
                            "Clave no encontrada",
                            color = Color.White,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            "La descripción para esta clave aún no ha sido añadida.",
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(70.dp))
            }

            if (showTags) {
                AlertDialog(
                    onDismissRequest = { showTags = false },
                    title = { Text("Etiquetas para " + (selectedKey ?: ""), color = Color.White) },
                    text = {
                        Column {
                            keyTags[selectedKey]?.forEach { tag ->
                                Text(" - $tag", color = Color.LightGray)
                            }
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = { showTags = false },
                            colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                        ) {
                            Text("Cerrar", color = Color.Black)
                        }
                    },
                    containerColor = surfaceColor
                )
            }
        }
    }
}
