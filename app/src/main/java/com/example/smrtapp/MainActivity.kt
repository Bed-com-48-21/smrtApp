package com.example.smrtapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.smrtapp.ui.theme.SmrtAppTheme
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmrtAppTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Scaffold(
                    topBar = {
                        if (currentRoute != "welcome" && currentRoute != "signup" && currentRoute != "login" && currentRoute != "forgot_password" && currentRoute != "thanks") {
                            TopAppBar(
                                title = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Image(
                                            painter = painterResource(id = R.drawable.logo3),
                                            contentDescription = "Logo",
                                            modifier = Modifier.size(40.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("SmrtApp", color = Color.White, fontSize = 20.sp)
                                    }
                                },
                                navigationIcon = {
                                    if (navController.previousBackStackEntry != null) {
                                        IconButton(onClick = { navController.navigateUp() }) {
                                            Icon(
                                                imageVector = Icons.Filled.ArrowBack,
                                                contentDescription = "Back",
                                                tint = Color.White
                                            )
                                        }
                                    }
                                },
                                actions = {
                                    var menuExpanded by remember { mutableStateOf(false) }

                                    TextButton(onClick = { navController.navigate("landing") }) {
                                        Text("Home", color = Color.White)
                                    }
                                    TextButton(onClick = { navController.navigate("categories") }) {
                                        Text("Categories", color = Color.White)
                                    }
                                    IconButton(onClick = { menuExpanded = true }) {
                                        Icon(Icons.Default.MoreVert, contentDescription = "More options", tint = Color.White)
                                    }
                                    DropdownMenu(
                                        expanded = menuExpanded,
                                        onDismissRequest = { menuExpanded = false }
                                    ) {
                                        DropdownMenuItem(text = { Text("Profile") }, onClick = { navController.navigate("profile"); menuExpanded = false })
                                        DropdownMenuItem(text = { Text("Tips") }, onClick = { navController.navigate("tips"); menuExpanded = false })
                                        DropdownMenuItem(text = { Text("Notifications") }, onClick = { /* TODO */ menuExpanded = false })
                                        DropdownMenuItem(text = { Text("Logout") }, onClick = { navController.navigate("welcome") { popUpTo("welcome") { inclusive = true } }; menuExpanded = false })
                                    }
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = Color.Blue
                                )
                            )
                        }
                    }
                ) { innerPadding ->
                    NavHost(navController = navController, startDestination = "welcome", modifier = Modifier.padding(innerPadding)) {
                        composable("welcome") {
                            OnDocWelcomeScreen(
                                onLoginClicked = { navController.navigate("login") },
                                onSignUpClicked = { navController.navigate("signup") }
                            )
                        }
                        composable("signup") { SignUpScreen(navController) { navController.navigate("login") } }
                        composable("login") { LoginScreen(navController = navController, onLoginSuccess = { navController.navigate("landing") }) }
                        composable("forgot_password") { ForgotPasswordScreen(navController) }
                        composable("landing") { LandingScreen(navController) }
                        composable("categories") { CategoriesScreen(navController) }
                        composable("tips") { TipsScreen() }
                        composable("profile") { ProfileScreen() }
                        composable("find_doctors") { 
                            FindDoctorsScreen(navController = navController, specializationName = null)
                        }
                        composable(
                            "find_doctors/{specializationName}",
                            arguments = listOf(navArgument("specializationName") { type = NavType.StringType; nullable = true })
                        ) { backStackEntry ->
                            val specializationName = backStackEntry.arguments?.getString("specializationName")
                            FindDoctorsScreen(navController = navController, specializationName = specializationName)
                        }
                        composable(
                            "dentist_detail/{dentistId}",
                            arguments = listOf(navArgument("dentistId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val dentistId = backStackEntry.arguments?.getString("dentistId") ?: ""
                            DentistDetailScreen(navController, dentistId)
                        }
                        composable(
                            "patient_details/{dentistId}",
                            arguments = listOf(navArgument("dentistId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val dentistId = backStackEntry.arguments?.getString("dentistId") ?: ""
                            PatientDetailsScreen(navController, dentistId)
                        }
                        composable(
                            "payment_method/{bookingFee}/{name}/{district}/{village}",
                            arguments = listOf(
                                navArgument("bookingFee") { type = NavType.IntType },
                                navArgument("name") { type = NavType.StringType },
                                navArgument("district") { type = NavType.StringType },
                                navArgument("village") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val bookingFee = backStackEntry.arguments?.getInt("bookingFee") ?: 0
                            val name = backStackEntry.arguments?.getString("name") ?: ""
                            val district = backStackEntry.arguments?.getString("district") ?: ""
                            val village = backStackEntry.arguments?.getString("village") ?: ""
                            PaymentMethodScreen(navController, bookingFee, name, district, village)
                        }
                        composable(
                            "payment_details/{paymentMethod}/{bookingFee}/{name}/{district}/{village}",
                            arguments = listOf(
                                navArgument("paymentMethod") { type = NavType.StringType },
                                navArgument("bookingFee") { type = NavType.IntType },
                                navArgument("name") { type = NavType.StringType },
                                navArgument("district") { type = NavType.StringType },
                                navArgument("village") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val paymentMethod = backStackEntry.arguments?.getString("paymentMethod") ?: ""
                            val bookingFee = backStackEntry.arguments?.getInt("bookingFee") ?: 0
                            val name = backStackEntry.arguments?.getString("name") ?: ""
                            val district = backStackEntry.arguments?.getString("district") ?: ""
                            val village = backStackEntry.arguments?.getString("village") ?: ""
                            PaymentDetailsScreen(navController, paymentMethod, bookingFee, name, district, village)
                        }
                        composable(
                            "booking_confirmation/{name}",
                            arguments = listOf(
                                navArgument("name") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val name = backStackEntry.arguments?.getString("name") ?: ""
                            BookingConfirmationScreen(navController, name)
                        }
                        composable("thanks") { ThanksScreen() }
                    }
                }
            }
        }
    }
}

// --- Screens ---
@Composable
fun OnDocWelcomeScreen(onLoginClicked: () -> Unit, onSignUpClicked: () -> Unit) {
    val offsetY = remember { Animatable(initialValue = -200f) }

    LaunchedEffect(Unit) {
        offsetY.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 800)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 32.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo3),
            contentDescription = "App Logo",
            modifier = Modifier.size(150.dp)
        )
        Text(
            text = "Welcome to the Online Doctor Booking Platform",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Box(contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = R.drawable.pic),
                contentDescription = "Doctor Image",
                modifier = Modifier.size(280.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = "Home of Health Services",
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset { IntOffset(0, offsetY.value.roundToInt()) },
                color = Color.Yellow,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = onLoginClicked, modifier = Modifier.fillMaxWidth()) {
                Text("Login")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Create Account", modifier = Modifier.clickable(onClick = onSignUpClicked))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavController, onSignUpSuccess: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TopAppBar(
            title = { Text("Sign Up") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (showError) {
                Text("Please fill all fields", color = Color.Red)
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            TextField(value = phoneNumber, onValueChange = { if (it.all { char -> char.isDigit() }) phoneNumber = it }, label = { Text("Phone Number") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            Spacer(modifier = Modifier.height(8.dp))
            TextField(value = dob, onValueChange = { dob = it }, label = { Text("Date of Birth") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            TextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = password, onValueChange = { password = it }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, "toggle password visibility")
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (name.isNotBlank() && phoneNumber.isNotBlank() && dob.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                        showError = false
                        onSignUpSuccess()
                    } else {
                        showError = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign Up")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TopAppBar(
            title = { Text("Login") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (showError) {
                Text("Please fill all fields", color = Color.Red)
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email/Phone Number") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, "toggle password visibility")
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank()) {
                        showError = false
                        onLoginSuccess()
                    } else {
                        showError = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Forgot password?",
                modifier = Modifier.clickable { navController.navigate("forgot_password") }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Forgot Password") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (showError) {
                Text("Please enter your email", color = Color.Red)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Enter your email to receive a password reset code.")
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if(email.isNotBlank()) {
                        showError = false
                        // TODO: Implement password reset logic
                        navController.popBackStack()
                    } else {
                        showError = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Send Reset Code")
            }
        }
    }
}

@Composable
fun ProfileScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Profile Screen", fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentMethodScreen(navController: NavController, bookingFee: Int, name: String, district: String, village: String) {
    val paymentMethods = listOf(
        "NBS" to R.drawable.nbs,
        "NB" to R.drawable.nb,
        "Airtel Money" to R.drawable.airtel,
        "Tnm Mpamba" to R.drawable.tnm
    )

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Choose a Payment Method", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            paymentMethods.forEach { (method, imageRes) ->
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = method,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(bottom = 8.dp)
                        .clickable { navController.navigate("payment_details/$method/$bookingFee/$name/$district/$village") },
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentDetailsScreen(navController: NavController, paymentMethod: String, bookingFee: Int, name: String, district: String, village: String) {
    var accountNumber by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf(bookingFee.toString()) }
    var showError by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (showError) {
                Text("Please fill all fields", color = Color.Red)
            }
            Text("Enter Payment Details for $paymentMethod", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            if (paymentMethod == "NBS" || paymentMethod == "NB") {
                TextField(
                    value = accountNumber,
                    onValueChange = { accountNumber = it },
                    label = { Text("Account Number") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            } else {
                TextField(
                    value = phoneNumber,
                    onValueChange = { if (it.all { char -> char.isDigit() }) phoneNumber = it },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = amount,
                onValueChange = { if (it.all { char -> char.isDigit() }) amount = it },
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { 
                    val isValid = if (paymentMethod == "NBS" || paymentMethod == "NB") {
                        accountNumber.isNotBlank() && amount.isNotBlank()
                    } else {
                        phoneNumber.isNotBlank() && amount.isNotBlank()
                    }
                    if (isValid) {
                        showError = false
                        navController.navigate("booking_confirmation/$name") 
                    } else {
                        showError = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirm Payment")
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingConfirmationScreen(navController: NavController, name: String) {

    Column(modifier = Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Thank you, your booking process is complete. looking forward to meeting you $name.",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("thanks") { popUpTo("welcome") { inclusive = true } } }) { Text("OK") }
        }
    }
}

@Composable
fun ThanksScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "your booking process is successful, thank you.",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

// --- Previews ---
@Preview(showBackground = true)
@Composable
fun OnDocWelcomeScreenPreview() {
    SmrtAppTheme {
        OnDocWelcomeScreen(onLoginClicked = {}, onSignUpClicked = {})
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SmrtAppTheme {
        SignUpScreen(navController = rememberNavController(), onSignUpSuccess = {})
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    SmrtAppTheme {
        LoginScreen(navController = rememberNavController(), onLoginSuccess = {})
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview() {
    SmrtAppTheme {
        ForgotPasswordScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentMethodScreenPreview() {
    SmrtAppTheme {
        PaymentMethodScreen(navController = rememberNavController(), 1500, "name", "district", "village")
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentDetailsScreenPreview() {
    SmrtAppTheme {
        PaymentDetailsScreen(navController = rememberNavController(), "Airtel Money", 1500, "name", "district", "village")
    }
}

@Preview(showBackground = true)
@Composable
fun BookingConfirmationScreenPreview() {
    SmrtAppTheme {
        BookingConfirmationScreen(navController = rememberNavController(), "name")
    }
}

@Preview(showBackground = true)
@Composable
fun ThanksScreenPreview() {
    SmrtAppTheme {
        ThanksScreen()
    }
}
