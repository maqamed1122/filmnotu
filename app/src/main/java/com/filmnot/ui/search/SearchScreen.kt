package com.filmnot.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.filmnot.ui.common.LoadingScreen
import com.filmnot.ui.common.MovieCard
import com.filmnot.ui.theme.*

@Composable
fun SearchScreen(
    onMovieClick: (String, Int) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        // Search Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Surface)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            TextField(
                value = uiState.query,
                onValueChange = viewModel::onQueryChange,
                placeholder = {
                    Text("Film və serial axtar...", color = TextSecondary)
                },
                leadingIcon = {
                    Icon(Icons.Filled.Search, contentDescription = null, tint = TextSecondary)
                },
                trailingIcon = {
                    if (uiState.query.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onQueryChange("") }) {
                            Icon(Icons.Filled.Clear, contentDescription = null, tint = TextSecondary)
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = SurfaceVariant,
                    unfocusedContainerColor = SurfaceVariant,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = OnBackground,
                    unfocusedTextColor = OnBackground,
                    cursorColor = Primary
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        when {
            uiState.query.isEmpty() -> {
                SearchPlaceholder()
            }
            uiState.isLoading -> LoadingScreen()
            uiState.isEmpty -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🔍", fontSize = 48.sp)
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = "Nəticə tapılmadı",
                            color = OnBackground,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "\"${uiState.query}\" üçün nəticə yoxdur",
                            color = TextSecondary,
                            fontSize = 14.sp
                        )
                    }
                }
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.results) { movie ->
                        MovieCard(
                            movie = movie,
                            onClick = { onMovieClick(movie.type().name, movie.id) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchPlaceholder() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🎬", fontSize = 64.sp)
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Film axtarın",
                color = OnBackground,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Milionlarla film və serial",
                color = TextSecondary,
                fontSize = 14.sp
            )
        }
    }
}
