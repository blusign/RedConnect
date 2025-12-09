package com.yareu.redconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yareu.redconnect.navigation.Screen
import com.yareu.redconnect.ui.screens.OnboardingScreen
import com.yareu.redconnect.ui.theme.RedConnectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RedConnectTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Screen.Onboarding.route
                ) {
                    // Onboarding Screen
                    composable(Screen.Onboarding.route) {
                        OnboardingScreen(
                            onNavigateToNext = {
                                navController.navigate(Screen.RoleSelector.route) {
                                    popUpTo(Screen.Onboarding.route) { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}