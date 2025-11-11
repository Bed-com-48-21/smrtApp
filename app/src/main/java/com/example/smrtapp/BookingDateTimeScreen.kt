package com.example.smrtapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.smrtapp.ui.theme.SmrtAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDateTimeScreen(navController: NavController, doctorId: String) {
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var showDoctorBookedError by remember { mutableStateOf(false) }
    val doctor = DataSource.doctors.find { it.id == doctorId }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
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
            if (showDoctorBookedError) {
                Text("Sorry the Doctor is booked, please choose another date.", color = Color.Red)
            }
            Text("Book an Appointment", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            if (doctor != null) {
                Text("with ${doctor.name}", fontSize = 18.sp)
            }
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
                        if (doctor?.isAvailable == true) {
                            showError = false
                            showDoctorBookedError = false
                            navController.navigate("dentist_detail/$doctorId")
                        } else {
                            showDoctorBookedError = true
                        }
                    } else {
                        showError = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Check Availability")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookingDateTimeScreenPreview() {
    SmrtAppTheme {
        BookingDateTimeScreen(navController = rememberNavController(), doctorId = "1")
    }
}
