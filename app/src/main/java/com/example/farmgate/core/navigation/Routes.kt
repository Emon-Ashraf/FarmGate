package com.example.farmgate.core.navigation

import com.example.farmgate.data.model.RoleType

object Graph {
    const val ROOT = "root_graph"
    const val AUTH = "auth_graph"
    const val CUSTOMER = "customer_graph"
    const val FARMER = "farmer_graph"
    const val ADMIN = "admin_graph"
}

object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTER = "register"

    const val CUSTOMER_HOME = "customer_home"
    const val CUSTOMER_ORDERS = "customer_orders"
    const val CUSTOMER_PROFILE = "customer_profile"

    const val CUSTOMER_PRODUCT_DETAILS = "customer_product_details/{productId}"
    const val PRODUCT_ID_ARG = "productId"

    fun customerProductDetails(productId: Long): String {
        return "customer_product_details/$productId"
    }

    const val FARMER_DASHBOARD = "farmer_dashboard"
    const val FARMER_ORDERS = "farmer_orders"
    const val FARMER_PRODUCTS = "farmer_products"
    const val FARMER_PROFILE = "farmer_profile"

    const val ADMIN_ISSUES = "admin_issues"

    fun graphForRole(role: RoleType): String {
        return when (role) {
            RoleType.Customer -> Graph.CUSTOMER
            RoleType.Farmer -> Graph.FARMER
            RoleType.Admin -> Graph.ADMIN
        }
    }
}