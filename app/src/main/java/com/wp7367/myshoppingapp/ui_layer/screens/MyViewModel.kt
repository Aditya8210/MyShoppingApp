package com.wp7367.myshoppingapp.ui_layer.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wp7367.myshoppingapp.common.ResultState
import com.wp7367.myshoppingapp.data_layer.useCase.GetAllCategoryUseCase
import com.wp7367.myshoppingapp.data_layer.useCase.GetAllProductUseCase
import com.wp7367.myshoppingapp.data_layer.useCase.GetUserByIdUseCase
import com.wp7367.myshoppingapp.data_layer.useCase.LoginUserWithEmailPassUseCase
import com.wp7367.myshoppingapp.data_layer.useCase.RegisterUserWithEmailPassUseCase
import com.wp7367.myshoppingapp.data_layer.useCase.UpdateUserByIdUseCase
import com.wp7367.myshoppingapp.domain_layer.models.category
import com.wp7367.myshoppingapp.domain_layer.models.productsModel
import com.wp7367.myshoppingapp.domain_layer.models.userData
import dagger.hilt.android.lifecycle.HiltViewModel
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


    init {
        getAllCategory()
        getAllProduct()
        getUserData("uid")
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

    fun updateUser(userData: userData)
    {

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
    val data: List<productsModel?> = emptyList()
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

