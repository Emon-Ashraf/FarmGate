package com.example.farmgate.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.navArgument
import com.example.farmgate.FarmGateApplication
import com.example.farmgate.presentation.auth.login.LoginScreen
import com.example.farmgate.presentation.auth.login.LoginViewModel
import com.example.farmgate.presentation.auth.register.RegisterScreen
import com.example.farmgate.presentation.auth.register.RegisterViewModel
import com.example.farmgate.presentation.auth.splash.SplashScreen
import com.example.farmgate.presentation.auth.splash.SplashViewModel
import com.example.farmgate.presentation.customer.home.CustomerHomeScreen
import com.example.farmgate.presentation.customer.home.CustomerHomeViewModel
import com.example.farmgate.presentation.customer.issue.CreateIssueScreen
import com.example.farmgate.presentation.customer.issue.CreateIssueViewModel
import com.example.farmgate.presentation.customer.order.CustomerOrdersScreen
import com.example.farmgate.presentation.customer.order.CustomerOrdersViewModel
import com.example.farmgate.presentation.customer.order.OrderDetailsScreen
import com.example.farmgate.presentation.customer.order.OrderDetailsViewModel
import com.example.farmgate.presentation.customer.order.ReviewOrderScreen
import com.example.farmgate.presentation.customer.order.ReviewOrderViewModel
import com.example.farmgate.presentation.customer.productdetails.ProductDetailsScreen
import com.example.farmgate.presentation.customer.productdetails.ProductDetailsViewModel
import com.example.farmgate.presentation.customer.rating.CreateRatingScreen
import com.example.farmgate.presentation.customer.rating.CreateRatingViewModel
import com.example.farmgate.presentation.farmer.order.FarmerOrderDetailsScreen
import com.example.farmgate.presentation.farmer.order.FarmerOrderDetailsViewModel
import com.example.farmgate.presentation.farmer.order.FarmerOrdersScreen
import com.example.farmgate.presentation.farmer.order.FarmerOrdersViewModel

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
                val viewModel: CustomerHomeViewModel = viewModel(
                    factory = CustomerHomeViewModel.Factory(
                        profileRepository = appContainer.profileRepository,
                        cityRepository = appContainer.cityRepository,
                        productRepository = appContainer.productRepository,
                        orderDraftRepository = appContainer.orderDraftRepository
                    )
                )
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                CustomerHomeScreen(
                    uiState = uiState,
                    onCitySelected = viewModel::onCitySelected,
                    onRetry = viewModel::onRetry,
                    onProductClick = { productId ->
                        navController.navigate(Routes.customerProductDetails(productId))
                    },
                    onReviewOrderClick = {
                        navController.navigate(Routes.CUSTOMER_REVIEW_ORDER)
                    },
                    onMyOrdersClick = {
                        navController.navigate(Routes.CUSTOMER_ORDERS)
                    }
                )
            }

            composable(
                route = Routes.CUSTOMER_PRODUCT_DETAILS,
                arguments = listOf(
                    navArgument(Routes.PRODUCT_ID_ARG) {
                        type = NavType.LongType
                    }
                )
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getLong(Routes.PRODUCT_ID_ARG) ?: 0L

                val viewModel: ProductDetailsViewModel = viewModel(
                    factory = ProductDetailsViewModel.Factory(
                        productRepository = appContainer.productRepository,
                        orderDraftRepository = appContainer.orderDraftRepository,
                        productId = productId
                    )
                )
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                ProductDetailsScreen(
                    uiState = uiState,
                    onBackClick = { navController.popBackStack() },
                    onRetry = viewModel::loadProduct,
                    onOrderQuantityChanged = viewModel::onOrderQuantityChanged,
                    onAddToOrderClick = viewModel::addToOrder,
                    onReviewOrderClick = viewModel::openReviewOrder,
                    onNavigation = {
                        viewModel.navigation.collect { route ->
                            navController.navigate(route)
                        }
                    }
                )
            }

            composable(Routes.CUSTOMER_REVIEW_ORDER) {
                val viewModel: ReviewOrderViewModel = viewModel(
                    factory = ReviewOrderViewModel.Factory(
                        orderDraftRepository = appContainer.orderDraftRepository,
                        orderRepository = appContainer.orderRepository
                    )
                )
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                ReviewOrderScreen(
                    uiState = uiState,
                    onBackClick = { navController.popBackStack() },
                    onQuantityChanged = viewModel::updateQuantity,
                    onRemoveItem = viewModel::removeItem,
                    onClearDraft = viewModel::clearDraft,
                    onSubmitOrder = viewModel::submitOrder,
                    onNavigation = {
                        viewModel.navigation.collect { route ->
                            navController.navigate(route) {
                                popUpTo(Routes.CUSTOMER_REVIEW_ORDER) { inclusive = true }
                            }
                        }
                    }
                )
            }

            composable(Routes.CUSTOMER_ORDERS) {
                val viewModel: CustomerOrdersViewModel = viewModel(
                    factory = CustomerOrdersViewModel.Factory(
                        orderRepository = appContainer.orderRepository
                    )
                )
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                CustomerOrdersScreen(
                    uiState = uiState,
                    onBackClick = { navController.popBackStack() },
                    onRetry = viewModel::loadOrders,
                    onOrderClick = { orderId ->
                        navController.navigate(Routes.customerOrderDetails(orderId))
                    }
                )
            }

            composable(
                route = Routes.CUSTOMER_ORDER_DETAILS,
                arguments = listOf(
                    navArgument(Routes.ORDER_ID_ARG) {
                        type = NavType.LongType
                    }
                )
            ) { backStackEntry ->
                val orderId = backStackEntry.arguments?.getLong(Routes.ORDER_ID_ARG) ?: 0L

                val viewModel: OrderDetailsViewModel = viewModel(
                    factory = OrderDetailsViewModel.Factory(
                        orderRepository = appContainer.orderRepository,
                        orderId = orderId
                    )
                )
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                OrderDetailsScreen(
                    uiState = uiState,
                    onBackClick = { navController.popBackStack() },
                    onRetry = viewModel::loadOrder,
                    onCancelNoteChanged = viewModel::onCancelNoteChanged,
                    onPaymentReferenceChanged = viewModel::onPaymentReferenceChanged,
                    onCancelOrderClick = viewModel::cancelOrder,
                    onConfirmFeeClick = viewModel::confirmServiceFee,
                    onRateFarmerClick = { selectedOrderId ->
                        navController.navigate(Routes.customerCreateRating(selectedOrderId))
                    },
                    onReportIssueClick = { selectedOrderId ->
                        navController.navigate(Routes.customerCreateIssue(selectedOrderId))
                    }
                )
            }

            composable(
                route = Routes.CUSTOMER_CREATE_RATING,
                arguments = listOf(
                    navArgument(Routes.ORDER_ID_ARG) {
                        type = NavType.LongType
                    }
                )
            ) { backStackEntry ->
                val orderId = backStackEntry.arguments?.getLong(Routes.ORDER_ID_ARG) ?: 0L

                val viewModel: CreateRatingViewModel = viewModel(
                    factory = CreateRatingViewModel.Factory(
                        ratingRepository = appContainer.ratingRepository,
                        orderId = orderId
                    )
                )
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(Unit) {
                    viewModel.navigation.collect {
                        navController.popBackStack()
                    }
                }

                CreateRatingScreen(
                    uiState = uiState,
                    onBackClick = { navController.popBackStack() },
                    onScoreChanged = viewModel::onScoreChanged,
                    onCommentChanged = viewModel::onCommentChanged,
                    onSubmitClick = viewModel::submit
                )
            }

            composable(
                route = Routes.CUSTOMER_CREATE_ISSUE,
                arguments = listOf(
                    navArgument(Routes.ORDER_ID_ARG) {
                        type = NavType.LongType
                    }
                )
            ) { backStackEntry ->
                val orderId = backStackEntry.arguments?.getLong(Routes.ORDER_ID_ARG) ?: 0L

                val viewModel: CreateIssueViewModel = viewModel(
                    factory = CreateIssueViewModel.Factory(
                        issueRepository = appContainer.issueRepository,
                        orderId = orderId
                    )
                )
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(Unit) {
                    viewModel.navigation.collect {
                        navController.popBackStack()
                    }
                }

                CreateIssueScreen(
                    uiState = uiState,
                    onBackClick = { navController.popBackStack() },
                    onTitleChanged = viewModel::onTitleChanged,
                    onDescriptionChanged = viewModel::onDescriptionChanged,
                    onSubmitClick = viewModel::submit
                )
            }

            composable(Routes.CUSTOMER_PROFILE) {
                PlaceholderScreen(title = "Customer Profile")
            }
        }

        navigation(
            startDestination = Routes.FARMER_ORDERS,
            route = Graph.FARMER
        ) {
            composable(Routes.FARMER_DASHBOARD) {
                PlaceholderScreen(title = "Farmer Dashboard")
            }

            composable(Routes.FARMER_ORDERS) {
                val viewModel: FarmerOrdersViewModel = viewModel(
                    factory = FarmerOrdersViewModel.Factory(
                        orderRepository = appContainer.orderRepository
                    )
                )
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                FarmerOrdersScreen(
                    uiState = uiState,
                    onBackClick = { navController.popBackStack() },
                    onRetry = viewModel::loadOrders,
                    onOrderClick = { orderId ->
                        navController.navigate(Routes.farmerOrderDetails(orderId))
                    }
                )
            }

            composable(
                route = Routes.FARMER_ORDER_DETAILS,
                arguments = listOf(
                    navArgument(Routes.ORDER_ID_ARG) {
                        type = NavType.LongType
                    }
                )
            ) { backStackEntry ->
                val orderId = backStackEntry.arguments?.getLong(Routes.ORDER_ID_ARG) ?: 0L

                val viewModel: FarmerOrderDetailsViewModel = viewModel(
                    factory = FarmerOrderDetailsViewModel.Factory(
                        orderRepository = appContainer.orderRepository,
                        orderId = orderId
                    )
                )
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                FarmerOrderDetailsScreen(
                    uiState = uiState,
                    onBackClick = { navController.popBackStack() },
                    onRetry = viewModel::loadOrder,
                    onAcceptClick = viewModel::acceptOrder,
                    onRejectClick = viewModel::rejectOrder,
                    onPickupCodeChanged = viewModel::onPickupCodeChanged,
                    onFulfilledQuantityChanged = viewModel::onFulfilledQuantityChanged,
                    onCompleteClick = viewModel::completeOrder
                )
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