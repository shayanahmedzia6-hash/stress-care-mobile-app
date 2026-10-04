package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.MainTab

data class NavItem(
    val tab: MainTab,
    val label: String,
    val icon: ImageVector,
    val testTag: String
)

val NAV_ITEMS = listOf(
    NavItem(MainTab.DASHBOARD, "Home", Icons.Default.Home, "nav_tab_dashboard"),
    NavItem(MainTab.MONITORING, "Monitoring", Icons.Default.MonitorHeart, "nav_tab_monitoring"),
    NavItem(MainTab.ANALYTICS, "History", Icons.Default.History, "nav_tab_analytics"),
    NavItem(MainTab.INTERVENTIONS, "Interventions", Icons.Default.Spa, "nav_tab_interventions"),
    NavItem(MainTab.PROFILE, "Profile", Icons.Default.Person, "nav_tab_profile")
)

@Composable
fun BottomNavBar(
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor = MaterialTheme.colorScheme.surface
    val activeColor = MaterialTheme.colorScheme.primary
    val inactiveColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
    val borderColor = MaterialTheme.colorScheme.outlineVariant

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
        color = containerColor,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NAV_ITEMS.forEach { item ->
                val isSelected = currentTab == item.tab
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onTabSelected(item.tab) }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                        .testTag(item.testTag)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) activeColor.copy(alpha = 0.12f) else Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = if (isSelected) activeColor else inactiveColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isSelected) activeColor else inactiveColor,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 10.5.sp
                    )
                }
            }
        }
    }
}
