package com.urbantransit.offline.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.urbantransit.offline.data.local.entity.TransitRoute
import com.urbantransit.offline.data.local.entity.TransitStop
import com.urbantransit.offline.domain.fare.CalculatedFare
import com.urbantransit.offline.presentation.ui.components.Holographic3DPass
import com.urbantransit.offline.presentation.ui.components.animatedSpecularSheen
import com.urbantransit.offline.presentation.ui.components.bounceClick
import com.urbantransit.offline.presentation.ui.components.interactive3DTilt
import com.urbantransit.offline.presentation.ui.theme.ChennaiColors

@Composable
fun FareBreakdownScreen(
    route: TransitRoute?,
    originStop: TransitStop?,
    destinationStop: TransitStop?,
    fare: CalculatedFare?,
    onBackClick: () -> Unit,
    onPurchaseOfflineTicket: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPurchased by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ChennaiColors.DeepCanvas)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        // Top Navigation Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(40.dp)
                    .background(ChennaiColors.SurfaceCardElevated, CircleShape)
                    .border(1.dp, ChennaiColors.SurfaceBorder, CircleShape)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = "Fare Breakdown & Offline Pass",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Official CMRL & MTC Rate Calculation",
                    fontSize = 12.sp,
                    color = ChennaiColors.TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (fare != null && originStop != null && destinationStop != null && route != null) {
            // Station Corridor Summary Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = ChennaiColors.SurfaceCard,
                border = androidx.compose.foundation.BorderStroke(1.dp, ChennaiColors.SurfaceBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = route.longName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("ORIGIN", fontSize = 10.sp, color = ChennaiColors.TextMuted, fontWeight = FontWeight.Bold)
                            Text(originStop.name, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = ChennaiColors.LiveOnlineGreen)
                        }
                        Text("➔", fontSize = 18.sp, color = ChennaiColors.TextMuted)
                        Column(horizontalAlignment = Alignment.End) {
                            Text("DESTINATION", fontSize = 10.sp, color = ChennaiColors.TextMuted, fontWeight = FontWeight.Bold)
                            Text(destinationStop.name, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = ChennaiColors.AlertRed)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = ChennaiColors.SurfaceBorder)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Traversed Distance", fontSize = 13.sp, color = ChennaiColors.TextSecondary)
                        Text(fare.formattedDistance, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Estimated Duration", fontSize = 13.sp, color = ChennaiColors.TextSecondary)
                        Text("~24 mins", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Official Fare Matrix Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = ChennaiColors.SurfaceCardElevated,
                border = androidx.compose.foundation.BorderStroke(1.dp, ChennaiColors.MetroBlue.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "OFFICIAL TARIFF COMPUTATION",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = ChennaiColors.TextSecondary,
                        letterSpacing = 1.2.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Standard Token Fare", fontSize = 14.sp, color = Color.White)
                        Text(fare.formattedTotal, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Metro Card / QR (20% Off)", fontSize = 14.sp, color = ChennaiColors.LiveOnlineGreen, fontWeight = FontWeight.SemiBold)
                            Text("National Common Mobility Card (NCMC)", fontSize = 10.sp, color = ChennaiColors.TextMuted)
                        }
                        Text(fare.formattedDiscounted, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = ChennaiColors.LiveOnlineGreen)
                    }

                    if (fare.isWomenFreeEligible) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            color = ChennaiColors.MtcPinkWomen.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ChennaiColors.MtcPinkWomen)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("🌸", fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text("Vidiyal & Vettri Payanam Thittam", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White)
                                    Text("Free zero-ticket travel for all women commuters across MTC bus services", fontSize = 10.sp, color = ChennaiColors.TextSecondary)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Rule: ${fare.appliedRuleDescription}",
                        fontSize = 11.sp,
                        color = ChennaiColors.TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 3D Holographic Offline Smart Ticket Pass
            if (isPurchased) {
                Holographic3DPass(
                    ticketId = "TK-${originStop.id.take(4).uppercase()}-${destinationStop.id.take(4).uppercase()}-2026",
                    routeName = route.longName,
                    originName = originStop.name,
                    destinationName = destinationStop.name,
                    fareRupees = if (fare.isWomenFreeEligible) "₹0 (Free)" else fare.formattedDiscounted,
                    isWomenFree = fare.isWomenFreeEligible,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Button(
                    onClick = {
                        isPurchased = true
                        onPurchaseOfflineTicket()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ChennaiColors.MetroBlue),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .animatedSpecularSheen()
                ) {
                    Icon(Icons.Default.ConfirmationNumber, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (fare.isWomenFreeEligible)
                            "Generate Vidiyal Payanam Free Pass"
                        else
                            "Generate 3D Offline Smartcard Pass (${fare.formattedDiscounted})",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
