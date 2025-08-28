package com.example.dealspy.data.repo

import com.example.dealspy.BuildConfig
import com.example.dealspy.data.model.GeminiSearchResponse
import com.example.dealspy.data.model.Product
import com.google.ai.client.generativeai.GenerativeModel
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiService @Inject constructor() {


    private val apiKey = BuildConfig.API_KEY
    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = apiKey
    )

    suspend fun generateSearchSuggestions(productName: String): List<Product> {
        val prompt = """
            Find 5-6 real products for "$productName" from Indian e-commerce sites.
            Return ONLY valid JSON in this exact format, no extra text:
            
            {
              "products": [
                {
                  "name": "Exact Product Name",
                  "platform": "Platform Name", 
                  "price": "₹1,299",
                  "deepLink": "https://example.com/product-link",
                  "imageUrl": "https://example.com/product-image.jpg",
                  "discount": "20% off"
                }
              ]
            }
            
            Ensure:
            - All products are real and available
            - Prices include ₹ symbol
            - Platform names: Amazon, Flipkart, Myntra, Ajio, Croma, etc.
            - Valid image URLs and product links
            - No markdown formatting, just pure JSON
        """.trimIndent()

        return try {
            val response = generativeModel.generateContent(prompt)
            val jsonString = response.text ?: return emptyList()

            // Clean the response to extract only JSON
            val cleanJson = extractJsonFromResponse(jsonString)

            // Parse JSON into GeminiSearchResponse
            val result = Gson().fromJson(cleanJson, GeminiSearchResponse::class.java)
            result.products

        } catch (e: Exception) {
            // Log the error for debugging
            println("Gemini API Error: ${e.message}")
            emptyList()
        }
    }

    private fun extractJsonFromResponse(response: String): String {
        // Remove markdown code blocks and extra formatting
        return response
            //.replace("
            .replace("", "")
            .trim()
            .let { cleanText ->
                // Find JSON object boundaries
                val startIndex = cleanText.indexOf("{")
                val endIndex = cleanText.lastIndexOf("}") + 1
                if (startIndex >= 0 && endIndex > startIndex) {
                    cleanText.substring(startIndex, endIndex)
                } else {
                    cleanText
                }
            }
    }
}
