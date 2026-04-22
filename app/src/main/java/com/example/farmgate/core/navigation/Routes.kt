package com.example.farmgate.core.navigation

import android.net.Uri
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
    const val WELCOME = "welcome"
    const val LOGIN = "login"
    const val REGISTER = "register"

    const val CUSTOMER_HOME = "customer_home"
    const val CUSTOMER_ORDERS = "customer_orders"
    const val CUSTOMER_PROFILE = "customer_profile"
    const val CUSTOMER_REVIEW_ORDER = "customer_review_order"

    const val PRODUCT_ID_ARG = "productId"
    const val ORDER_ID_ARG = "orderId"

    const val CUSTOMER_PRODUCT_DETAILS = "customer_product_details/{$PRODUCT_ID_ARG}"
    const val CUSTOMER_ORDER_DETAILS = "customer_order_details/{$ORDER_ID_ARG}"
    const val CUSTOMER_CREATE_RATING = "customer_create_rating/{$ORDER_ID_ARG}"
    const val CUSTOMER_CREATE_ISSUE = "customer_create_issue/{$ORDER_ID_ARG}"

    fun customerProductDetails(productId: Long): String {
        return "customer_product_details/$productId"
    }

    fun customerOrderDetails(orderId: Long): String {
        return "customer_order_details/$orderId"
    }

    fun customerCreateRating(orderId: Long): String {
        return "customer_create_rating/$orderId"
    }

    fun customerCreateIssue(orderId: Long): String {
        return "customer_create_issue/$orderId"
    }

    const val FARMER_DASHBOARD = "farmer_dashboard"
    const val FARMER_ORDERS = "farmer_orders"
    const val FARMER_PRODUCTS = "farmer_products"
    const val FARMER_PROFILE = "farmer_profile"
    const val FARMER_ORDER_DETAILS = "farmer_order_details/{$ORDER_ID_ARG}"

    fun farmerOrderDetails(orderId: Long): String {
        return "farmer_order_details/$orderId"
    }

    const val ADMIN_ISSUES = "admin_issues"
    const val ADMIN_USER_MODERATION = "admin_user_moderation"
    const val ADMIN_PRODUCT_MODERATION = "admin_product_moderation"

    const val ISSUE_ID_ARG = "issueId"
    const val TITLE_ARG = "title"
    const val ADMIN_STATUS_ARG = "status"
    const val CUSTOMER_NAME_ARG = "customerName"
    const val FARMER_NAME_ARG = "farmerName"
    const val CREATED_AT_ARG = "createdAt"

    const val ADMIN_ISSUE_DETAILS =
        "admin_issue_details/{$ISSUE_ID_ARG}/{$ORDER_ID_ARG}/{$TITLE_ARG}/{$ADMIN_STATUS_ARG}/{$CUSTOMER_NAME_ARG}/{$FARMER_NAME_ARG}/{$CREATED_AT_ARG}"

    fun adminIssueDetails(
        issueId: Long,
        orderId: Long,
        title: String,
        status: String,
        customerName: String,
        farmerName: String,
        createdAt: String
    ): String {
        return "admin_issue_details/" +
                "$issueId/" +
                "$orderId/" +
                "${Uri.encode(title)}/" +
                "${Uri.encode(status)}/" +
                "${Uri.encode(customerName)}/" +
                "${Uri.encode(farmerName)}/" +
                "${Uri.encode(createdAt)}"
    }

    fun graphForRole(role: RoleType): String {
        return when (role) {
            RoleType.Customer -> Graph.CUSTOMER
            RoleType.Farmer -> Graph.FARMER
            RoleType.Admin -> Graph.ADMIN
        }
    }
}