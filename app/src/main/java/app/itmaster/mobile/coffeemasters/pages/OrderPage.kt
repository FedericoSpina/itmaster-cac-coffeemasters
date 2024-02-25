package app.itmaster.mobile.coffeemasters.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import app.itmaster.mobile.coffeemasters.data.DataManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderPage(dataManager: DataManager) {

    var value by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },

        )
    { contentPadding ->

        Column {
            if (dataManager.cart.isNotEmpty()) {
                // Existing Box for displaying items
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(MaterialTheme.colorScheme.primary)
                        .clip(RoundedCornerShape(25.dp)) // You can adjust the corner radius as needed
                ) {
                    Text(
                        text = "ITEMS",
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .padding(20.dp)
                    )

                    LazyColumn(
                        modifier = Modifier
                            .padding(top = 40.dp)
                            .padding(20.dp)
                    ) {
                        items(dataManager.cart.orEmpty()) { cartItem ->
                            val totalPrice = cartItem.quantity * cartItem.product.price

                            // Use string resources for better localization support
                            Column {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    // First two Text elements on one side
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = ("${cartItem.quantity}x"),
                                            color = MaterialTheme.colorScheme.surfaceVariant
                                        )
                                        Text(
                                            text = (cartItem.product.name),
                                            color = MaterialTheme.colorScheme.background
                                        )
                                    }

                                    // Other Text and Image on the other side
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = ("$${totalPrice.toString()}"),
                                            color = MaterialTheme.colorScheme.background
                                        )

                                        // Material Design delete icon as an image
                                        Image(
                                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surfaceVariant),
                                            imageVector = Icons.Outlined.Delete,
                                            contentDescription = "Delete",
                                            modifier = Modifier
                                                .clickable {
                                                    // Handle delete action here
                                                    dataManager.cartRemove(cartItem.product)
                                                }
                                                .size(24.dp)
                                        )
                                    }
                                }

                                // Divider between items
                                Divider(
                                    color = Color.Gray,
                                    thickness = 1.dp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                )
                            }
                        }
                    }

                }



                // TODO: export the box outside of the fun
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(MaterialTheme.colorScheme.primary) // Adjust color as needed
                        .clip(RoundedCornerShape(50.dp)) // You can adjust the corner radius as needed
                        .height(150.dp)
                ) {

                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxSize()
                    ){

                        Text(
                            text = "NAME",
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            //modifier = Modifier.padding(1.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp)) // Add some space between TextField and Text

                        TextField(
                            value = value,
                            onValueChange = { value = it },
                            label = { Text("Enter name") },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Send
                            ),
                            keyboardActions = KeyboardActions(onSend = {
                                // Aquí puedes hacer lo que necesites con el texto enviado

                                // TODO: randomize number

                                 scope.launch {
                                     snackbarHostState.showSnackbar(
                                         message = "Your order number is : " + "5544",
                                         actionLabel = "Undo",
                                         duration = SnackbarDuration.Short
                                     )
                                 }

                                println("Texto enviado: $value")
                                value = ""
                                dataManager.clearCart()
                            }),
                            //textStyle = TextStyle(fontWeight = FontWeight.Bold),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(4.dp))

                        )

                    }

                }

                // fin del box





            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Its empty , add something",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleFilledTextField(dataManager: DataManager) {

    //val scope = rememberCoroutineScope()
    //val snackbarHostState = remember { SnackbarHostState() }

    // The box below for delivering the thing


}


