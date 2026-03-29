package com.filmnot.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.filmnot.data.model.MediaType
import com.filmnot.data.model.TmdbMovie
import com.filmnot.data.db.entity.WatchlistEntity
import com.filmnot.ui.theme.*

@Composable
fun MovieCard(
    movie: TmdbMovie,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(130.dp)
            .height(200.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .background(CardSurface)
    ) {
        AsyncImage(
            model = movie.posterUrl(),
            contentDescription = movie.displayTitle(),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // Gradient overlay at bottom
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.45f)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color(0xDD000000))
                    )
                )
        )
        // Rating badge
        Box(
            modifier = Modifier
                .padding(6.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xCC000000))
                .padding(horizontal = 5.dp, vertical = 2.dp)
                .align(Alignment.TopEnd)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                Icon(Icons.Filled.Star, contentDescription = null, tint = StarYellow, modifier = Modifier.size(10.dp))
                Text(
                    text = String.format("%.1f", movie.voteAverage),
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        // Title at bottom
        Text(
            text = movie.displayTitle(),
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(6.dp)
        )
    }
}

@Composable
fun WatchlistMovieCard(
    item: WatchlistEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(120.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
                    .background(SurfaceVariant)
            ) {
                AsyncImage(
                    model = item.posterUrl(),
                    contentDescription = item.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                if (item.isWatched) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0x66000000))
                    )
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = GreenWatched,
                        modifier = Modifier.align(Alignment.Center).size(24.dp)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (item.type() == MediaType.TV) Icons.Filled.Tv else Icons.Filled.Movie,
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = if (item.type() == MediaType.TV) "TV" else "Movie",
                        color = Primary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text = item.title,
                    color = OnBackground,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                if (item.releaseDate?.isNotEmpty() == true) {
                    Text(
                        text = item.releaseDate.take(4),
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Star, contentDescription = null, tint = StarYellow, modifier = Modifier.size(12.dp))
                    Spacer(Modifier.width(3.dp))
                    Text(
                        text = String.format("%.1f", item.voteAverage),
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                    if (item.userRating != null) {
                        Spacer(Modifier.width(8.dp))
                        Text("·", color = TextSecondary, fontSize = 12.sp)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Your: ${item.userRating}★",
                            color = StarYellow,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        color = OnBackground,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = Primary)
    }
}

@Composable
fun ErrorScreen(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Xəta baş verdi", color = OnBackground, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(text = message, color = TextSecondary, fontSize = 14.sp)
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = Primary)
        ) {
            Text("Yenidən cəhd et")
        }
    }
}
