package com.yareu.redconnect.ui.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yareu.redconnect.ui.theme.DarkText
import com.yareu.redconnect.ui.theme.Gray
import com.yareu.redconnect.ui.theme.White

@Composable
fun PersonalInfoCard(
    bloodType: String = "A+",
    totalDonations: Int = 8,
    points: Int = 850,
    status: String = "Donor Aktif"
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Row 1: Blood Type and Total Donor
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Golongan Darah", fontSize = 14.sp, color = Gray)
                    Text(
                        text = bloodType,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text("Total Donor", fontSize = 14.sp, color = Gray)
                    Text(
                        text = "$totalDonations kali",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
            Spacer(modifier = Modifier.height(20.dp))

            // Row 2: Points and Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Poin", fontSize = 14.sp, color = Gray)
                    Text(
                        text = "$points poin",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text("Status", fontSize = 14.sp, color = Gray)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "⭐", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = status,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                    }
                }
            }
        }
    }
}