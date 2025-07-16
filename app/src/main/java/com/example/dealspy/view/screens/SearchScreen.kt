package com.example.dealspy.view.screens

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dealspy.data.model.SearchCategory
import com.example.dealspy.ui.state.UiStateHandler
import com.example.dealspy.ui.theme.DealSpyTheme
import com.example.dealspy.view.utils.PopularCategorySection
import com.example.dealspy.view.utils.SearchResultCard
import com.example.dealspy.view.utils.ShimmerSearchResultCard
import com.example.dealspy.vm.SearchViewModel

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    navController: NavController
) {
    DealSpyTheme {
        var query by remember { mutableStateOf("") }
        val searchListState by viewModel.searchList.collectAsState()

        val popularCategories = listOf(
            SearchCategory(
                "Trendy T-Shirts",
                "https://images.unsplash.com/photo-1600185364241-d1641d75d5b1"
            ),
            SearchCategory(
                "Bold Lipsticks",
                "https://images.unsplash.com/photo-1598214886801-617ec3b86a9d"
            ),
            SearchCategory(
                "Hair Care",
                "https://images.unsplash.com/photo-1615461066841-1bb63b0cf72e"
            ),
            SearchCategory(
                "Backpacks",
                "https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f"
            ),
            SearchCategory(
                "Tshirts",
                "https://images.unsplash.com/photo-1584467735871-594d63f4f01b"
            ),
            SearchCategory(
                "Sweatshirts",
                "https://images.unsplash.com/photo-1618354691327-3c0c4b773482"
            ),
            SearchCategory(
                "Moisturisers",
                "https://images.unsplash.com/photo-1616745307545-b90f46bcb8b3"
            ),
            SearchCategory(
                "Cups & Mugs",
                "https://images.unsplash.com/photo-1507914372432-45d43f3b1590"
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = {
                    Text(
                        "Search for products...",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                trailingIcon = { IconButton(modifier = Modifier.padding(8.dp), onClick = {if (query.isNotBlank()) {
                    viewModel.searchProductList(query)
                }}) { Icon(Icons.Default.Search,contentDescription = "Search") } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        if (query.isNotBlank()) {
                            viewModel.searchProductList(query)
                        }
                    }
                )
            )

            Spacer(modifier = Modifier.height(28.dp))

            if (query.isBlank()) {
                PopularCategorySection(
                    categories = popularCategories,
                    onCategoryClick = {
                        query = it
                        viewModel.searchProductList(it)
                    }
                )
            } else {
                UiStateHandler(
                    state = searchListState,
                    modifier = Modifier.fillMaxSize(),
                    onRetry = { viewModel.searchProductList(query) },
                    onSuccess =
                { products ->
                    Column(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "Showing results for \"$query\"",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyColumn {
                            items(products) { product ->
                                ShimmerSearchResultCard(
                                    isLoading = false,
                                    contentAfterLoading = {
                                        SearchResultCard(product = product)
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }
                }
                )
            }
        }
    }
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
@Preview
fun SearchScreenPreview() {
    SearchScreen(navController = rememberNavController())
}
