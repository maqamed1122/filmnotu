package com.filmnot.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.filmnot.data.model.TmdbMovie
import com.filmnot.ui.common.*
import com.filmnot.ui.theme.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    onMovieClick: (String, Int) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        when {
            uiState.isLoading -> LoadingScreen()
            uiState.error != null -> ErrorScreen(uiState.error!!) { viewModel.loadHome() }
            else -> HomeContent(uiState = uiState, onMovieClick = onMovieClick)
        }
    }
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onMovieClick: (String, Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // Hero Banner - Trending Pager
        if (uiState.trending.isNotEmpty()) {
            item {
                HeroBanner(
                    movies = uiState.trending.take(5),
                    onMovieClick = onMovieClick
                )
            }
        }

        // Now Playing
        if (uiState.nowPlaying.isNotEmpty()) {
            item { SectionTitle("🎬 İndi Kinodan") }
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(uiState.nowPlaying.take(10)) { movie ->
                        MovieCard(
                            movie = movie,
                            onClick = { onMovieClick(movie.type().name, movie.id) }
                        )
                    }
                }
            }
        }

        // Popular Movies
        if (uiState.popularMovies.isNotEmpty()) {
            item { SectionTitle("🔥 Populyar Filmlər") }
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(uiState.popularMovies.take(15)) { movie ->
                        MovieCard(
                            movie = movie,
                            onClick = { onMovieClick("MOVIE", movie.id) }
                        )
                    }
                }
            }
        }

        // Popular TV
        if (uiState.popularTv.isNotEmpty()) {
            item { SectionTitle("📺 Populyar Seriallar") }
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(uiState.popularTv.take(15)) { movie ->
                        MovieCard(
                            movie = movie,
                            onClick = { onMovieClick("TV", movie.id) }
                        )
                    }
                }
            }
        }

        // Top Rated
        if (uiState.topRated.isNotEmpty()) {
            item { SectionTitle("⭐ Ən Yüksək Qiymətləndirilən") }
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(uiState.topRated.take(15)) { movie ->
                        MovieCard(
                            movie = movie,
                            onClick = { onMovieClick("MOVIE", movie.id) }
                        )
                    }
                }
            }
        }

        // Upcoming
        if (uiState.upcoming.isNotEmpty()) {
            item { SectionTitle("📅 Tezliklə") }
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(uiState.upcoming.take(10)) { movie ->
                        MovieCard(
                            movie = movie,
                            onClick = { onMovieClick("MOVIE", movie.id) }
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun HeroBanner(
    movies: List<TmdbMovie>,
    onMovieClick: (String, Int) -> Unit
) {
    val pagerState = rememberPagerState { movies.size }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
    ) {
        HorizontalPager(state = pagerState) { page ->
            val movie = movies[page]
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Background)
            ) {
                AsyncImage(
                    model = movie.backdropUrl() ?: movie.posterUrl(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0x44000000), Background),
                                startY = 100f
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "TREND",
                        color = Primary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 2.sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = movie.displayTitle(),
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = { onMovieClick(movie.type().name, movie.id) },
                        colors = ButtonDefaults.buttonColors(containerColor = Primary),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Ətraflı", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        // Dots indicator
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            movies.indices.forEach { index ->
                Box(
                    modifier = Modifier
                        .size(if (pagerState.currentPage == index) 8.dp else 5.dp)
                        .clip(RoundedCornerShape(50))
                        .background(if (pagerState.currentPage == index) Primary else Color.White.copy(alpha = 0.5f))
                )
            }
        }
    }
}
