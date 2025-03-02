package com.example.app


import TripDetailsScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.app.ui.screens.AboutScreen
import com.example.app.ui.screens.ExploreScreen
import com.example.app.ui.screens.HomeScreen
import com.example.app.ui.screens.LoginScreen
import com.example.app.ui.screens.MapScreen
import com.example.app.ui.screens.TermsCondScreen
import com.example.app.ui.screens.TravelListScreen
import com.example.app.ui.screens.VersionScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("login") { LoginScreen(navController) }
        composable("about") { AboutScreen(navController) }
        composable("version") { VersionScreen(navController) }
        composable("home") {  HomeScreen(navController) }
        composable("Travel List") {  TravelListScreen(navController) }
        composable("Trip Details Screen") {  TripDetailsScreen(navController) }
        composable("Map Screen") {  MapScreen(navController) }
        composable("ExploreScreen") {  ExploreScreen(navController) }
        composable("TermsCondScreen") {  TermsCondScreen(navController) }



    }
}