package com.urbantransit.offline.presentation.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.urbantransit.offline.domain.model.UserSession
import com.urbantransit.offline.presentation.ui.theme.ChennaiColors

private val COMMUTER_AVATARS = listOf(
    "🚇" to "Metro Rider",
    "🧑‍💼" to "IT Techie",
    "👩‍💼" to "Executive",
    "👩‍🎓" to "Anna Univ Student",
    "🧕" to "Commuter",
    "👨‍🦳" to "Senior Citizen",
    "🚌" to "MTC Explorer",
    "🎧" to "Music Traveler",
    "🕶️" to "Urban Nomad",
    "🧑‍💻" to "Tidel Dev",
    "👩‍⚕️" to "Health Warrior",
    "🎒" to "Daily Voyager"
)

private val CONCESSION_CATEGORIES = listOf(
    "General Commuter",
    "Vidiyal & Vettri Payanam (Women Free Bus Scheme)",
    "Student Concession (Free Pass / 50% Off)",
    "Senior Citizen (MTC 10 Free Tokens / CMRL 20% Off)",
    "Singara Chennai NCMC Smartcard Holder (20% Off Metro)"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(
    session: UserSession,
    onBackClick: () -> Unit,
    onSaveProfile: (
        name: String,
        email: String,
        phoneNumber: String,
        address: String,
        metroCardNumber: String,
        emergencyContact: String,
        avatarEmoji: String,
        concessionCategory: String
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember(session.displayName) { mutableStateOf(session.displayName) }
    var email by remember(session.email) { mutableStateOf(session.email) }
    var phoneNumber by remember(session.phoneNumber) { mutableStateOf(session.phoneNumber) }
    var address by remember(session.address) { mutableStateOf(session.address) }
    var metroCardNumber by remember(session.metroCardNumber) {
        mutableStateOf(session.metroCardNumber ?: "CMRL-8829-4102")
    }
    var emergencyContact by remember(session.emergencyContact) {
        mutableStateOf(session.emergencyContact.ifBlank { "+91 94440 98765" })
    }
    var selectedAvatar by remember(session.avatarEmoji) {
        mutableStateOf(session.avatarEmoji.ifBlank { "🚇" })
    }
    var selectedConcession by remember(session.concessionCategory) {
        mutableStateOf(session.concessionCategory)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = ChennaiColors.DeepCanvas,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Edit Profile",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "Chennai Transit Digital Identity",
                            color = ChennaiColors.TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    Button(
                        onClick = {
                            onSaveProfile(
                                name,
                                email,
                                phoneNumber,
                                address,
                                metroCardNumber,
                                emergencyContact,
                                selectedAvatar,
                                selectedConcession
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ChennaiColors.MetroBlue),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Save", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ChennaiColors.SurfaceCardElevated
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item { Spacer(modifier = Modifier.height(6.dp)) }

            // 1. Profile Photo / Avatar Showcase & Editor
            item {
                Surface(
                    shape = RoundedCornerShape(22.dp),
                    color = ChennaiColors.SurfaceCardElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, ChennaiColors.SurfaceBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Large Avatar Circle with Glowing Accent Ring
                        Box(
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(92.dp)
                                    .background(
                                        brush = Brush.linearGradient(
                                            listOf(
                                                ChennaiColors.MetroBlue,
                                                ChennaiColors.MetroGreen,
                                                ChennaiColors.MtcAmber
                                            )
                                        ),
                                        shape = CircleShape
                                    )
                                    .padding(3.dp)
                                    .background(ChennaiColors.DeepCanvas, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = selectedAvatar,
                                    fontSize = 44.sp
                                )
                            }

                            // Camera / Edit Badge
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .background(ChennaiColors.MetroBlue, CircleShape)
                                    .border(2.dp, ChennaiColors.DeepCanvas, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Edit,
                                    contentDescription = "Edit Avatar",
                                    tint = Color.White,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = name.ifBlank { "Chennai Commuter" },
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = ChennaiColors.MetroBlueLight,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Verified Chennai Transit User",
                                fontSize = 12.sp,
                                color = ChennaiColors.TextSecondary
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Avatar Selector Horizontal Carousel
                        Text(
                            text = "CHOOSE COMMUTER AVATAR",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = ChennaiColors.TextSecondary,
                            letterSpacing = 1.2.sp,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(COMMUTER_AVATARS) { (emoji, label) ->
                                val isSelected = selectedAvatar == emoji
                                Surface(
                                    onClick = { selectedAvatar = emoji },
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) ChennaiColors.MetroBlue.copy(alpha = 0.25f) else ChennaiColors.SurfaceCard,
                                    border = androidx.compose.foundation.BorderStroke(
                                        if (isSelected) 1.5.dp else 1.dp,
                                        if (isSelected) ChennaiColors.MetroBlue else ChennaiColors.SurfaceBorder
                                    )
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                    ) {
                                        Text(text = emoji, fontSize = 24.sp)
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = label,
                                            fontSize = 10.sp,
                                            color = if (isSelected) Color.White else ChennaiColors.TextSecondary,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 2. Personal Information Fields Card
            item {
                Surface(
                    shape = RoundedCornerShape(22.dp),
                    color = ChennaiColors.SurfaceCardElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, ChennaiColors.SurfaceBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "PERSONAL INFORMATION",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = ChennaiColors.TextSecondary,
                            letterSpacing = 1.2.sp
                        )

                        // Full Name
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Full Name") },
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null, tint = ChennaiColors.MetroBlueLight)
                            },
                            colors = customTextFieldColors(),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        // Mobile Number
                        OutlinedTextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            label = { Text("Mobile Phone Number") },
                            leadingIcon = {
                                Icon(Icons.Default.Phone, contentDescription = null, tint = ChennaiColors.MetroBlueLight)
                            },
                            colors = customTextFieldColors(),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true
                        )

                        // Email Address
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email Address") },
                            leadingIcon = {
                                Icon(Icons.Default.Email, contentDescription = null, tint = ChennaiColors.MetroBlueLight)
                            },
                            colors = customTextFieldColors(),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            singleLine = true
                        )

                        // Residential / Commute Address
                        OutlinedTextField(
                            value = address,
                            onValueChange = { address = it },
                            label = { Text("Residential / Commute Address (Chennai)") },
                            leadingIcon = {
                                Icon(Icons.Default.Home, contentDescription = null, tint = ChennaiColors.MetroBlueLight)
                            },
                            colors = customTextFieldColors(),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            maxLines = 4
                        )

                        // Emergency Contact Number
                        OutlinedTextField(
                            value = emergencyContact,
                            onValueChange = { emergencyContact = it },
                            label = { Text("Emergency Contact (Family / Friend)") },
                            leadingIcon = {
                                Icon(Icons.Default.PhoneInTalk, contentDescription = null, tint = ChennaiColors.MtcRed)
                            },
                            colors = customTextFieldColors(),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true
                        )
                    }
                }
            }

            // 3. CMRL Smartcard & Transit Settings Card
            item {
                Surface(
                    shape = RoundedCornerShape(22.dp),
                    color = ChennaiColors.SurfaceCardElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, ChennaiColors.SurfaceBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "METRO SMARTCARD & FARE CONCESSION",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = ChennaiColors.TextSecondary,
                            letterSpacing = 1.2.sp
                        )

                        // Metro Card Number Field
                        OutlinedTextField(
                            value = metroCardNumber,
                            onValueChange = { metroCardNumber = it },
                            label = { Text("CMRL Metro Smartcard ID") },
                            leadingIcon = {
                                Icon(Icons.Default.CreditCard, contentDescription = null, tint = ChennaiColors.MtcAmber)
                            },
                            colors = customTextFieldColors(),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        // Concession Category
                        Text(
                            text = "COMMUTER FARE CATEGORY",
                            style = MaterialTheme.typography.labelSmall,
                            color = ChennaiColors.TextSecondary,
                            fontWeight = FontWeight.Medium
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            CONCESSION_CATEGORIES.forEach { category ->
                                val isSelected = selectedConcession == category
                                Surface(
                                    onClick = { selectedConcession = category },
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) ChennaiColors.MetroBlue.copy(alpha = 0.2f) else ChennaiColors.SurfaceCard,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSelected) ChennaiColors.MetroBlue else ChennaiColors.SurfaceBorder
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(18.dp)
                                                .background(
                                                    if (isSelected) ChennaiColors.MetroBlue else Color.Transparent,
                                                    CircleShape
                                                )
                                                .border(1.5.dp, if (isSelected) ChennaiColors.MetroBlue else ChennaiColors.TextSecondary, CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (isSelected) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = null,
                                                    tint = Color.White,
                                                    modifier = Modifier.size(12.dp)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            text = category,
                                            fontSize = 13.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) Color.White else ChennaiColors.TextPrimary
                                        )
                                    }
                                }
                            }
                        }

                        // Digital Smartcard Mockup
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = ChennaiColors.DeepCanvas,
                            border = androidx.compose.foundation.BorderStroke(1.dp, ChennaiColors.MetroBlue.copy(alpha = 0.4f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "CHENNAI METRO RAIL (CMRL)",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = ChennaiColors.MetroBlueLight,
                                        letterSpacing = 1.1.sp
                                    )
                                    Text(
                                        text = "OFFLINE PASS",
                                        fontSize = 9.sp,
                                        color = ChennaiColors.LiveOnlineGreen,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = metroCardNumber.ifBlank { "CMRL-8829-4102" },
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    letterSpacing = 2.sp
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text("CARDHOLDER", fontSize = 9.sp, color = ChennaiColors.TextSecondary)
                                        Text(name.ifBlank { "COMMUTER" }, fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text("OFFLINE BALANCE", fontSize = 9.sp, color = ChennaiColors.TextSecondary)
                                        Text("₹${session.walletBalanceCents / 100}.00", fontSize = 13.sp, color = ChennaiColors.LiveOnlineGreen, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 4. Save and Cancel Buttons
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onBackClick,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Text("Cancel", color = ChennaiColors.TextSecondary)
                    }

                    Button(
                        onClick = {
                            onSaveProfile(
                                name,
                                email,
                                phoneNumber,
                                address,
                                metroCardNumber,
                                emergencyContact,
                                selectedAvatar,
                                selectedConcession
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ChennaiColors.MetroBlue),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .weight(1.5f)
                            .height(50.dp)
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Save Profile", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(30.dp)) }
        }
    }
}

@Composable
private fun customTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = ChennaiColors.TextPrimary,
    focusedBorderColor = ChennaiColors.MetroBlue,
    unfocusedBorderColor = ChennaiColors.SurfaceBorder,
    focusedLabelColor = ChennaiColors.MetroBlueLight,
    unfocusedLabelColor = ChennaiColors.TextSecondary,
    cursorColor = ChennaiColors.MetroBlue
)
