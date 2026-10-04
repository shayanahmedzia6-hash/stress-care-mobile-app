package com.example.ui.overlays

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.viewmodel.StressCareViewModel

@Composable
fun AuthOverlay(
    viewModel: StressCareViewModel,
    onDismiss: () -> Unit
) {
    var isRegisterMode by remember { mutableStateOf(false) }
    val isDarkMode by viewModel.isDarkMode.collectAsState()

    var fullName by remember { mutableStateOf("Alex Morgan") }
    var email by remember { mutableStateOf("alex.morgan@stresscare.ai") }
    var phone by remember { mutableStateOf("+1 (555) 234-5678") }
    var password by remember { mutableStateOf("password123") }
    var confirmPassword by remember { mutableStateOf("password123") }
    var passwordVisible by remember { mutableStateOf(false) }
    var agreeToTerms by remember { mutableStateOf(true) }

    var showForgotDialog by remember { mutableStateOf(false) }
    var resetEmail by remember { mutableStateOf("") }
    var resetSentMessage by remember { mutableStateOf(false) }

    val bgBrush = if (isDarkMode) {
        Brush.verticalGradient(listOf(Color(0xFF0F172A), Color(0xFF0B0F19)))
    } else {
        Brush.verticalGradient(listOf(Color(0xFFEAF3FF), Color(0xFFF5FAFF), Color(0xFFFFFFFF)))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgBrush)
            .testTag("auth_overlay")
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))

                // Top Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "StressCare Logo",
                        modifier = Modifier.height(36.dp),
                        contentScale = ContentScale.Fit
                    )

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("auth_close_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Hero Content: Title + Subtitle + Hero Illustration
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isRegisterMode) "Create\nAccount" else "Welcome\nBack",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 36.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (isRegisterMode)
                                "Register to monitor stress, sleep and migraine risk."
                            else
                                "Sign in to continue monitoring your stress, sleep and migraine insights.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )
                    }

                    Image(
                        painter = painterResource(id = R.drawable.onboarding3),
                        contentDescription = "Illustration",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Auth Form Card (White card with rounded corners matching stresscarepp.netlify.app)
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 4.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        if (isRegisterMode) {
                            OutlinedTextField(
                                value = fullName,
                                onValueChange = { fullName = it },
                                label = { Text("Full Name") },
                                leadingIcon = {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("auth_name_input"),
                                shape = RoundedCornerShape(14.dp)
                            )
                        }

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email Address") },
                            leadingIcon = {
                                Icon(Icons.Default.Email, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("auth_email_input"),
                            shape = RoundedCornerShape(14.dp)
                        )

                        if (isRegisterMode) {
                            OutlinedTextField(
                                value = phone,
                                onValueChange = { phone = it },
                                label = { Text("Phone Number") },
                                leadingIcon = {
                                    Icon(Icons.Default.Phone, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("auth_phone_input"),
                                shape = RoundedCornerShape(14.dp)
                            )
                        }

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = "Toggle password",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            },
                            singleLine = true,
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("auth_password_input"),
                            shape = RoundedCornerShape(14.dp)
                        )

                        if (isRegisterMode) {
                            OutlinedTextField(
                                value = confirmPassword,
                                onValueChange = { confirmPassword = it },
                                label = { Text("Confirm Password") },
                                leadingIcon = {
                                    Icon(Icons.Default.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                },
                                singleLine = true,
                                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("auth_confirm_password_input"),
                                shape = RoundedCornerShape(14.dp)
                            )

                            // Agree to terms
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Checkbox(
                                    checked = agreeToTerms,
                                    onCheckedChange = { agreeToTerms = it },
                                    colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
                                )
                                Text(
                                    text = "I agree to the Terms & Privacy Policy",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            // Forgot password link
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Text(
                                    text = "Forgot Password?",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .clickable { showForgotDialog = true }
                                        .testTag("auth_forgot_password_btn")
                                )
                            }
                        }

                        // Primary Action Button (Royal Blue #2563EB)
                        Button(
                            onClick = onDismiss,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("auth_submit_button"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = Color.White
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                        ) {
                            Text(
                                text = if (isRegisterMode) "Create Account" else "Sign In",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // OR Divider
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outlineVariant)
                            Text(
                                text = "or",
                                modifier = Modifier.padding(horizontal = 12.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outlineVariant)
                        }

                        // Social Sign In: Google
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("auth_google_button"),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                text = "Continue with Google",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // Switch mode footer
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isRegisterMode) "Already have an account? " else "Don't have an account? ",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = if (isRegisterMode) "Sign In" else "Sign Up",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .clickable { isRegisterMode = !isRegisterMode }
                                    .testTag("auth_mode_toggle")
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))
            }
        }

        // Forgot password dialog
        if (showForgotDialog) {
            AlertDialog(
                onDismissRequest = {
                    showForgotDialog = false
                    resetSentMessage = false
                },
                title = { Text("Forgot Password") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Enter your registered email address and we will send you a password reset link.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        OutlinedTextField(
                            value = resetEmail,
                            onValueChange = { resetEmail = it },
                            label = { Text("Email Address") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (resetSentMessage) {
                            Text(
                                text = "Reset link has been sent to your email!",
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { resetSentMessage = true },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Send Link")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showForgotDialog = false
                        resetSentMessage = false
                    }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
