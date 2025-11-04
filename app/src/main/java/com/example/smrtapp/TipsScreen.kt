package com.example.smrtapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Tip(val name: String, val description: String)

val medicalTips = listOf(
    Tip("Malaria", "Malaria is a deadly disease that can easily be avoided. This disease is transmitted by Mosquito, to stay away from Malaria, you should always make sure that you sleep in a mosquito net.")
)

@Composable
fun TipsScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTip by remember { mutableStateOf<Tip?>(null) }

    val filteredTips = if (searchQuery.isBlank()) {
        emptyList()
    } else {
        medicalTips.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.tip),
            contentDescription = "Medical Tips Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Medical Tips", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = searchQuery,
                onValueChange = { 
                    searchQuery = it
                    selectedTip = null
                },
                label = { Text("Enter a disease to learn about...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (selectedTip != null) {
                Card(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = selectedTip!!.description,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                LazyColumn {
                    items(filteredTips) { tip ->
                        Text(
                            text = tip.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedTip = tip }
                                .padding(16.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            color = if (tip.name.equals("Malaria", ignoreCase = true)) Color(0xFFE5B50A) else Color.Black
                        )
                    }
                }
            }
        }
    }
}
