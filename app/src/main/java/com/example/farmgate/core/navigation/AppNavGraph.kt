package com.example.farmgate.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.farmgate.FarmGateApplication
import com.example.farmgate.presentation.auth.login.LoginScreen
import com.example.farmgate.presentation.auth.login.LoginViewModel
import com.example.farmgate.presentation.auth.register.RegisterScreen
import com.example.farmgate.presentation.auth.register.RegisterViewModel
import com.example.farmgate.presentation.auth.splash.SplashScreen
import com.example.farmgate.presentation.auth.splash.SplashViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController
) {
    val context = LocalContext.current
    val appContainer = (context.applicationContext as FarmGateApplication).appContainer

    NavHost(
        navController = navController,
        startDestination = Graph.AUTH,
        route = Graph.ROOT
    ) {
        navigation(
            startDestination = Routes.SPLASH,
            route = Graph.AUTH
        ) {
            composable(Routes.SPLASH) {
                val viewModel: SplashViewModel = viewModel(
                    factory = SplashViewModel.Factory(
                        sessionManager = appContainer.sessionManager,
                        profileRepository = appContainer.profileRepository
                    )
                )
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                SplashScreen(
                    uiState = uiState,
                    onNavigation = {
                        viewModel.navigation.collect { route ->
                            navController.navigate(route) {
                                popUpTo(Graph.ROOT) { inclusive = true }
                            }
                        }
                    }
                )
            }

            composable(Routes.LOGIN) {
                val viewModel: LoginViewModel = viewModel(
                    factory = LoginViewModel.Factory(
                        authRepository = appContainer.authRepository
                    )
                )
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                LoginScreen(
                    uiState = uiState,
                    onLoginChanged = viewModel::onLoginChanged,
                    onPasswordChanged = viewModel::onPasswordChanged,
                    onLoginClick = viewModel::login,
                    onRegisterClick = {
                        navController.navigate(Routes.REGISTER)
                    },
                    onNavigation = {
                        viewModel.navigation.collect { route ->
                            navController.navigate(route) {
                                popUpTo(Graph.AUTH) { inclusive = true }
                            }
                        }
                    }
                )
            }

            composable(Routes.REGISTER) {
                val viewModel: RegisterViewModel = viewModel(
                    factory = RegisterViewModel.Factory(
                        authRepository = appContainer.authRepository
                    )
                )
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                RegisterScreen(
                    uiState = uiState,
                    onFullNameChanged = viewModel::onFullNameChanged,
                    onPhoneNumberChanged = viewModel::onPhoneNumberChanged,
                    onEmailChanged = viewModel::onEmailChanged,
                    onPasswordChanged = viewModel::onPasswordChanged,
                    onRegisterClick = viewModel::register,
                    onBackToLoginClick = {
                        navController.popBackStack()
                    },
                    onNavigation = {
                        viewModel.navigation.collect { route ->
                            navController.navigate(route) {
                                popUpTo(Graph.AUTH) { inclusive = true }
                            }
                        }
                    }
                )
            }
        }

        navigation(
            startDestination = Routes.CUSTOMER_HOME,
            route = Graph.CUSTOMER
        ) {
            composable(Routes.CUSTOMER_HOME) {
                PlaceholderScreen(title = "Customer Home")
            }

            composable(Routes.CUSTOMER_ORDERS) {
                PlaceholderScreen(title = "Customer Orders")
            }

            composable(Routes.CUSTOMER_PROFILE) {
                PlaceholderScreen(title = "Customer Profile")
            }
        }

        navigation(
            startDestination = Routes.FARMER_DASHBOARD,
            route = Graph.FARMER
        ) {
            composable(Routes.FARMER_DASHBOARD) {
                PlaceholderScreen(title = "Farmer Dashboard")
            }

            composable(Routes.FARMER_ORDERS) {
                PlaceholderScreen(title = "Farmer Orders")
            }

            composable(Routes.FARMER_PRODUCTS) {
                PlaceholderScreen(title = "Farmer Products")
            }

            composable(Routes.FARMER_PROFILE) {
                PlaceholderScreen(title = "Farmer Profile")
            }
        }

        navigation(
            startDestination = Routes.ADMIN_ISSUES,
            route = Graph.ADMIN
        ) {
            composable(Routes.ADMIN_ISSUES) {
                PlaceholderScreen(title = "Admin Issues")
            }
        }
    }
}

@Composable
private fun PlaceholderScreen(title: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = title)
    }
}