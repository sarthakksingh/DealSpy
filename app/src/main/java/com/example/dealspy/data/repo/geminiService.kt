package com.example.dealspy.data.repo

import com.example.dealspy.BuildConfig
import com.example.dealspy.data.model.GeminiSearchResponse
import com.example.dealspy.data.model.Product
import com.google.ai.client.generativeai.GenerativeModel
import com.google.gson.Gson


object GeminiService {
    val apiKey = BuildConfig.API_KEY

    private val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = apiKey
    )

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
                },
                ...
                ]
               }""".trimIndent()
        return try {
            val response = generativeModel.generateContent(prompt)
            val jsonString = response.text ?: return emptyList()

            // Parse JSON into ProductApiResponse
            val result = Gson().fromJson(jsonString, GeminiSearchResponse::class.java)
            result.product  // Return List<Product>
        } catch (e: Exception) {
            emptyList()
        }
    }
}
