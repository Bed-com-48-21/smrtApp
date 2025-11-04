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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.draw.clip
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
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

// --- Data Classes ---
data class Specialization(val name: String)
data class Doctor(val id: String, val name: String, val specialization: String, val clinic: String, val location: String, val contact: String, val bookingFee: Int)

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
                        if (currentRoute != "welcome" && currentRoute != "signup" && currentRoute != "login" && currentRoute != "forgot_password"  && currentRoute != "thanks") {
                            TopAppBar(
                                title = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Image(
                                            painter = painterResource(id = R.drawable.medical),
                                            contentDescription = "Logo",
                                            modifier = Modifier.size(40.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("SmrtApp", color = Color.White)
                                    }
                                },
                                actions = {
                                    TextButton(onClick = { navController.navigate("landing") }) {
                                        Text("Home", color = Color.White)
                                    }
                                    TextButton(onClick = { navController.navigate("doctors") }) {
                                        Text("Categories", color = Color.White)
                                    }
                                    IconButton(onClick = { navController.navigate("welcome") { popUpTo("welcome") { inclusive = true } } }) {
                                        Icon(Icons.Default.Logout, contentDescription = "Logout", tint = Color.White)
                                    }
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = Color.Blue
                                )
                            )
                        }
                    },
                    bottomBar = {
                        if (currentRoute != "welcome" && currentRoute != "signup" && currentRoute != "login" && currentRoute != "forgot_password" && currentRoute != "thanks") {
                            Footer(navController)
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
                        composable("doctors") { DoctorsScreen(navController) }
                        composable(
                            "booking_date_time/{specializationName}",
                            arguments = listOf(navArgument("specializationName") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val specializationName = backStackEntry.arguments?.getString("specializationName") ?: ""
                            BookingDateTimeScreen(navController, specializationName)
                        }
                        composable(
                            "dentist_list/{specializationName}",
                            arguments = listOf(navArgument("specializationName") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val specializationName = backStackEntry.arguments?.getString("specializationName") ?: ""
                            DentistListScreen(navController, specializationName)
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
                            "patient_location/{dentistId}/{name}/{age}/{contact}/{relationship}",
                            arguments = listOf(
                                navArgument("dentistId") { type = NavType.StringType },
                                navArgument("name") { type = NavType.StringType },
                                navArgument("age") { type = NavType.StringType },
                                navArgument("contact") { type = NavType.StringType },
                                navArgument("relationship") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val dentistId = backStackEntry.arguments?.getString("dentistId") ?: ""
                            val name = backStackEntry.arguments?.getString("name") ?: ""
                            val age = backStackEntry.arguments?.getString("age") ?: ""
                            val contact = backStackEntry.arguments?.getString("contact") ?: ""
                            val relationship = backStackEntry.arguments?.getString("relationship") ?: ""
                            PatientLocationScreen(navController, dentistId, name, age, contact, relationship)
                        }
                        composable(
                            "payment_method/{bookingFee}/{name}/{district}/{village}/{day}",
                            arguments = listOf(
                                navArgument("bookingFee") { type = NavType.IntType },
                                navArgument("name") { type = NavType.StringType },
                                navArgument("district") { type = NavType.StringType },
                                navArgument("village") { type = NavType.StringType },
                                navArgument("day") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val bookingFee = backStackEntry.arguments?.getInt("bookingFee") ?: 0
                            val name = backStackEntry.arguments?.getString("name") ?: ""
                            val district = backStackEntry.arguments?.getString("district") ?: ""
                            val village = backStackEntry.arguments?.getString("village") ?: ""
                            val day = backStackEntry.arguments?.getString("day") ?: ""
                            PaymentMethodScreen(navController, bookingFee, name, district, village, day)
                        }
                        composable(
                            "payment_details/{paymentMethod}/{bookingFee}/{name}/{district}/{village}/{day}",
                            arguments = listOf(
                                navArgument("paymentMethod") { type = NavType.StringType },
                                navArgument("bookingFee") { type = NavType.IntType },
                                navArgument("name") { type = NavType.StringType },
                                navArgument("district") { type = NavType.StringType },
                                navArgument("village") { type = NavType.StringType },
                                navArgument("day") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val paymentMethod = backStackEntry.arguments?.getString("paymentMethod") ?: ""
                            val bookingFee = backStackEntry.arguments?.getInt("bookingFee") ?: 0
                            val name = backStackEntry.arguments?.getString("name") ?: ""
                            val district = backStackEntry.arguments?.getString("district") ?: ""
                            val village = backStackEntry.arguments?.getString("village") ?: ""
                            val day = backStackEntry.arguments?.getString("day") ?: ""
                            PaymentDetailsScreen(navController, paymentMethod, bookingFee, name, district, village, day)
                        }
                        composable(
                            "booking_confirmation/{name}/{district}/{village}/{day}",
                            arguments = listOf(
                                navArgument("name") { type = NavType.StringType },
                                navArgument("district") { type = NavType.StringType },
                                navArgument("village") { type = NavType.StringType },
                                navArgument("day") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val name = backStackEntry.arguments?.getString("name") ?: ""
                            val district = backStackEntry.arguments?.getString("district") ?: ""
                            val village = backStackEntry.arguments?.getString("village") ?: ""
                            val day = backStackEntry.arguments?.getString("day") ?: ""
                            BookingConfirmationScreen(navController, name, district, village, day)
                        }
                        composable("thanks") { ThanksScreen(navController) }
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
            .padding(vertical = 32.dp),
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
            .verticalScroll(rememberScrollState())
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
            .fillMaxSize()
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
                label = { Text("Email") },
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TopAppBar(
            title = { Text("Forgot Password") },
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
                Text("Please enter your email", color = Color.Red)
            }
            Text(text = "Forgot Password", fontSize = 24.sp, fontWeight = FontWeight.Bold)
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

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LandingScreen(navController: NavController) {
    val pagerState = rememberPagerState()
    val texts = listOf(
        "Want a medical help? you are in right place, we are here to cater your healthy needs",
        "Enter the category name above, to pin down the type of doctor you need",
        "we offer medical exparts of all sorts"
    )
    val specializations = remember { listOf(Specialization("Dentist"), Specialization("Physicist"), Specialization("Optometry"), Specialization("Psychologist"), Specialization("Surgeon")) }
    var searchQuery by remember { mutableStateOf("") }
    val filteredSpecializations = specializations.filter { it.name.contains(searchQuery, ignoreCase = true) }

    LaunchedEffect(pagerState) {
        while (true) {
            delay(5000)
            pagerState.animateScrollToPage((pagerState.currentPage + 1) % pagerState.pageCount)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search for a category...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (searchQuery.isBlank()) {
                Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(id = R.drawable.medical),
                        contentDescription = "Medical Equipment",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    HorizontalPager(
                        count = texts.size,
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = texts[it],
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp),
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            if(searchQuery.isNotBlank()) {
                LazyColumn(modifier = Modifier.height(500.dp)) {
                    items(filteredSpecializations) { specialization ->
                        Text(
                            text = specialization.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { navController.navigate("booking_date_time/${specialization.name}") }
                                .padding(16.dp),
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorsScreen(navController: NavController) {
    val doctors = remember {
        listOf(
            Doctor("1", "Dr. Daniel Medson", "Dentist", "Ali Clinic", "Lilongwe", "+265 883 760 614", 1500),
            Doctor("2", "Dr. Bless Katumbi", "Dentist", "Happy Teeth Dental", "Los Angeles", "098-765-4321", 2000),
            Doctor("3", "Dr Mercy chiponde", "Dentist", "Mercy Clinic", "Blantyre", "111-222-3333", 1800),
            Doctor("4", "Dr hestings chapotera", "Dentist", "Chapotera Clinic", "Zomba", "444-555-6666", 2500)
        )
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Our Doctors", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            doctors.forEach { doctor ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .clickable { navController.navigate("dentist_detail/${doctor.id}") }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.pic),
                            contentDescription = "Doctor's profile picture",
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                        Column {
                            Text(doctor.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text(doctor.specialization, fontSize = 16.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDateTimeScreen(navController: NavController, specializationName: String) {
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
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
            Text("Book an Appointment", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Specialization: $specializationName", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = date,
                onValueChange = { date = it },
                label = { Text("Enter Date (DD/MM/YYYY)") },
                leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = "Date") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = time,
                onValueChange = { time = it },
                label = { Text("Enter Time (e.g., 14:00)") },
                leadingIcon = { Icon(Icons.Default.Notifications, contentDescription = "Time") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { 
                    if(date.isNotBlank() && time.isNotBlank()) {
                        showError = false
                        navController.navigate("dentist_list/$specializationName") 
                    } else {
                        showError = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Find Doctors")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DentistListScreen(navController: NavController, specializationName: String) {
    val allDoctors = remember {
        listOf(
            Doctor("1", "Dr. Daniel Medson", "Dentist", "Ali Clinic", "Lilongwe", "+265 883 760 614", 1500),
            Doctor("2", "Dr. Bless Katumbi", "Dentist", "Happy Teeth Dental", "Los Angeles", "098-765-4321", 2000),
            Doctor("3", "Dr Mercy chiponde", "Dentist", "Mercy Clinic", "Blantyre", "111-222-3333", 1800),
            Doctor("4", "Dr hestings chapotera", "Dentist", "Chapotera Clinic", "Zomba", "444-555-6666", 2500)
        )
    }
    var searchQuery by remember { mutableStateOf("") }
    val filteredDoctors = allDoctors.filter { it.name.contains(searchQuery, ignoreCase = true) && it.specialization.equals(specializationName, ignoreCase = true) }

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(value = searchQuery, onValueChange = { searchQuery = it }, label = { Text("Enter doctor's name") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            filteredDoctors.forEach { doctor ->
                Text(
                    text = doctor.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("dentist_detail/${doctor.id}") }
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DentistDetailScreen(navController: NavController, dentistId: String) {
    val doctor = remember {
        listOf(
            Doctor("1", "Dr. Daniel Medson", "Dentist", "Ali Clinic", "Lilongwe", "+265 881 770 119", 1500),
            Doctor("2", "Dr. Bless Katumbi", "Dentist", "Happy Teeth Dental", "Los Angeles", "098-765-4321", 2000),
            Doctor("3", "Dr Mercy chiponde", "Dentist", "Mercy Clinic", "Blantyre", "111-222-3333", 1800),
            Doctor("4", "Dr hestings chapotera", "Dentist", "Chapotera Clinic", "Zomba", "444-555-6666", 2500)
        ).find { it.id == dentistId }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {
        if (doctor != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = doctor.name, fontSize = 24.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Clinic: ${doctor.clinic}", textAlign = TextAlign.Center)
                Text(text = "Location: ${doctor.location}", textAlign = TextAlign.Center)
                Text(text = "Contact: ${doctor.contact}", textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Booking Fee: ${doctor.bookingFee}", textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { navController.navigate("patient_details/${doctor.id}") }) { Text("Book now") }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Doctor not found.")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientDetailsScreen(navController: NavController, dentistId: String) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var relationship by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    val doctor = remember {
        listOf(
            Doctor("1", "Dr. Daniel Medson", "Dentist", "Ali Clinic", "Lilongwe", "+265 881 770 119", 1500),
            Doctor("2", "Dr. Bless Katumbi", "Dentist", "Happy Teeth Dental", "Los Angeles", "098-765-4321", 2000),
            Doctor("3", "Dr Mercy chiponde", "Dentist", "Mercy Clinic", "Blantyre", "111-222-3333", 1800),
            Doctor("4", "Dr hestings chapotera", "Dentist", "Chapotera Clinic", "Zomba", "444-555-6666", 2500)
        ).find { it.id == dentistId }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            if (showError) {
                Text("Please fill all fields", color = Color.Red)
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            TextField(value = age, onValueChange = { age = it }, label = { Text("Age") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            TextField(value = contact, onValueChange = { if (it.all { char -> char.isDigit() }) contact = it }, label = { Text("Contact Number") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            Spacer(modifier = Modifier.height(8.dp))
            TextField(value = relationship, onValueChange = { relationship = it }, label = { Text("Relationship to Patient") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    if (name.isNotBlank() && age.isNotBlank() && contact.isNotBlank() && relationship.isNotBlank()) {
                        showError = false
                        navController.navigate("patient_location/$dentistId/$name/$age/$contact/$relationship")
                    } else {
                        showError = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Continue") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientLocationScreen(navController: NavController, dentistId: String, name: String, age: String, contact: String, relationship: String) {
    var district by remember { mutableStateOf("") }
    var village by remember { mutableStateOf("") }
    var street by remember { mutableStateOf("") }
    var postalAddress by remember { mutableStateOf("") }
    var day by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    val doctor = remember {
        listOf(
            Doctor("1", "Dr. Daniel Medson", "Dentist", "Ali Clinic", "Lilongwe", "+265 881 770 119", 1500),
            Doctor("2", "Dr. Bless Katumbi", "Dentist", "Happy Teeth Dental", "Los Angeles", "098-765-4321", 2000),
            Doctor("3", "Dr Mercy chiponde", "Dentist", "Mercy Clinic", "Blantyre", "111-222-3333", 1800),
            Doctor("4", "Dr hestings chapotera", "Dentist", "Chapotera Clinic", "Zomba", "444-555-6666", 2500)
        ).find { it.id == dentistId }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            if (showError) {
                Text("Please fill all fields", color = Color.Red)
            }
            TextField(value = district, onValueChange = { district = it }, label = { Text("District") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            TextField(value = village, onValueChange = { village = it }, label = { Text("Village") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            TextField(value = street, onValueChange = { street = it }, label = { Text("Street") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            TextField(value = postalAddress, onValueChange = { postalAddress = it }, label = { Text("Postal Address") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            TextField(value = day, onValueChange = { day = it }, label = { Text("Day") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { 
                    if (district.isNotBlank() && village.isNotBlank() && street.isNotBlank() && postalAddress.isNotBlank() && day.isNotBlank()) {
                        showError = false
                        navController.navigate("payment_method/${doctor?.bookingFee}/$name/$district/$village/$day") 
                    } else {
                        showError = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continue")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentMethodScreen(navController: NavController, bookingFee: Int, name: String, district: String, village: String, day: String) {
    val paymentMethods = listOf(
        "NBS" to R.drawable.nbs, // Replace with your drawable resource
        "NB" to R.drawable.nb, // Replace with your drawable resource
        "Airtel Money" to R.drawable.airtel, // Replace with your drawable resource
        "Tnm Mpamba" to R.drawable.tnm // Replace with your drawable resource
    )

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {
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
                        .height(80.dp)
                        .padding(bottom = 8.dp)
                        .clickable { navController.navigate("payment_details/$method/$bookingFee/$name/$district/$village/$day") },
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentDetailsScreen(navController: NavController, paymentMethod: String, bookingFee: Int, name: String, district: String, village: String, day: String) {
    var accountNumber by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf(bookingFee.toString()) }
    var showError by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {
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
                        navController.navigate("booking_confirmation/$name/$district/$village/$day") 
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
fun BookingConfirmationScreen(navController: NavController, name: String, district: String, village: String, day: String) {

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Thanks, your booking process is complete. Your name is $name located at $district, $village, on $day we will find you.",
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
fun ThanksScreen(navController: NavController) {
    val scale = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )
        delay(2000)
        navController.navigate("welcome") { popUpTo("welcome") { inclusive = true } }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Thanks!",
            color = Color.White,
            fontSize = 48.sp * scale.value,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun Footer(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text("About", modifier = Modifier.clickable { /*TODO*/ })
        Text("Contact Us", modifier = Modifier.clickable { /*TODO*/ })
        Text("Privacy Policy", modifier = Modifier.clickable { /*TODO*/ })
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
fun LandingScreenPreview() {
    SmrtAppTheme {
        LandingScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun DoctorsScreenPreview() {
    SmrtAppTheme {
        DoctorsScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun BookingDateTimeScreenPreview() {
    SmrtAppTheme {
        BookingDateTimeScreen(navController = rememberNavController(), specializationName = "Dentist")
    }
}

@Preview(showBackground = true)
@Composable
fun DentistListScreenPreview() {
    SmrtAppTheme {
        DentistListScreen(navController = rememberNavController(), specializationName = "Dentist")
    }
}

@Preview(showBackground = true)
@Composable
fun DentistDetailScreenPreview() {
    SmrtAppTheme {
        DentistDetailScreen(navController = rememberNavController(), dentistId = "1")
    }
}

@Preview(showBackground = true)
@Composable
fun PatientDetailsScreenPreview() {
    SmrtAppTheme {
        PatientDetailsScreen(navController = rememberNavController(), dentistId = "1")
    }
}

@Preview(showBackground = true)
@Composable
fun PatientLocationScreenPreview() {
    SmrtAppTheme {
        PatientLocationScreen(navController = rememberNavController(), "1", "name", "age", "contact", "relationship")
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentMethodScreenPreview() {
    SmrtAppTheme {
        PaymentMethodScreen(navController = rememberNavController(), 1500, "name", "district", "village", "day")
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentDetailsScreenPreview() {
    SmrtAppTheme {
        PaymentDetailsScreen(navController = rememberNavController(), "Airtel Money", 1500, "name", "district", "village", "day")
    }
}

@Preview(showBackground = true)
@Composable
fun BookingConfirmationScreenPreview() {
    SmrtAppTheme {
        BookingConfirmationScreen(navController = rememberNavController(), "name", "district", "village", "day")
    }
}

@Preview(showBackground = true)
@Composable
fun ThanksScreenPreview() {
    SmrtAppTheme {
        ThanksScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun FooterPreview() {
    SmrtAppTheme {
        Footer(navController = rememberNavController())
    }
}
