package com.example.mandatorysales.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.mandatorysales.models.SalesItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SalesItemsRepository {
    private val url = "https://anbo-salesitems.azurewebsites.net/api/"

    private val salesService: SalesService
    val salesItemLiveData: MutableLiveData<List<SalesItem>> = MutableLiveData<List<SalesItem>>()
    val errorMessageLiveData: MutableLiveData<String> = MutableLiveData()
    val updateMessageLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        val build: Retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create()) // GSON
            .build()
        salesService = build.create(SalesService::class.java)
        getSalesItems()
    }

    fun getSalesItems() {
        salesService.getAllSalesItems().enqueue(object : Callback<List<SalesItem>> {
            override fun onResponse(call: Call<List<SalesItem>>, response: Response<List<SalesItem>>) {
                if (response.isSuccessful) {
                    val b: List<SalesItem>? = response.body()
                    salesItemLiveData.postValue(b!!)
                    errorMessageLiveData.postValue("")
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("APPLE", message)
                }
            }

            override fun onFailure(call: Call<List<SalesItem>>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
                Log.d("APPLE", t.message!!)
            }
        })
    }

    fun add(salesItem: SalesItem) {
        salesService.saveSalesItem(salesItem).enqueue(object : Callback<SalesItem> {
            override fun onResponse(call: Call<SalesItem>, response: Response<SalesItem>) {
                if (response.isSuccessful) {
                    Log.d("APPLE", "Added: " + response.body())
                    updateMessageLiveData.postValue("Added: " + response.body())
                    getSalesItems()
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("APPLE", message)
                }
            }

            override fun onFailure(call: Call<SalesItem>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
                Log.d("APPLE", t.message!!)
            }
        })
    }

    fun delete(id: Int) {
        salesService.deleteSalesItem(id).enqueue(object : Callback<SalesItem> {
            override fun onResponse(call: Call<SalesItem>, response: Response<SalesItem>) {
                if (response.isSuccessful) {
                    Log.d("APPLE", "Updated: " + response.body())
                    updateMessageLiveData.postValue("Deleted: " + response.body())
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("APPLE", message)
                }
            }

            override fun onFailure(call: Call<SalesItem>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
                Log.d("APPLE", t.message!!)
            }
        })
    }

    fun update(salesItem: SalesItem) {
        salesService.updateSalesItem(salesItem.id, salesItem).enqueue(object : Callback<SalesItem> {
            override fun onResponse(call: Call<SalesItem>, response: Response<SalesItem>) {
                if (response.isSuccessful) {
                    Log.d("APPLE", "Updated: " + response.body())
                    updateMessageLiveData.postValue("Updated: " + response.body())
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("APPLE", message)
                }
            }

            override fun onFailure(call: Call<SalesItem>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
                Log.d("APPLE", t.message!!)
            }
        })
    }
    fun sortByDescription() {
        salesItemLiveData.value = salesItemLiveData.value?.sortedBy { it.description }
    }

    fun sortByDescriptionDescending() {
        salesItemLiveData.value = salesItemLiveData.value?.sortedByDescending { it.description }
    }

    fun sortByPrice() {
        salesItemLiveData.value = salesItemLiveData.value?.sortedBy { it.price }
    }

    fun sortByPriceDescending() {
        salesItemLiveData.value = salesItemLiveData.value?.sortedByDescending { it.price }
    }

    fun filterByDescription(description: String) {
        if (description.isBlank()) {
            getSalesItems()
        }else {
            salesItemLiveData.value = salesItemLiveData.value?.filter { salesItem -> salesItem.description.contains(description) }
        }
    }
}