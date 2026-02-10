
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {

    @Serializable
    data object Auth : Screen()

    @Serializable
    data object HomeGraph : Screen()

    @Serializable
    data object ProductOverview : Screen()

    @Serializable
    data object Cart : Screen()

    @Serializable
    data object Categories : Screen()

    @Serializable
    data object Profile : Screen()



    @Serializable
    data class ManageProduct(
        val id: String? = null
    ) : Screen()

    @Serializable
    data class Details(
        val id: String
    ) : Screen()



    @Serializable
    data class Checkout(
        val totalAmount: String
    ) : Screen()




}