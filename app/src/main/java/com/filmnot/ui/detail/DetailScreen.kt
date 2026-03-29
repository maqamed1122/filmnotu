package com.filmnot.ui.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.filmnot.ui.common.LoadingScreen
import com.filmnot.ui.common.ErrorScreen
import com.filmnot.ui.common.MovieCard
import com.filmnot.ui.theme.*
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun DetailScreen(
    onBack: () -> Unit,
    onMovieClick: (String, Int) -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var showTrailer by remember { mutableStateOf(false) }
    var showRatingDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().background(Background)) {
        when {
            uiState.isLoading -> LoadingScreen()
            uiState.error != null -> ErrorScreen(uiState.error!!) { }
            uiState.detail != null -> {
                val detail = uiState.detail!!
                Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                    // Backdrop + Back button
                    Box(modifier = Modifier.fillMaxWidth().height(240.dp)) {
                        AsyncImage(
                            model = detail.backdropUrl() ?: detail.posterUrl(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier.fillMaxSize().background(
                                Brush.verticalGradient(
                                    colors = listOf(Color(0x55000000), Background)
                                )
                            )
                        )
                        // Back button
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier
                                .padding(12.dp)
                                .clip(CircleShape)
                                .background(Color(0x88000000))
                                .align(Alignment.TopStart)
                        ) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Geri", tint = Color.White)
                        }
                        // Play trailer button
                        if (detail.trailerKey() != null) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(Primary.copy(alpha = 0.9f))
                                    .clickable { showTrailer = true },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.PlayArrow, contentDescription = "Fragman", tint = Color.White, modifier = Modifier.size(36.dp))
                            }
                        }
                    }

                    // Trailer Player
                    if (showTrailer && detail.trailerKey() != null) {
                        TrailerPlayer(
                            videoKey = detail.trailerKey()!!,
                            onClose = { showTrailer = false }
                        )
                    }

                    // Content
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Title
                        Text(
                            text = detail.displayTitle(),
                            color = OnBackground,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            lineHeight = 30.sp
                        )
                        Spacer(Modifier.height(6.dp))

                        // Tagline
                        if (!detail.tagline.isNullOrBlank()) {
                            Text(
                                text = "\"${detail.tagline}\"",
                                color = TextSecondary,
                                fontSize = 13.sp,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )
                            Spacer(Modifier.height(8.dp))
                        }

                        // Meta row
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Rating
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Star, contentDescription = null, tint = StarYellow, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = String.format("%.1f", detail.voteAverage),
                                    color = OnBackground,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = " (${detail.voteCount})",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                            // Year
                            if (detail.displayDate().isNotEmpty()) {
                                Text(
                                    text = detail.displayDate().take(4),
                                    color = TextSecondary,
                                    fontSize = 13.sp
                                )
                            }
                            // Runtime
                            val runtime = detail.runtime ?: detail.episodeRunTime?.firstOrNull()
                            if (runtime != null && runtime > 0) {
                                Text(
                                    text = "${runtime}dəq",
                                    color = TextSecondary,
                                    fontSize = 13.sp
                                )
                            }
                            // TV seasons
                            if (detail.numberOfSeasons != null) {
                                Text(
                                    text = "${detail.numberOfSeasons} mövsüm",
                                    color = TextSecondary,
                                    fontSize = 13.sp
                                )
                            }
                        }
                        Spacer(Modifier.height(12.dp))

                        // Genres
                        if (!detail.genres.isNullOrEmpty()) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.horizontalScroll(rememberScrollState())
                            ) {
                                detail.genres.forEach { genre ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(20.dp))
                                            .background(SurfaceVariant)
                                            .padding(horizontal = 12.dp, vertical = 5.dp)
                                    ) {
                                        Text(text = genre.name, color = OnSurface, fontSize = 12.sp)
                                    }
                                }
                            }
                            Spacer(Modifier.height(12.dp))
                        }

                        // Action Buttons
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            // Watchlist button
                            Button(
                                onClick = viewModel::toggleWatchlist,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (uiState.isInWatchlist) SurfaceVariant else Primary
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = if (uiState.isInWatchlist) Icons.Filled.Done else Icons.Filled.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(if (uiState.isInWatchlist) "Listdə" else "Listə əlavə et", fontSize = 13.sp)
                            }
                            // Watched button
                            if (uiState.isInWatchlist) {
                                IconButton(
                                    onClick = viewModel::toggleWatched,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (uiState.isWatched) GreenWatched else SurfaceVariant)
                                        .size(48.dp)
                                ) {
                                    Icon(
                                        Icons.Filled.Done,
                                        contentDescription = "İzlənildi",
                                        tint = Color.White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                // Favorite
                                IconButton(
                                    onClick = viewModel::toggleFavorite,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(SurfaceVariant)
                                        .size(48.dp)
                                ) {
                                    Icon(
                                        if (uiState.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                        contentDescription = "Sevimli",
                                        tint = if (uiState.isFavorite) Primary else TextSecondary,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                // Rating
                                IconButton(
                                    onClick = { showRatingDialog = true },
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(SurfaceVariant)
                                        .size(48.dp)
                                ) {
                                    Icon(
                                        Icons.Filled.Star,
                                        contentDescription = "Qiymətləndir",
                                        tint = if (uiState.userRating != null) StarYellow else TextSecondary,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.height(12.dp))

                        // IMDB Button
                        val imdbUrl = detail.imdbUrl()
                        if (imdbUrl != null) {
                            OutlinedButton(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(imdbUrl))
                                    context.startActivity(intent)
                                },
                                border = ButtonDefaults.outlinedButtonBorder.copy(
                                    brush = Brush.horizontalGradient(listOf(Color(0xFFF5C518), Color(0xFFF5C518)))
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "⭐ IMDb-də Bax",
                                    color = Color(0xFFF5C518),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                            Spacer(Modifier.height(12.dp))
                        }

                        // Overview
                        if (!detail.overview.isNullOrBlank()) {
                            Text("Haqqında", color = OnBackground, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(6.dp))
                            Text(detail.overview, color = TextSecondary, fontSize = 14.sp, lineHeight = 22.sp)
                            Spacer(Modifier.height(16.dp))
                        }

                        // Cast
                        if (!detail.credits?.cast.isNullOrEmpty()) {
                            Text("Aktyor heyəti", color = OnBackground, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                items(detail.credits!!.cast.take(10)) { cast ->
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.width(80.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(70.dp)
                                                .clip(CircleShape)
                                                .background(SurfaceVariant)
                                        ) {
                                            AsyncImage(
                                                model = cast.profileUrl(),
                                                contentDescription = cast.name,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                        }
                                        Spacer(Modifier.height(4.dp))
                                        Text(
                                            text = cast.name,
                                            color = OnSurface,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis,
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                        )
                                        Text(
                                            text = cast.character,
                                            color = TextSecondary,
                                            fontSize = 10.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                        )
                                    }
                                }
                            }
                            Spacer(Modifier.height(16.dp))
                        }

                        // Similar
                        if (!detail.similar?.results.isNullOrEmpty()) {
                            Text("Oxşar", color = OnBackground, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                items(detail.similar!!.results.take(10)) { movie ->
                                    MovieCard(
                                        movie = movie,
                                        onClick = { onMovieClick(uiState.mediaType.name, movie.id) }
                                    )
                                }
                            }
                            Spacer(Modifier.height(24.dp))
                        }
                    }
                }
            }
        }
    }

    // Rating Dialog
    if (showRatingDialog) {
        RatingDialog(
            currentRating = uiState.userRating,
            onRatingSelected = { rating ->
                viewModel.setRating(rating)
                showRatingDialog = false
            },
            onDismiss = { showRatingDialog = false }
        )
    }
}

