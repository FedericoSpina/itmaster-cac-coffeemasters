package app.itmaster.mobile.coffeemasters.data

import android.content.ClipData.Item
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DataManager: ViewModel() {
    var menu: List<Category> by mutableStateOf(listOf())
    var cart: List<ItemInCart> by mutableStateOf(listOf())

    init {
        fetchData()
    }

    fun fetchData() {
        // Ejecuta el getMenu en una corutina (algo así como un thread)
        viewModelScope.launch {
            menu = API.menuService.getMenu()
        }
    }

    fun cartAdd(product: Product) {
        val existingItem = cart.find { it.product == product }
        if (existingItem != null) {
            // Product is already in the cart, update quantity
            cart = cart.map {
                if (it.product == product) it.copy(quantity = it.quantity + 1)
                else it
            }
        } else {
            // Product is not in the cart, add a new item
            cart = cart + ItemInCart(product, 1)
        }
        println(cart)
    }


    fun cartRemove(product: Product) {
        val existingItem = cart.find { it.product == product }
        if (existingItem != null) {
            // Product is in the cart, decrement quantity or remove if quantity is 1
            if (existingItem.quantity > 1) {
                cart = cart.map {
                    if (it.product == product) it.copy(quantity = it.quantity - 1)
                    else it
                }
            } else {
                // Remove the item from the cart
                cart = cart.filterNot { it.product == product }
            }
        }
        println(cart)
    }


    fun clearCart() {
        cart = emptyList()
        println(cart)
    }

}