package com.wp7367.myshoppingapp.ui_layer.di


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.wp7367.myshoppingapp.data_layer.remote.PaymentApiService
import com.wp7367.myshoppingapp.data_layer.repoimp.RepoImp

import com.wp7367.myshoppingapp.domain_layer.repo.repo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)

object UiModel {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://your-render-app-url.onrender.com/") // TODO: PASTE YOUR RENDER URL HERE
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providePaymentApiService(retrofit: Retrofit): PaymentApiService {
        return retrofit.create(PaymentApiService::class.java)
    }


    @Provides
    fun provideRepo(Firestore: FirebaseFirestore,
                    FirebaseAuth: FirebaseAuth,
                    paymentApiService: PaymentApiService): repo{
        return RepoImp(Firestore, FirebaseAuth, paymentApiService)

    }

}