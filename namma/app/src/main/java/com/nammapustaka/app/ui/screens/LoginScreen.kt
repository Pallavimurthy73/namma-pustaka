package com.nammapustaka.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nammapustaka.app.repository.AuthRepository
import com.nammapustaka.app.ui.components.SectionCard
import com.nammapustaka.app.ui.theme.Ink
import com.nammapustaka.app.ui.theme.InkSoft
import com.nammapustaka.app.ui.theme.Saffron
import com.nammapustaka.app.ui.theme.SaffronDark
import com.nammapustaka.app.viewmodel.LibraryViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LibraryViewModel,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var selectedRole by rememberSaveable { mutableStateOf("Student") }
    var studentName by rememberSaveable { mutableStateOf("") }
    var studentRoll by rememberSaveable { mutableStateOf("") }
    var studentPin by rememberSaveable { mutableStateOf("") }
    var teacherUsername by rememberSaveable { mutableStateOf(AuthRepository.teacherUsername) }
    var teacherPassword by rememberSaveable { mutableStateOf(AuthRepository.teacherPassword) }
    var showRegistrationSheet by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.Transparent,
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(listOf(Saffron, SaffronDark)),
                )
                .padding(paddingValues)
                .safeDrawingPadding(),
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = "\uD83D\uDCDA", fontSize = 56.sp)
                Text(
                    text = "Namma Pustaka",
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Black,
                )
                Text(
                    text = "ನಮ್ಮ ಪುಸ್ತಕ · Smart Library for Rural Schools",
                    color = Color(0xFFFDE7D8),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 6.dp),
                )

                SectionCard(
                    modifier = Modifier.padding(top = 28.dp),
                    padding = PaddingValues(22.dp),
                ) {
                    Text(
                        text = "Library Login",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Ink,
                    )

                    Text(
                        text = "Choose your role to continue.",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        textAlign = TextAlign.Center,
                        color = InkSoft,
                        fontSize = 13.sp,
                    )

                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 18.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        listOf("Student", "Teacher").forEach { role ->
                            FilterChip(
                                selected = selectedRole == role,
                                onClick = { selectedRole = role },
                                label = { Text(role) },
                            )
                        }
                    }

                    if (selectedRole == "Student") {
                        TextField(
                            value = studentName,
                            onValueChange = { studentName = it },
                            label = { Text("Student name") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 14.dp),
                            singleLine = true,
                        )
                        TextField(
                            value = studentRoll,
                            onValueChange = { studentRoll = it },
                            label = { Text("Roll number") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        )
                        TextField(
                            value = studentPin,
                            onValueChange = { studentPin = it },
                            label = { Text("PIN") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            visualTransformation = PasswordVisualTransformation(),
                        )

                        ElevatedButton(
                            onClick = {
                                scope.launch {
                                    val message = viewModel.loginStudent(studentName, studentRoll, studentPin)
                                    if (message != null) snackbarHostState.showSnackbar(message)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 18.dp),
                        ) {
                            Text("Login to Library")
                        }

                        OutlinedButton(
                            onClick = { showRegistrationSheet = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                        ) {
                            Text("New student? Register")
                        }

                        Text(
                            text = "Demo student PINs are preloaded too: Priya(12/1234), Arjun(04/2222), Meena(22/3333), Raju(33/4444).",
                            modifier = Modifier.padding(top = 10.dp),
                            color = InkSoft,
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                        )
                    } else {
                        TextField(
                            value = teacherUsername,
                            onValueChange = { teacherUsername = it },
                            label = { Text("Username") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 14.dp),
                            singleLine = true,
                        )
                        TextField(
                            value = teacherPassword,
                            onValueChange = { teacherPassword = it },
                            label = { Text("Password") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                        )
                        ElevatedButton(
                            onClick = {
                                scope.launch {
                                    val message = viewModel.loginTeacher(teacherUsername, teacherPassword)
                                    if (message != null) snackbarHostState.showSnackbar(message)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 18.dp),
                        ) {
                            Text("Open Teacher Dashboard")
                        }
                        Text(
                            text = "Default teacher login: admin / admin123",
                            modifier = Modifier.padding(top = 10.dp),
                            color = InkSoft,
                            fontSize = 12.sp,
                        )
                    }
                }
            }
        }
    }

    if (showRegistrationSheet) {
        RegistrationSheet(
            onDismiss = { showRegistrationSheet = false },
            onRegister = { name, roll, pin ->
                scope.launch {
                    val result = viewModel.registerStudent(name, roll, pin)
                    if (result == null) {
                        showRegistrationSheet = false
                    } else {
                        snackbarHostState.showSnackbar(result)
                    }
                }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegistrationSheet(
    onDismiss: () -> Unit,
    onRegister: (String, String, String) -> Unit,
) {
    var name by rememberSaveable { mutableStateOf("") }
    var roll by rememberSaveable { mutableStateOf("") }
    var pin by rememberSaveable { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = Modifier.imePadding(),
    ) {
        SectionCard(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Text(
                text = "Student Registration",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
            )
            Text(
                text = "ಹೆಸರು, ರೋಲ್ ನಂಬರ್ ಮತ್ತು 4 ಅಂಕಿಯ PIN ನೀಡಿ.",
                color = InkSoft,
                modifier = Modifier.padding(top = 6.dp),
            )
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Student name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp),
                singleLine = true,
            )
            TextField(
                value = roll,
                onValueChange = { roll = it },
                label = { Text("Roll number") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            TextField(
                value = pin,
                onValueChange = { pin = it },
                label = { Text("4-digit PIN") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                visualTransformation = PasswordVisualTransformation(),
            )
            ElevatedButton(
                onClick = { onRegister(name, roll, pin) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp, bottom = 24.dp),
            ) {
                Text("Register and Continue")
            }
        }
    }
}
