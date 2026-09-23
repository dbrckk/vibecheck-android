package com.vibecheck.app.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PremiumBillingManager(
    context: Context
) : PurchasesUpdatedListener {

    private val appContext = context.applicationContext

    private val _isPremium = MutableStateFlow(false)
    val isPremium: StateFlow<Boolean> = _isPremium.asStateFlow()

    private val _isPurchaseReady = MutableStateFlow(false)
    val isPurchaseReady: StateFlow<Boolean> = _isPurchaseReady.asStateFlow()

    private val _formattedPrice = MutableStateFlow<String?>(null)
    val formattedPrice: StateFlow<String?> = _formattedPrice.asStateFlow()

    private var productDetails: ProductDetails? = null
    private var selectedOfferToken: String? = null

    private val billingClient = BillingClient.newBuilder(appContext)
        .setListener(this)
        .enablePendingPurchases(
            PendingPurchasesParams.newBuilder()
                .enableOneTimeProducts()
                .build()
        )
        .enableAutoServiceReconnection()
        .build()

    fun start() {
        if (billingClient.isReady) {
            refresh()
            return
        }

        billingClient.startConnection(
            object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        refresh()
                    }
                }

                override fun onBillingServiceDisconnected() {
                    _isPurchaseReady.value = false
                }
            }
        )
    }

    fun launchPurchase(activity: Activity): Boolean {
        if (!billingClient.isReady) {
            _isPurchaseReady.value = false
            start()
            return false
        }
        val details = productDetails ?: return false
        val offerToken = selectedOfferToken ?: return false

        val productParams = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(details)
            .setOfferToken(offerToken)
            .build()

        val flowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(listOf(productParams))
            .build()

        val result = billingClient.launchBillingFlow(activity, flowParams)
        return result.responseCode == BillingClient.BillingResponseCode.OK
    }

    fun close() {
        _isPurchaseReady.value = false
        billingClient.endConnection()
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                if (purchases != null) processPurchases(purchases)
            }
            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> queryExistingPurchases()
        }
    }

    private fun refresh() {
        queryProductDetails()
        queryExistingPurchases()
    }

    private fun queryProductDetails() {
        val product = QueryProductDetailsParams.Product.newBuilder()
            .setProductId(PRODUCT_REMOVE_ADS_LIFETIME)
            .setProductType(BillingClient.ProductType.INAPP)
            .build()

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(listOf(product))
            .build()

        billingClient.queryProductDetailsAsync(params) { billingResult, result ->
            if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
                _isPurchaseReady.value = false
                _formattedPrice.value = null
                return@queryProductDetailsAsync
            }

            val details = result.productDetailsList
                .firstOrNull { it.productId == PRODUCT_REMOVE_ADS_LIFETIME }
            val offer = details
                ?.oneTimePurchaseOfferDetailsList
                ?.firstOrNull()
                ?: details?.oneTimePurchaseOfferDetails

            productDetails = details
            selectedOfferToken = offer?.offerToken
            _formattedPrice.value = offer?.formattedPrice
            _isPurchaseReady.value = details != null && selectedOfferToken != null
        }
    }

    private fun queryExistingPurchases() {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .build()

        billingClient.queryPurchasesAsync(params) { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                processPurchases(purchases)
            }
        }
    }

    private fun processPurchases(purchases: List<Purchase>) {
        val ownedPurchase = purchases.firstOrNull { purchase ->
            purchase.products.contains(PRODUCT_REMOVE_ADS_LIFETIME) &&
                purchase.purchaseState == Purchase.PurchaseState.PURCHASED
        }

        _isPremium.value = ownedPurchase != null

        if (ownedPurchase != null && !ownedPurchase.isAcknowledged) {
            val params = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(ownedPurchase.purchaseToken)
                .build()

            billingClient.acknowledgePurchase(params) {
                // Ownership is already granted from the PURCHASED state.
                // Do not recursively re-query on acknowledgement failure:
                // a later app start/refresh can retry safely without a tight loop.
            }
        }
    }

    companion object {
        const val PRODUCT_REMOVE_ADS_LIFETIME = "remove_ads_lifetime"
    }
}
