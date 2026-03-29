package com.filmnot.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.filmnot.ui.theme.*

@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val authState by viewModel.authState.collectAsStateWithLifecycle()

    LaunchedEffect(authState) {
        if (authState != null) onAuthSuccess()
    }

    var isLogin by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1A0000), Background)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            Text("🎬", fontSize = 64.sp)
            Spacer(Modifier.height(8.dp))
            Text(
                text = "filmnot",
                color = Primary,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp
            )
            Text(
                text = "Film dünyanda yerini tap",
                color = TextSecondary,
                fontSize = 14.sp
            )
            Spacer(Modifier.height(40.dp))

            // Tab - Login / Register
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceVariant, RoundedCornerShape(12.dp))
                    .padding(4.dp)
            ) {
                listOf("Daxil ol", "Qeydiyyat").forEachIndexed { index, label ->
                    val selected = if (index == 0) isLogin else !isLogin
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                if (selected) Primary else Color.Transparent,
                                RoundedCornerShape(10.dp)
                            )
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        TextButton(onClick = { isLogin = index == 0 }) {
                            Text(
                                text = label,
                                color = if (selected) Color.White else TextSecondary,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(24.dp))

            // Email field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-poçt") },
                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = SurfaceVariant,
                    focusedTextColor = OnBackground,
                    unfocusedTextColor = OnBackground,
                    focusedLabelColor = Primary,
                    unfocusedLabelColor = TextSecondary,
                    focusedLeadingIconColor = Primary,
                    unfocusedLeadingIconColor = TextSecondary,
                    focusedContainerColor = Surface,
                    unfocusedContainerColor = Surface
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            // Password field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Şifrə") },
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = null,
                            tint = TextSecondary
                        )
                    }
                },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = SurfaceVariant,
                    focusedTextColor = OnBackground,
                    unfocusedTextColor = OnBackground,
                    focusedLabelColor = Primary,
                    unfocusedLabelColor = TextSecondary,
                    focusedLeadingIconColor = Primary,
                    unfocusedLeadingIconColor = TextSecondary,
                    focusedContainerColor = Surface,
                    unfocusedContainerColor = Surface
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            // Error
            AnimatedVisibility(visible = uiState.error != null) {
                Text(
                    text = uiState.error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(Modifier.height(20.dp))

            // Submit button
            Button(
                onClick = {
                    if (isLogin) viewModel.signIn(email, password)
                    else viewModel.register(email, password)
                },
                enabled = email.isNotBlank() && password.length >= 6 && !uiState.isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
                } else {
                    Text(
                        text = if (isLogin) "Daxil ol" else "Qeydiyyatdan keç",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
