package com.example.smrtapp

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LandingScreen(navController: NavController) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val pagerState = rememberPagerState()
            val slideData = listOf(
                "Want a medical help? you are in the right place, we are here to cater your healthy needs" to R.drawable.virus3,
                "Enter the date and time you need a doctor, to pin down the type of doctor you need" to R.drawable.virus2,
                "We offer medical experts of all sorts" to R.drawable.bac7
            )

            LaunchedEffect(pagerState) {
                while (true) {
                    delay(5000)
                    pagerState.animateScrollToPage((pagerState.currentPage + 1) % slideData.size)
                }
            }

            HorizontalPager(
                count = slideData.size,
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) { page ->
                val (text, imageRes) = slideData[page]
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = text,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp),
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = { navController.navigate("find_doctors") }) {
                Text("Find a Doctor by Date and Time", fontSize = 18.sp)
            }

            val arrowOffset = remember { Animatable(0f) }
            LaunchedEffect(Unit) {
                while (true) {
                    arrowOffset.animateTo(
                        targetValue = -20f,
                        animationSpec = tween(durationMillis = 500)
                    )
                    arrowOffset.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(durationMillis = 500)
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ArrowUpward,
                contentDescription = "Find a doctor",
                modifier = Modifier
                    .size(48.dp)
                    .offset(y = arrowOffset.value.dp),
                tint = Color.Blue
            )

            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.med),
                contentDescription = "med image",
                modifier = Modifier.size(330.dp)
            )
        }
    }
}
