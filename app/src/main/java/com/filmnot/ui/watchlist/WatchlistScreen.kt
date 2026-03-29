package com.filmnot.ui.watchlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.filmnot.ui.common.WatchlistMovieCard
import com.filmnot.ui.watchlist.WatchlistFilter
import com.filmnot.ui.theme.*

@Composable
fun WatchlistScreen(
    onMovieClick: (String, Int) -> Unit,
    viewModel: WatchlistViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Surface)
                .padding(16.dp)
        ) {
            Text(
                text = "İzləmə Siyahısı",
                color = OnBackground,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        // Filter tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Surface)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            WatchlistFilter.values().forEach { filter ->
                FilterChip(
                    selected = uiState.filter == filter,
                    onClick = { viewModel.setFilter(filter) },
                    label = {
                        Text(
                            text = when (filter) {
                                WatchlistFilter.ALL -> "Hamısı"
                                WatchlistFilter.UNWATCHED -> "Gözləyir"
                                WatchlistFilter.WATCHED -> "İzlənildi"
                                WatchlistFilter.FAVORITES -> "Sevimlilər"
                            },
                            fontSize = 12.sp
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Primary,
                        selectedLabelColor = OnPrimary,
                        containerColor = SurfaceVariant,
                        labelColor = TextSecondary
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = uiState.filter == filter,
                        borderColor = SurfaceVariant,
                        selectedBorderColor = Primary
                    )
                )
            }
        }

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Primary)
            }
        } else if (uiState.items.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🎬", fontSize = 56.sp)
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "Siyahı boşdur",
                        color = OnBackground,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Film və serialları siyahıya əlavə edin",
                        color = TextSecondary,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.items, key = { it.id }) { item ->
                    WatchlistMovieCard(
                        item = item,
                        onClick = { onMovieClick(item.mediaType, item.id) }
                    )
                }
            }
        }
    }
}
