package com.wp7367.myshoppingapp.ui_layer.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wp7367.myshoppingapp.common.ResultState
import com.wp7367.myshoppingapp.data_layer.useCase.DeleteCartUseCase
import com.wp7367.myshoppingapp.data_layer.useCase.DeleteFavItemUseCase
import com.wp7367.myshoppingapp.data_layer.useCase.GetAllCategoryUseCase
import com.wp7367.myshoppingapp.data_layer.useCase.GetAllProductUseCase
import com.wp7367.myshoppingapp.data_layer.useCase.GetBannerUseCase
import com.wp7367.myshoppingapp.data_layer.useCase.GetCartItemUseCase
import com.wp7367.myshoppingapp.data_layer.useCase.GetFavItemUseCase
import com.wp7367.myshoppingapp.data_layer.useCase.GetProductByIdUseCase
import com.wp7367.myshoppingapp.data_layer.useCase.GetUserByIdUseCase
import com.wp7367.myshoppingapp.data_layer.useCase.LoginUserWithEmailPassUseCase
import com.wp7367.myshoppingapp.data_layer.useCase.RegisterUserWithEmailPassUseCase
import com.wp7367.myshoppingapp.data_layer.useCase.SetCartItemUseCase
import com.wp7367.myshoppingapp.data_layer.useCase.SetFavItemUseCase
import com.wp7367.myshoppingapp.data_layer.useCase.UpdateUserByIdUseCase

