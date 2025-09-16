import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // Added this missing import
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
// import androidx.compose.material.icons.filled.ArrowBack // Duplicate, kept the automirrored one
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wp7367.myshoppingapp.R // Added R class import


@Preview(showBackground = true)
@Composable
fun ShoppingCartScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Top Bar
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") // Used AutoMirrored
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Shopping Cart",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Continue Shopping",
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Cart Items List
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(cartItems) { item ->
                CartItemRow(item = item)
                Divider(color = Color.LightGray, thickness = 1.dp)
            }
        }

        // Subtotal Section
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Sub Total", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
            Text("Rs: ${cartItems.sumOf { it.price * it.quantity }}",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
        }

        // Checkout Button
        Button(
            onClick = { /* TODO: handle checkout */ },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B8686))
        ) {
            Text("Checkout", color = Color.White)
        }
    }
}

@Composable
fun CartItemRow(item: CartItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = item.image),
            contentDescription = item.name,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(item.name, fontWeight = FontWeight.Bold)
            Text(item.code, color = Color.Gray, style = MaterialTheme.typography.bodySmall)
            Text("Size: ${item.size}", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Color: ", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(item.color)
                )
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            Text("Rs: ${item.price}")
            Text("Qty: ${item.quantity}")
            Text("Rs: ${item.price * item.quantity}", fontWeight = FontWeight.Bold, color = Color.Red)
            IconButton(onClick = { /* TODO remove item */ }) {
                Icon(Icons.Default.Close, contentDescription = "Remove")
            }
        }
    }
}

// Data Model
data class CartItem(
    val name: String,
    val code: String,
    val size: String,
    val color: Color,
    val price: Int,
    val quantity: Int,
    val image: Int // This should be a @DrawableRes Int
)

// Sample Data
val cartItems = listOf(
    CartItem("One Shoulder Linen Dress", "GF1025", "UK10", Color.Red, 5740, 1, image = R.drawable.profile332553),
    CartItem("Embroidered Linen Top", "GF2261", "UK8", Color.Blue, 3000, 1, image = R.drawable.clothes)
)
