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
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.navArgument
import com.example.farmgate.FarmGateApplication
import com.example.farmgate.data.model.IssueStatus
import com.example.farmgate.presentation.admin.issue.AdminIssueDetailsScreen
import com.example.farmgate.presentation.admin.issue.AdminIssueDetailsViewModel
import com.example.farmgate.presentation.admin.issue.AdminIssuesScreen
import com.example.farmgate.presentation.admin.issue.AdminIssuesViewModel
import com.example.farmgate.presentation.admin.product.AdminProductModerationScreen
import com.example.farmgate.presentation.admin.product.AdminProductModerationViewModel
import com.example.farmgate.presentation.admin.user.AdminUserModerationScreen
import com.example.farmgate.presentation.admin.user.AdminUserModerationViewModel
import com.example.farmgate.presentation.auth.login.LoginScreen
import com.example.farmgate.presentation.auth.login.LoginViewModel
import com.example.farmgate.presentation.auth.register.RegisterScreen
import com.example.farmgate.presentation.auth.register.RegisterViewModel
import com.example.farmgate.presentation.auth.splash.SplashScreen
import com.example.farmgate.presentation.auth.splash.SplashViewModel
import com.example.farmgate.presentation.auth.welcome.WelcomeScreen
import com.example.farmgate.presentation.customer.home.CustomerHomeScreen
import com.example.farmgate.presentation.customer.home.CustomerHomeViewModel
import com.example.farmgate.presentation.customer.issue.CreateIssueScreen
import com.example.farmgate.presentation.customer.issue.CreateIssueViewModel
import com.example.farmgate.presentation.customer.navigation.CustomerMainScreen
import com.example.farmgate.presentation.customer.order.CustomerOrdersScreen
import com.example.farmgate.presentation.customer.order.CustomerOrdersViewModel
import com.example.farmgate.presentation.customer.order.OrderDetailsScreen
import com.example.farmgate.presentation.customer.order.OrderDetailsViewModel
import com.example.farmgate.presentation.customer.order.ReviewOrderScreen
import com.example.farmgate.presentation.customer.order.ReviewOrderViewModel
import com.example.farmgate.presentation.customer.productdetails.ProductDetailsScreen
import com.example.farmgate.presentation.customer.productdetails.ProductDetailsViewModel
import com.example.farmgate.presentation.customer.profile.CustomerProfileScreen
import com.example.farmgate.presentation.customer.profile.CustomerProfileViewModel
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

            composable(Routes.WELCOME) {
                WelcomeScreen(
                    onLoginClick = {
                        navController.navigate(Routes.LOGIN)
                    },
                    onCreateAccountClick = {
                        navController.navigate(Routes.REGISTER)
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
                    onRoleChanged = viewModel::onRoleChanged,
                    onDisplayNameChanged = viewModel::onDisplayNameChanged,
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
            startDestination = Routes.CUSTOMER_MAIN,
            route = Graph.CUSTOMER
        ) {
            composable(Routes.CUSTOMER_MAIN) {
                val customerNavController = rememberNavController()

                CustomerMainScreen(navController = customerNavController) { innerModifier ->
                    NavHost(
                        navController = customerNavController,
                        startDestination = Routes.CUSTOMER_HOME,
                        modifier = innerModifier
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
                                onSearchQueryChanged = viewModel::onSearchQueryChanged,
                                onRetry = viewModel::onRetry,
                                onProductClick = { productId ->
                                    navController.navigate(Routes.customerProductDetails(productId))
                                },
                                onReviewOrderClick = {
                                    navController.navigate(Routes.CUSTOMER_REVIEW_ORDER)
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
                                onBackClick = {},
                                onRetry = viewModel::loadOrders,
                                onOrderClick = { orderId ->
                                    navController.navigate(Routes.customerOrderDetails(orderId))
                                }
                            )
                        }

                        composable(Routes.CUSTOMER_PROFILE) {
                            val viewModel: CustomerProfileViewModel = viewModel(
                                factory = CustomerProfileViewModel.Factory(
                                    profileRepository = appContainer.profileRepository,
                                    authRepository = appContainer.authRepository
                                )
                            )
                            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                            CustomerProfileScreen(
                                uiState = uiState,
                                onRetry = viewModel::loadProfile,
                                onLogoutClick = viewModel::logout,
                                onNavigation = {
                                    viewModel.navigation.collect { route ->
                                        navController.navigate(route) {
                                            popUpTo(Graph.ROOT) { inclusive = true }
                                        }
                                    }
                                }
                            )
                        }


                    }
                }
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
                val viewModel: AdminIssuesViewModel = viewModel(
                    factory = AdminIssuesViewModel.Factory(
                        adminRepository = appContainer.adminRepository
                    )
                )
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                AdminIssuesScreen(
                    uiState = uiState,
                    onRetry = viewModel::loadIssues,
                    onIssueClick = { issue ->
                        navController.navigate(
                            Routes.adminIssueDetails(
                                issueId = issue.id,
                                orderId = issue.orderId,
                                title = issue.title,
                                status = issue.status.name,
                                customerName = issue.customerName,
                                farmerName = issue.farmerName,
                                createdAt = issue.createdAt
                            )
                        )
                    }
                )
            }

            composable(
                route = Routes.ADMIN_ISSUE_DETAILS,
                arguments = listOf(
                    navArgument(Routes.ISSUE_ID_ARG) { type = NavType.LongType },
                    navArgument(Routes.ORDER_ID_ARG) { type = NavType.LongType },
                    navArgument(Routes.TITLE_ARG) { type = NavType.StringType },
                    navArgument(Routes.ADMIN_STATUS_ARG) { type = NavType.StringType },
                    navArgument(Routes.CUSTOMER_NAME_ARG) { type = NavType.StringType },
                    navArgument(Routes.FARMER_NAME_ARG) { type = NavType.StringType },
                    navArgument(Routes.CREATED_AT_ARG) { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val issueId = backStackEntry.arguments?.getLong(Routes.ISSUE_ID_ARG) ?: 0L
                val orderId = backStackEntry.arguments?.getLong(Routes.ORDER_ID_ARG) ?: 0L
                val title = backStackEntry.arguments?.getString(Routes.TITLE_ARG).orEmpty()
                val statusString = backStackEntry.arguments?.getString(Routes.ADMIN_STATUS_ARG).orEmpty()
                val customerName = backStackEntry.arguments?.getString(Routes.CUSTOMER_NAME_ARG).orEmpty()
                val farmerName = backStackEntry.arguments?.getString(Routes.FARMER_NAME_ARG).orEmpty()
                val createdAt = backStackEntry.arguments?.getString(Routes.CREATED_AT_ARG).orEmpty()

                val viewModel: AdminIssueDetailsViewModel = viewModel(
                    factory = AdminIssueDetailsViewModel.Factory(
                        adminRepository = appContainer.adminRepository,
                        issueId = issueId,
                        orderId = orderId,
                        title = title,
                        status = IssueStatus.valueOf(statusString),
                        customerName = customerName,
                        farmerName = farmerName,
                        createdAt = createdAt
                    )
                )
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(Unit) {
                    viewModel.navigation.collect {
                        navController.popBackStack()
                    }
                }

                AdminIssueDetailsScreen(
                    uiState = uiState,
                    onBackClick = { navController.popBackStack() },
                    onStatusChanged = viewModel::onStatusChanged,
                    onAdminNoteChanged = viewModel::onAdminNoteChanged,
                    onSubmitClick = viewModel::submit
                )
            }

            composable(Routes.ADMIN_USER_MODERATION) {
                val viewModel: AdminUserModerationViewModel = viewModel(
                    factory = AdminUserModerationViewModel.Factory(
                        adminRepository = appContainer.adminRepository
                    )
                )
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                AdminUserModerationScreen(
                    uiState = uiState,
                    onDeactivateUser = viewModel::deactivateUser,
                    onActivateUser = viewModel::activateUser
                )
            }

            composable(Routes.ADMIN_PRODUCT_MODERATION) {
                val viewModel: AdminProductModerationViewModel = viewModel(
                    factory = AdminProductModerationViewModel.Factory(
                        adminRepository = appContainer.adminRepository
                    )
                )
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                AdminProductModerationScreen(
                    uiState = uiState,
                    onDeactivateProduct = viewModel::deactivateProduct,
                    onActivateProduct = viewModel::activateProduct
                )
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