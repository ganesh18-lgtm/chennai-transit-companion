package com.urbantransit.offline.presentation.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.urbantransit.offline.data.local.entity.TransitStop
import com.urbantransit.offline.presentation.ui.theme.ChennaiColors

@Composable
fun ChennaiTransitSchematicMap(
    originStop: TransitStop?,
    destinationStop: TransitStop?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(ChennaiColors.SurfaceCard, RoundedCornerShape(20.dp))
            .border(1.dp, ChennaiColors.SurfaceBorder, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "CHENNAI METRO & BUS CORRIDORS",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = ChennaiColors.TextSecondary,
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = "Blue Line • Green Line • Yellow Line (Phase 2)",
                    style = MaterialTheme.typography.bodySmall,
                    color = ChennaiColors.TextMuted
                )
            }

            Surface(
                color = ChennaiColors.DeepCanvas,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "CMRL PHASE 1 & 2",
                    color = ChennaiColors.MetroBlueLight,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Vector Canvas Schematic
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .background(ChennaiColors.DeepCanvas, RoundedCornerShape(14.dp))
                .border(1.dp, ChennaiColors.SurfaceBorder.copy(alpha = 0.5f), RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxWidth().height(230.dp)) {
                val width = size.width
                val height = size.height

                // Schematic Key Nodes
                // Blue Line Nodes (Corridor 1)
                val pWimco = Offset(width * 0.76f, height * 0.08f)
                val pWasherman = Offset(width * 0.72f, height * 0.20f)
                val pCentral = Offset(width * 0.64f, height * 0.32f)
                val pLic = Offset(width * 0.60f, height * 0.44f)
                val pThousandLights = Offset(width * 0.56f, height * 0.54f) // Blue & Yellow Interchange
                val pGuindy = Offset(width * 0.46f, height * 0.72f)
                val pAlandur = Offset(width * 0.40f, height * 0.82f) // Blue & Green Interchange
                val pAirport = Offset(width * 0.28f, height * 0.92f)

                // Green Line Nodes (Corridor 2)
                val pEgmore = Offset(width * 0.52f, height * 0.30f)
                val pShenoy = Offset(width * 0.38f, height * 0.28f)
                val pCmbt = Offset(width * 0.22f, height * 0.40f)
                val pVadapalani = Offset(width * 0.30f, height * 0.58f) // Green & Yellow Interchange
                val pAshok = Offset(width * 0.34f, height * 0.70f)
                val pStThomas = Offset(width * 0.46f, height * 0.90f)

                // Yellow Line Nodes (Corridor 4 - Poonamallee to Lighthouse)
                val pPoonamallee = Offset(width * 0.08f, height * 0.56f)
                val pPorur = Offset(width * 0.18f, height * 0.57f)
                // Intersects Vadapalani at pVadapalani (0.30f, 0.58f)
                val pPanagalPark = Offset(width * 0.43f, height * 0.56f)
                // Intersects Thousand Lights at pThousandLights (0.56f, 0.54f)
                val pRoyapettah = Offset(width * 0.68f, height * 0.55f)
                val pThirumayilai = Offset(width * 0.76f, height * 0.62f)
                val pLighthouse = Offset(width * 0.86f, height * 0.54f)

                // 1. Draw Blue Line Path (Corridor 1)
                val bluePath = Path().apply {
                    moveTo(pWimco.x, pWimco.y)
                    lineTo(pWasherman.x, pWasherman.y)
                    lineTo(pCentral.x, pCentral.y)
                    lineTo(pLic.x, pLic.y)
                    lineTo(pThousandLights.x, pThousandLights.y)
                    lineTo(pGuindy.x, pGuindy.y)
                    lineTo(pAlandur.x, pAlandur.y)
                    lineTo(pAirport.x, pAirport.y)
                }

                drawPath(
                    path = bluePath,
                    color = ChennaiColors.MetroBlue.copy(alpha = 0.3f),
                    style = Stroke(width = 10f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )
                drawPath(
                    path = bluePath,
                    color = ChennaiColors.MetroBlue,
                    style = Stroke(width = 5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )

                // 2. Draw Green Line Path (Corridor 2)
                val greenPath = Path().apply {
                    moveTo(pCentral.x, pCentral.y)
                    lineTo(pEgmore.x, pEgmore.y)
                    lineTo(pShenoy.x, pShenoy.y)
                    lineTo(pCmbt.x, pCmbt.y)
                    lineTo(pVadapalani.x, pVadapalani.y)
                    lineTo(pAshok.x, pAshok.y)
                    lineTo(pAlandur.x, pAlandur.y)
                    lineTo(pStThomas.x, pStThomas.y)
                }

                drawPath(
                    path = greenPath,
                    color = ChennaiColors.MetroGreen.copy(alpha = 0.3f),
                    style = Stroke(width = 10f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )
                drawPath(
                    path = greenPath,
                    color = ChennaiColors.MetroGreen,
                    style = Stroke(width = 5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )

                // 3. Draw Yellow Line Path (Corridor 4)
                // Operational section: Poonamallee -> Porur (all stops) -> Vadapalani (express non-stop)
                val yellowOperationalPath = Path().apply {
                    moveTo(pPoonamallee.x, pPoonamallee.y)
                    lineTo(pPorur.x, pPorur.y)
                    lineTo(pVadapalani.x, pVadapalani.y)
                }

                drawPath(
                    path = yellowOperationalPath,
                    color = ChennaiColors.MtcAmber.copy(alpha = 0.35f),
                    style = Stroke(width = 10f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )
                drawPath(
                    path = yellowOperationalPath,
                    color = ChennaiColors.MtcAmber,
                    style = Stroke(width = 5.5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )

                // Future Phase 2 Extension: Vadapalani -> Lighthouse (Under Construction)
                val yellowFuturePath = Path().apply {
                    moveTo(pVadapalani.x, pVadapalani.y)
                    lineTo(pPanagalPark.x, pPanagalPark.y)
                    lineTo(pThousandLights.x, pThousandLights.y)
                    lineTo(pRoyapettah.x, pRoyapettah.y)
                    lineTo(pThirumayilai.x, pThirumayilai.y)
                    lineTo(pLighthouse.x, pLighthouse.y)
                }

                drawPath(
                    path = yellowFuturePath,
                    color = ChennaiColors.MtcAmber.copy(alpha = 0.25f),
                    style = Stroke(
                        width = 3.5f,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round,
                        pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(10f, 8f), 0f)
                    )
                )

                // 4. Draw Regular Station Node Dots
                // Intermediate all-stops nodes on Poonamallee-Porur stretch
                val pPoonamalleeGH = Offset(pPoonamallee.x + (pPorur.x - pPoonamallee.x) * 0.25f, pPoonamallee.y)
                val pKumananchavadi = Offset(pPoonamallee.x + (pPorur.x - pPoonamallee.x) * 0.50f, pPoonamallee.y)
                val pIyyappanthangal = Offset(pPoonamallee.x + (pPorur.x - pPoonamallee.x) * 0.75f, pPoonamallee.y)

                val regularStations = listOf(
                    pWimco, pWasherman, pLic, pGuindy, pAirport,
                    pEgmore, pShenoy, pCmbt, pAshok, pStThomas,
                    pPoonamallee, pPoonamalleeGH, pKumananchavadi, pIyyappanthangal, pPorur
                )
                regularStations.forEach { pos ->
                    drawCircle(color = Color.White, radius = 3.5f, center = pos)
                    drawCircle(color = ChennaiColors.DeepCanvas, radius = 1.8f, center = pos)
                }

                // Future unopened stations (faint dots)
                val futureStations = listOf(pPanagalPark, pRoyapettah, pThirumayilai, pLighthouse)
                futureStations.forEach { pos ->
                    drawCircle(color = Color.White.copy(alpha = 0.35f), radius = 2.5f, center = pos)
                }

                // 5. Interchange Nodes (Central, Alandur, Vadapalani, Thousand Lights)
                val interchanges = listOf(
                    pCentral to "Central",
                    pAlandur to "Alandur",
                    pVadapalani to "Vadapalani",
                    pThousandLights to "Thousand Lights"
                )
                interchanges.forEach { (pos, _) ->
                    drawCircle(color = Color.White, radius = 7.5f, center = pos)
                    drawCircle(color = ChennaiColors.DeepCanvas, radius = 5f, center = pos)
                    drawCircle(color = Color(0xFFFACC15), radius = 2.5f, center = pos)
                }

                // 6. Highlight user origin and destination with animated/styled rings
                originStop?.let {
                    drawCircle(color = ChennaiColors.LiveOnlineGreen.copy(alpha = 0.45f), radius = 16f, center = pCentral)
                    drawCircle(color = ChennaiColors.LiveOnlineGreen, radius = 7f, center = pCentral)
                    drawCircle(color = Color.White, radius = 2.5f, center = pCentral)
                }

                destinationStop?.let {
                    drawCircle(color = ChennaiColors.AlertRed.copy(alpha = 0.45f), radius = 16f, center = pAirport)
                    drawCircle(color = ChennaiColors.AlertRed, radius = 7f, center = pAirport)
                    drawCircle(color = Color.White, radius = 2.5f, center = pAirport)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Legend Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            LegendItem(color = ChennaiColors.MetroBlue, label = "Blue Line (C1)")
            LegendItem(color = ChennaiColors.MetroGreen, label = "Green Line (C2)")
            LegendItem(color = ChennaiColors.MtcAmber, label = "Yellow Line (Open to Vadapalani)")
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color = ChennaiColors.TextSecondary
        )
    }
}
