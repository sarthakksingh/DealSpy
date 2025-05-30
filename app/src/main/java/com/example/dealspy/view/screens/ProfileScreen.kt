package com.example.dealspy.view.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.dealspy.ui.theme.DealSpyTheme
import com.example.dealspy.view.components.WishlistCard


@Composable
fun ProfileScreen(navController: NavHostController) {
    DealSpyTheme {
        val backgroundColor = Color(0xFFFEEFEF)
        val cardColor = Color(0xFFFACFCF)
        val buttonColor = Color(0xFFFCD9A8)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(16.dp)
        ) {
            Text(text = "Profile", fontSize = 20.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(16.dp))

            // Profile Image
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .align(Alignment.CenterHorizontally),

                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "TANISHI BISEN",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                "+91 _______",
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(12.dp))
            /*Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(buttonColor),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Edit Profile", color = Color.Black)
        }*/

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Wishlist", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = "More Options",
                    tint = Color.Gray,
                    modifier = Modifier.rotate(90f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Wishlist Items
            repeat(3) {
                WishlistCard(cardColor = cardColor)
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.weight(1f))


        }
    }
}

