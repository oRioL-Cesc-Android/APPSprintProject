package com.TravelPlanner


import TripDetailsScreen
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.TravelPlanner.ui.view.AboutScreen
import com.TravelPlanner.ui.view.ForgotPasswordScreen
import com.TravelPlanner.ui.view.HomeScreen
import com.TravelPlanner.ui.view.LoginScreen
import com.TravelPlanner.ui.view.MapScreen
import com.TravelPlanner.ui.view.RegisterScreen
import com.TravelPlanner.ui.view.SettingsScreen
import com.TravelPlanner.ui.view.TermsCondScreen
import com.TravelPlanner.ui.view.TravelListScreen
import com.TravelPlanner.ui.view.VersionScreen
import com.google.firebase.auth.FirebaseAuth
import com.TravelPlanner.ui.view.ExploreScreen


@Composable
fun NavGraph(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    NavHost(navController = navController, startDestination = if (auth.currentUser != null) "home" else "login") {
        composable("login") { LoginScreen(navController) }
        composable("about") { AboutScreen(navController) }
        composable("version") { VersionScreen(navController) }
        composable("home") {  HomeScreen(navController) }
        composable("Travel List") {  TravelListScreen(navController, hiltViewModel()) }
        composable("Trip Details Screen") {  TripDetailsScreen(navController) }
        composable("Map Screen") {  MapScreen(navController) }
        composable("ExploreScreen") {  ExploreScreen(navController) }
        composable("TermsCondScreen") {  TermsCondScreen(navController) }
        composable("SettingsScreen") { SettingsScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("forgot_password") { ForgotPasswordScreen(navController)}


    }
}
