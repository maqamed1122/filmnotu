package com.filmnot.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.filmnot.ui.theme.*

@Composable
fun SettingsScreen(
    onSignOut: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val stats by viewModel.stats.collectAsStateWithLifecycle()
    var showSignOutDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Surface)
                .padding(16.dp)
        ) {
            Text(
                text = "Statistika & Tənzimləmələr",
                color = OnBackground,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            "Statistika",
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard("Ümumi", stats.total.toString(), Icons.Filled.FormatListBulleted, Modifier.weight(1f))
            StatCard("İzlənildi", stats.watched.toString(), Icons.Filled.Done, Modifier.weight(1f))
        }
        Spacer(Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard("Film", stats.movies.toString(), Icons.Filled.Movie, Modifier.weight(1f))
            StatCard("Serial", stats.tvShows.toString(), Icons.Filled.Tv, Modifier.weight(1f))
        }

        Spacer(Modifier.height(24.dp))

        Text(
            "Hesab",
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.AccountCircle, contentDescription = null, tint = Primary, modifier = Modifier.size(32.dp))
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Hesabdan çıx", color = OnBackground, fontWeight = FontWeight.SemiBold)
                        Text("Firebase hesabı", color = TextSecondary, fontSize = 12.sp)
                    }
                }
                IconButton(onClick = { showSignOutDialog = true }) {
                    Icon(Icons.Filled.Logout, contentDescription = "Çıx", tint = Primary)
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            "Haqqında",
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = CardSurface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🎬", fontSize = 32.sp)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("filmnot", color = OnBackground, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                        Text("v1.0.0", color = TextSecondary, fontSize = 13.sp)
                    }
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    "Film və serialları izləyin, qeyd edin, qiymətləndirin. TMDB verilənlər bazası ilə işləyir.",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }

    if (showSignOutDialog) {
        AlertDialog(
            onDismissRequest = { showSignOutDialog = false },
            containerColor = Surface,
            title = { Text("Çıxmaq istəyirsən?", color = OnBackground, fontWeight = FontWeight.Bold) },
            text = { Text("Hesabdan çıxacaqsınız.", color = TextSecondary) },
            confirmButton = {
                Button(
                    onClick = { showSignOutDialog = false; onSignOut() },
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) { Text("Çıx") }
            },
            dismissButton = {
                TextButton(onClick = { showSignOutDialog = false }) {
                    Text("Ləğv et", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
private fun StatCard(label: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = CardSurface)) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = Primary, modifier = Modifier.size(24.dp))
            Spacer(Modifier.height(8.dp))
            Text(text = value, color = OnBackground, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
            Text(text = label, color = TextSecondary, fontSize = 13.sp)
        }
    }
}