import com.wp7367.myshoppingapp.domain_layer.models.category
import com.wp7367.myshoppingapp.domain_layer.models.ProductModel
import com.wp7367.myshoppingapp.domain_layer.models.cartItemModel
import com.wp7367.myshoppingapp.domain_layer.models.favouriteModel
import com.wp7367.myshoppingapp.domain_layer.models.userData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(private val GetAllCategory: GetAllCategoryUseCase,
                                      private val GetAllProduct: GetAllProductUseCase,
                                      private val RegisterUser: RegisterUserWithEmailPassUseCase,
                                      private val LoginUser: LoginUserWithEmailPassUseCase,
                                      private val GetUserData: GetUserByIdUseCase,
                                      private val UpdateUser: UpdateUserByIdUseCase,
                                      private val GetProductById: GetProductByIdUseCase,
                                      private val GetBanner: GetBannerUseCase,
                                      private val SetCartItem: SetCartItemUseCase,
                                      private val GetCartItem: GetCartItemUseCase,
                                      private val DeleteCart: DeleteCartUseCase,
                                      private val SetFavItem: SetFavItemUseCase,
                                      private val GetFavItem: GetFavItemUseCase,
                                      private val DeleteFav: DeleteFavItemUseCase,



                                      ) : ViewModel() {


    //   ~ Here State is Create ~

    private val _getAllCategorySt = MutableStateFlow(getCategoryState())
    val getAllCategory = _getAllCategorySt.asStateFlow()


    private val _getAllProductSt = MutableStateFlow(getProductsState())
    val getAllProduct = _getAllProductSt.asStateFlow()


    private val _registerUserSt = MutableStateFlow(RegisterState())
    val registerUser = _registerUserSt.asStateFlow()

    private val _loginUserSt = MutableStateFlow(LoginState())
    val loginUser = _loginUserSt.asStateFlow()

    private val _getUserDataSt = MutableStateFlow(GetUserDataState())
    val getUserData = _getUserDataSt.asStateFlow()

    private val _updateDataSt = MutableStateFlow(UpdateDataState())
    val updateData = _updateDataSt.asStateFlow()

    private val _getProductByIdSt = MutableStateFlow(GetProductByIdState())
    val getProductById = _getProductByIdSt.asStateFlow()

    private val _getBannerSt = MutableStateFlow(GetBannerState())
    val getBanner = _getBannerSt.asStateFlow()

    private val _setCartItemSt = MutableStateFlow(SetCartItemState())
    val setCartItem = _setCartItemSt.asStateFlow()

    private val _getCartItemSt = MutableStateFlow(GetCartItemState())
    val getCartItem = _getCartItemSt.asStateFlow()

    private val _deleteCartSt = MutableStateFlow(DeleteCartState())
    val deleteCart = _deleteCartSt.asStateFlow()

    private val _setFavItemSt = MutableStateFlow(SetFavItemState())
    val setFavItem = _setFavItemSt.asStateFlow()

    private val _getFavItemSt = MutableStateFlow(GetFavItemState())
    val getFavItem = _getFavItemSt.asStateFlow()

    private val _deleteFavSt = MutableStateFlow(DeleteFavState())
    val deleteFav = _deleteFavSt.asStateFlow()



    init {
        getAllCategory()
        getAllProduct()
        getUserData("uid")
        getBanner()
        getCartItem()
        getFavItem()


    }


//          ~ Here All Function and UseCase is Called ~

    fun getAllProduct() {
        viewModelScope.launch {
            GetAllProduct.getAllProductUseCase().collectLatest {
                when (it) {
                    is ResultState.Loading -> {
                        _getAllProductSt.value = getProductsState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _getAllProductSt.value = getProductsState(data = it.data)
                    }

                    is ResultState.Error -> {
                        _getAllProductSt.value = getProductsState(error = it.exception)
                    }
                }
            }
        }
    }

    fun getAllCategory() {

        viewModelScope.launch {
            GetAllCategory.getAllCategoryUseCase().collectLatest {
                when (it) {
                    is ResultState.Loading -> {
                        _getAllCategorySt.value = getCategoryState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _getAllCategorySt.value = getCategoryState(data = it.data)
                    }

                    is ResultState.Error -> {
                        _getAllCategorySt.value = getCategoryState(error = it.exception)
                    }
                }

            }

        }

    }

    fun getBanner(){
        viewModelScope.launch {
            GetBanner.getBannerUseCase().collectLatest {
                when(it){
                    is ResultState.Loading -> {
                        _getBannerSt.value = GetBannerState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _getBannerSt.value = GetBannerState(data = it.data)
                    }
                    is ResultState.Error -> {
                        _getBannerSt.value = GetBannerState(error = it.exception)

                    }
                }
            }

        }
    }


    fun registerUser(userData: userData) {
        viewModelScope.launch {
            RegisterUser.registerUserWithEmailPassUseCase(userData).collectLatest {
                when (it) {
                    is ResultState.Loading -> {
                        _registerUserSt.value = RegisterState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _registerUserSt.value = RegisterState(data = it.data)
                    }

                    is ResultState.Error -> {
                        _registerUserSt.value = RegisterState(error = it.exception)
                    }

                }
            }
        }


    }


    fun loginUser(email: String, password: String) {

        viewModelScope.launch {
            LoginUser.loginUserWithEmailPassUseCase(email, password).collectLatest {
                when (it) {
                    is ResultState.Loading -> {
                        _loginUserSt.value = LoginState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _loginUserSt.value = LoginState(data = it.data)
                    }

                    is ResultState.Error -> {
                        _loginUserSt.value = LoginState(error = it.exception)
                    }

                }
            }
        }
    }

    fun getUserData(uid: String) {
        viewModelScope.launch {
            GetUserData.getUserByIdUseCase(uid).collectLatest {
                when (it) {
                    is ResultState.Loading -> {
                        _getUserDataSt.value = GetUserDataState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _getUserDataSt.value = GetUserDataState(data = it.data)
                    }

                    is ResultState.Error -> {
                        _getUserDataSt.value = GetUserDataState(error = it.exception)
                    }
                }
            }
        }


    }


    fun updateUser(userData: userData) {

        viewModelScope.launch {
            UpdateUser.updateUserByIdUseCase(userData).collectLatest{
                when(it){
                    is ResultState.Loading -> {
                        _updateDataSt.value = UpdateDataState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _updateDataSt.value = UpdateDataState(data = it.data)
                    }
                    is ResultState.Error -> {
                        _updateDataSt.value = UpdateDataState(error = it.exception)
                    }

                }

            }
        }
    }

    fun getProductById(productId: String) {
        viewModelScope.launch (Dispatchers.IO){
            GetProductById.getProductByIdUseCase(productId).collect  {
                when (it) {
                    is ResultState.Loading -> {
                        _getProductByIdSt.value  = GetProductByIdState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _getProductByIdSt.value = GetProductByIdState(data = it.data)
                    }
                    is ResultState.Error -> {
                        _getProductByIdSt.value = GetProductByIdState(error = it.exception)
                    }


                }
            }
        }
    }

    fun setCartItem(cartItemModel: cartItemModel) {
        viewModelScope.launch {
            SetCartItem.setCartItemUseCase(cartItemModel).collectLatest {
                when (it) {
                    is ResultState.Loading -> {
                        _setCartItemSt.value = SetCartItemState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _setCartItemSt.value = SetCartItemState(data = it.data)
                    }

                    is ResultState.Error -> {
                        _setCartItemSt.value = SetCartItemState(error = it.exception)
                    }

                }
            }
        }
    }

    fun getCartItem(){
        viewModelScope.launch {
            GetCartItem.getCartItemUseCase().collectLatest {
                when(it) {
                    is ResultState.Loading -> {
                        _getCartItemSt.value = GetCartItemState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _getCartItemSt.value = GetCartItemState(data = it.data)
                    }

                    is ResultState.Error -> {
                        _getCartItemSt.value = GetCartItemState(error = it.exception)
                    }
                }

            }

        }
    }


    fun deleteCartItem(cartItemModel: cartItemModel){
        viewModelScope.launch {
            DeleteCart.deleteCartUseCase(cartItemModel).collectLatest {

                when(it){
                    is ResultState.Loading -> {
                        _deleteCartSt.value = DeleteCartState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _deleteCartSt.value = DeleteCartState(data = it.data)
                    }
                    is ResultState.Error -> {
                        _deleteCartSt.value = DeleteCartState(error = it.exception)

                    }
                }
            }
        }
    }

    fun setFavItem(favouriteModel: favouriteModel){
        viewModelScope.launch {
            SetFavItem.setFavItemUseCase(favouriteModel).collectLatest {
                when (it) {
                    is ResultState.Loading -> {
                        _setFavItemSt.value = SetFavItemState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _setFavItemSt.value = SetFavItemState(data = it.data)
                    }

                    is ResultState.Error -> {
                        _setFavItemSt.value = SetFavItemState(error = it.exception)
                    }
                }

            }
        }

    }

    fun getFavItem(){
        viewModelScope.launch {
            GetFavItem.getFavItemUseCase().collectLatest {
                when (it) {
                    is ResultState.Loading -> {
                        _getFavItemSt.value = GetFavItemState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _getFavItemSt.value = GetFavItemState(data = it.data)
                    }

                    is ResultState.Error -> {
                        _getFavItemSt.value = GetFavItemState(error = it.exception)

                    }


                }


            }
        }

    }

    fun deleteFavItem(favouriteModel: favouriteModel){
        viewModelScope.launch {
            DeleteFav.deleteFavItemUseCase(favouriteModel).collectLatest {
                when (it) {
                    is ResultState.Loading -> {
                        _deleteFavSt.value = DeleteFavState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _deleteFavSt.value = DeleteFavState(data = it.data)
                    }

                    is ResultState.Error -> {
                        _deleteFavSt.value = DeleteFavState(error = it.exception)
                    }

                }
            }

        }

    }



}
// _________________________________DATA CLASS_____________________________________

