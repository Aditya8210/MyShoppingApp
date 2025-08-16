package com.wp7367.myshoppingapp.ui_layer.di


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.wp7367.myshoppingapp.data_layer.repoimp.RepoImp

import com.wp7367.myshoppingapp.domain_layer.repo.repo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)

object UiModel {


    @Provides

    fun provideRepo(Firestore: FirebaseFirestore,
                    FirebaseAuth: FirebaseAuth): repo{
        return RepoImp(Firestore, FirebaseAuth)

    }

}