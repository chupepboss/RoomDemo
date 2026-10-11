
package com.example.roomdemo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val allProducts: LiveData<List<Product>>
    val searchResults: MutableLiveData<List<Product>>

    private val repository: ProductRepository

    init {
        val productDatabase = ProductRoomDatabase.getInstance(application)
        val productDao = productDatabase.productDao()

        repository = ProductRepository(productDao)

        allProducts = repository.allProducts
        searchResults = repository.searchResults
    }

    fun insertProduct(product: Product) {
        repository.insertProduct(product)
    }

    fun findProduct(name: String) {
        repository.findProduct(name)
    }

    fun deleteProduct(name: String) {
        repository.deleteProduct(name)
    }
}