data class getCategoryState(

    val isLoading: Boolean = false,
    val error: String = "",

    // Here Changing
    val data: List<category?> = emptyList()
)

data class getProductsState(
    val isLoading: Boolean = false,
    val error: String = "",
    val data: List<ProductModel?> = emptyList()
)

data class RegisterState(
    val isLoading: Boolean = false,
    val error: String ?= null,
    val data: String? = null
)

data class LoginState(
    val isLoading: Boolean = false,
    val error: String ?= null,
    val data: String? = null
)

data class GetUserDataState(
    val isLoading: Boolean = false,
    val error: String ?= null,
    val data:userData? = null,
    )

data class UpdateDataState(
    val isLoading: Boolean = false,
    val error: String ?= null,
    val data: String? = null
)

    data class GetProductByIdState(
        val isLoading: Boolean = false,
        val error: String ?= null,
        val data:ProductModel? = null
    )

data class GetBannerState(
    val isLoading: Boolean = false,
    val error: String ?= null,
    val data:List<String>? = null
)

data class SetCartItemState(

    val isLoading: Boolean = false,
    val error: String ?= null,
    val data:String? = null,
)

data class GetCartItemState(
    val isLoading: Boolean = false,
    val error: String ?= null,
    val data:List<cartItemModel?> = emptyList()

)

data class DeleteCartState(

    val isLoading: Boolean = false,
    val error: String ?= null,
    val data:String? = null,
)

data class SetFavItemState(
    val isLoading: Boolean = false,
    val error: String ?= null,
    val data:String? = null
)

data class GetFavItemState(
    val isLoading: Boolean = false,
    val error: String ?= null,
    val data:List<favouriteModel?> = emptyList()
)

data class DeleteFavState(

    val isLoading: Boolean = false,
    val error: String ?= null,
    val data:String? = null,
)


