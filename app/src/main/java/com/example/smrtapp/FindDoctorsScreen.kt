package com.example.smrtapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindDoctorsScreen(navController: NavController, specializationName: String?) {
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var showResults by remember { mutableStateOf(false) }
    var searching by remember { mutableStateOf(false) }
    var availableDoctors by remember { mutableStateOf<List<Doctor>>(emptyList()) }
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = specializationName ?: "All Available Doctors",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        if(showError){
            Text("Please enter both date and time", color = Color.Red)
            Spacer(modifier = Modifier.height(8.dp))
        }
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
                if (date.isNotBlank() && time.isNotBlank()) {
                    showError = false
                    searching = true
                    // In a real app, you'd perform a network request here.
                    // For this simulation, we'll just filter our local list.
                    availableDoctors = DataSource.doctors.filter { 
                        val isSpecializationMatch = specializationName == null || it.specialization.equals(specializationName, ignoreCase = true)
                        it.isAvailable && isSpecializationMatch
                    }
                    searching = false
                    showResults = true
                } else {
                    showError = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Find Available Doctors")
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (searching) {
            CircularProgressIndicator()
        }

        if (showResults) {
            if (availableDoctors.isEmpty()) {
                Text("No doctors available for the selected criteria.")
            } else {
                LazyColumn(modifier = Modifier.height(500.dp)) {
                    items(availableDoctors) { doctor ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable { navController.navigate("dentist_detail/${doctor.id}") }
                        ) {
                           Text(doctor.name, textAlign = TextAlign.Center, modifier = Modifier.padding(16.dp), fontSize = 20.sp)
                        }
                    }
                }
            }
        }
    }
} 