@Composable
private fun TrailerPlayer(videoKey: String, onClose: () -> Unit) {
    val context = LocalContext.current
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Fragman", color = OnBackground, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            IconButton(onClick = onClose) {
                Icon(Icons.Filled.Close, contentDescription = "Bağla", tint = TextSecondary)
            }
        }
        AndroidView(
            factory = { ctx ->
                YouTubePlayerView(ctx).apply {
                    addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            youTubePlayer.loadVideo(videoKey, 0f)
                        }
                    })
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(Color.Black)
        )
    }
}

@Composable
private fun RatingDialog(
    currentRating: Float?,
    onRatingSelected: (Float) -> Unit,
    onDismiss: () -> Unit
) {
    var selected by remember { mutableStateOf(currentRating ?: 0f) }
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Surface,
        title = { Text("Qiymətləndir", color = OnBackground, fontWeight = FontWeight.Bold) },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (selected > 0) "${selected.toInt()} / 10" else "Qiymət seçin",
                    color = StarYellow,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(12.dp))
                Slider(
                    value = selected,
                    onValueChange = { selected = it },
                    valueRange = 1f..10f,
                    steps = 8,
                    colors = SliderDefaults.colors(
                        thumbColor = StarYellow,
                        activeTrackColor = StarYellow
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("1", color = TextSecondary, fontSize = 12.sp)
                    Text("10", color = TextSecondary, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onRatingSelected(selected) },
                colors = ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                Text("Təsdiqlə")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Ləğv et", color = TextSecondary)
            }
        }
    )
}
