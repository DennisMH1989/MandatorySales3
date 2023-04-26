package com.example.mandatorysales.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mandatorysales.repository.SalesItemsRepository

class SalesViewModel : ViewModel() {
    private val repository = SalesItemsRepository()
    val salesItemsLiveData: LiveData<List<SalesItem>> = repository.salesItemLiveData
    val errorMessageLiveData: LiveData<String> = repository.errorMessageLiveData
    val updateMessageLiveData: LiveData<String> = repository.updateMessageLiveData

    init {
        reload()
    }

    fun reload() {
        repository.getSalesItems()
    }

    operator fun get(index: Int): SalesItem? {
        return salesItemsLiveData.value?.get(index)
    }

    fun add(salesItem: SalesItem) {
        repository.add(salesItem)
    }

    fun delete(id: Int) {
        repository.delete(id)
    }

    fun update(salesItem: SalesItem) {
        repository.update(salesItem)
    }

    fun sortByDescription() {
        repository.sortByDescription()
    }

    fun sortByDescriptionDescending() {
        repository.sortByDescriptionDescending()
    }

    fun sortByPrice() {
        repository.sortByPrice()
    }

    fun sortByPriceDescending() {
        repository.sortByPriceDescending()
    }

    fun filterByDescription(description: String) {
        repository.filterByDescription(description)
    }
}