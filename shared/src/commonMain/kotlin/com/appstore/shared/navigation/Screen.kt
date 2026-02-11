
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {

    @Serializable
    data object Auth : Screen()

    @Serializable
    data object HomeGraph : Screen()

    @Serializable
    data object ProductList : Screen()

    @Serializable
    data class ProductDetail(val productId: Int) : Screen()

    @Serializable
    data class AddEditProduct(val productId: Int?) : Screen()


}