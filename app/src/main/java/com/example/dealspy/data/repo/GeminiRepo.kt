package com.example.dealspy.data.repo

import com.example.dealspy.BuildConfig
import com.example.dealspy.data.model.GeminiSearchResponse
import com.example.dealspy.data.model.Product
import com.google.ai.client.generativeai.GenerativeModel
import com.google.gson.Gson
import javax.inject.Inject


class GeminiRepo @Inject constructor() {

    private val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = BuildConfig.API_KEY
    )

    suspend fun fetchProductsFromPrompt(prompt: String): List<Product> {
        return try {
            val response = generativeModel.generateContent(prompt)
            val jsonString = response.text ?: return emptyList()

            val result = Gson().fromJson(jsonString, GeminiSearchResponse::class.java)
            result.product
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun generateSearchSuggestions(productName: String): List<Product> {
        val prompt = """
            Find specific listings for individual $productName products on brand websites, 
            Amazon India, Flipkart, Ajio, Myntra, Meesho, Tata CLiQ, Croma, 
            Blinkit, BigBasket, JioMart, and Instamart. For each product found,
            extract the product name (including any variants),
            platform name, price (in INR), the direct product deep link, 
            and the image URL. Present the results in the following JSON format:
            ```json
            {
                "product": [
                    {
                      "name": "Product Name (Variant)",
                      "platform name": "Platform Name",
                      "price": "Price in INR",
                      "deep link": "Product URL",
                      "image URL": "Image URL"
                    }
                ]
            }
            ```
        """.trimIndent()

        return fetchProductsFromPrompt(prompt)
    }
}
