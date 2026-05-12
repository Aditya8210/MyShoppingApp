package com.wp7367.myshoppingapp.domain_layer.useCase

import com.wp7367.myshoppingapp.domain_layer.repo.repo
import javax.inject.Inject

class DeleteNotificationUseCase @Inject constructor(private val repo: repo) {
    fun execute(notificationId: String) = repo.deleteNotification(notificationId)
}
