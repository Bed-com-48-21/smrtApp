package com.example.smrtapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.smrtapp.ui.theme.SmrtAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DentistListScreen(navController: NavController, specializationName: String) {
    var searchQuery by remember { mutableStateOf("") }
    var showDoctorNotFoundError by remember { mutableStateOf(false) }
    var searching by remember { mutableStateOf(false) }
    val filteredDoctors = DataSource.doctors.filter { it.name.contains(searchQuery, ignoreCase = true) && it.specialization.equals(specializationName, ignoreCase = true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                TextField(value = searchQuery, onValueChange = { searchQuery = it }, label = { Text("Enter doctor's name") }, modifier = Modifier.weight(1f))
                Button(onClick = { 
                    searching = true
                    showDoctorNotFoundError = filteredDoctors.isEmpty()
                    searching = false
                }) {
                    Text("Go")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if(searching){
                CircularProgressIndicator()
            } else if (showDoctorNotFoundError) {
                Text("The doctor does not exist in the database")
            } else {
                filteredDoctors.forEach { doctor ->
                    Text(
                        text = doctor.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate("booking_date_time/${doctor.id}") }
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DentistListScreenPreview() {
    SmrtAppTheme {
        DentistListScreen(navController = rememberNavController(), specializationName = "Dentist")
    }
}
