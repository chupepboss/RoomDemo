
package com.example.roomdemo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ScreenSetup(viewModel: MainViewModel) {
    var productName by remember { mutableStateOf("") }
    var quantityText by remember { mutableStateOf("") }
    var searchName by remember { mutableStateOf("") }
    var deleteName by remember { mutableStateOf("") }

    val products by viewModel.allProducts.observeAsState(emptyList())
    val searchResults by viewModel.searchResults.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Учёт товаров",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = productName,
            onValueChange = { productName = it },
            label = { Text("Название товара") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = quantityText,
            onValueChange = { quantityText = it },
            label = { Text("Количество") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Button(
            onClick = {
                val quantity = quantityText.toIntOrNull()
                if (productName.isNotBlank() && quantity != null && quantity >= 0) {
                    viewModel.insertProduct(
                        Product(productName.trim(), quantity)
                    )
                    productName = ""
                    quantityText = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Добавить товар")
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Поиск товара",
            style = MaterialTheme.typography.titleMedium
        )

        OutlinedTextField(
            value = searchName,
            onValueChange = { searchName = it },
            label = { Text("Название для поиска") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.findProduct(searchName.trim()) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Найти")
            }

            Button(
                onClick = {
                    searchName = ""
                    viewModel.findProduct("")
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Сбросить поиск")
            }
        }

        if (searchName.isNotBlank()) {
            Text(
                text = "Результаты поиска",
                style = MaterialTheme.typography.titleMedium
            )

            if (searchResults.isEmpty()) {
                Text("Ничего не найдено")
            } else {
                LazyColumn(
                    modifier = Modifier.height(120.dp)
                ) {
                    items(searchResults) { product ->
                        ProductRow(product = product)
                    }
                }
            }
        }

        Text(
            text = "Все товары",
            style = MaterialTheme.typography.titleMedium
        )

        if (products.isEmpty()) {
            Text("Список товаров пока пуст")
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(products) { product ->
                    ProductRow(product = product)
                }
            }
        }

        OutlinedTextField(
            value = deleteName,
            onValueChange = { deleteName = it },
            label = { Text("Название для удаления") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Button(
            onClick = {
                if (deleteName.isNotBlank()) {
                    viewModel.deleteProduct(deleteName.trim())
                    deleteName = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Удалить товар")
        }
    }
}

@Composable
fun ProductRow(product: Product) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = product.productName,
                style = MaterialTheme.typography.titleMedium
            )
            Text("Количество: ${product.quantity}")
        }
    }
}
