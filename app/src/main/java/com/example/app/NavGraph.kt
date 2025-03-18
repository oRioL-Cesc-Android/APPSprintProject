package com.example.app


import TripDetailsScreen
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.app.ui.screens.AboutScreen
import com.example.app.ui.screens.ExploreScreen
import com.example.app.ui.screens.HomeScreen
import com.example.app.ui.screens.LoginScreen
import com.example.app.ui.screens.MapScreen
import com.example.app.ui.screens.SettingsScreen
import com.example.app.ui.screens.TermsCondScreen
import com.example.app.ui.screens.TravelListScreen
import com.example.app.ui.screens.VersionScreen
//import com.example.localpreferences.ui.view.SettingsScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
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



    }
}
