package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.res.painterResource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.PhoneCategory
import com.example.ui.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainContainer()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContainer() {
    val viewModel: InstallmentViewModel = viewModel()
    
    // Tab State: 0 -> Explore, 1 -> Calculator, 2 -> Apply, 3 -> Tracking, 4 -> Support
    var selectedTab by remember { mutableStateOf(0) }

    // Preloaded inquiry states
    var preloadedDeviceName by remember { mutableStateOf("") }
    var preloadedPrice by remember { mutableStateOf(0.0) }
    var preloadedDuration by remember { mutableStateOf(12) }
    var preloadedDownPercent by remember { mutableStateOf(15) }
    var preloadedCategory by remember { mutableStateOf(PhoneCategory.TOUCH) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(end = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_brand_logo),
                                contentDescription = "Mobile Installments PK Logo",
                                modifier = Modifier
                                    .size(38.dp)
                                    .testTag("app_brand_logo")
                            )
                            Column(verticalArrangement = Arrangement.Center) {
                                Text(
                                    text = "PAK INSTALLMENTS",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 14.sp,
                                    color = Color(0xFF006D5B),
                                    letterSpacing = (-0.2).sp
                                )
                                Text(
                                    text = "Easy Monthly Plans 🇵🇰",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Gray
                                )
                            }
                        }
                        
                        val context = androidx.compose.ui.platform.LocalContext.current
                        Surface(
                            onClick = {
                                val intent = android.content.Intent(android.content.Intent.ACTION_DIAL, android.net.Uri.parse("tel:03482640090"))
                                intent.flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
                                context.startActivity(intent)
                            },
                            color = Color(0xFFF1F5F9),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "0348-2640090",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = Color(0xFF006D5B)
                                )
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .background(Color(0xFF006D5B), shape = androidx.compose.foundation.shape.CircleShape),
                                    contentAlignment = androidx.compose.ui.Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Call,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(10.dp)
                                    )
                                }
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                ),
                modifier = Modifier.background(Color.White)
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.PhoneAndroid, contentDescription = "Explore") },
                    label = { Text("Mobiles", fontSize = 10.sp) },
                    modifier = Modifier.testTag("nav_mobiles_tab")
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.Calculate, contentDescription = "Calculator") },
                    label = { Text("Calculator", fontSize = 10.sp) },
                    modifier = Modifier.testTag("nav_calculator_tab")
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.AddBox, contentDescription = "Apply") },
                    label = { Text("Apply", fontSize = 10.sp) },
                    modifier = Modifier.testTag("nav_apply_tab")
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Default.Assignment, contentDescription = "Track Status") },
                    label = { Text("Status", fontSize = 10.sp) },
                    modifier = Modifier.testTag("nav_status_tab")
                )
                NavigationBarItem(
                    selected = selectedTab == 4,
                    onClick = { selectedTab = 4 },
                    icon = { Icon(Icons.Default.HelpOutline, contentDescription = "Support") },
                    label = { Text("Support", fontSize = 10.sp) },
                    modifier = Modifier.testTag("nav_support_tab")
                )
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            when (selectedTab) {
                0 -> {
                    ExploreScreen(
                        viewModel = viewModel,
                        onNavigateToInquiry = { phone, months, downPercent ->
                            preloadedDeviceName = phone.name
                            preloadedPrice = phone.price
                            preloadedDuration = months
                            preloadedDownPercent = downPercent
                            preloadedCategory = phone.category
                            selectedTab = 2 // Move to Apply page!
                        }
                    )
                }
                1 -> {
                    CalculatorScreen(
                        viewModel = viewModel,
                        onNavigateWithPlan = { deviceName, price, duration, downPercent, category ->
                            preloadedDeviceName = deviceName
                            preloadedPrice = price
                            preloadedDuration = duration
                            preloadedDownPercent = downPercent
                            preloadedCategory = category
                            selectedTab = 2 // Move to Apply page!
                        }
                    )
                }
                2 -> {
                    ApplyScreen(
                        viewModel = viewModel,
                        predefinedDeviceName = preloadedDeviceName,
                        predefinedPrice = preloadedPrice,
                        predefinedDuration = preloadedDuration,
                        predefinedDownPercent = preloadedDownPercent,
                        predefinedCategory = preloadedCategory,
                        onSuccess = {
                            // Reset preloads upon success
                            preloadedDeviceName = ""
                            preloadedPrice = 0.0
                            preloadedDuration = 12
                            preloadedDownPercent = 15
                            preloadedCategory = PhoneCategory.TOUCH
                            // Direct customers to view their local timeline tracker!
                            selectedTab = 3
                        }
                    )
                }
                3 -> {
                    StatusScreen(viewModel = viewModel)
                }
                4 -> {
                    SupportScreen(viewModel = viewModel)
                }
            }
        }
    }
}
