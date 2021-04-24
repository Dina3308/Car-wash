package ru.kpfu.itis.carwash.workManager.di

import androidx.work.ListenableWorker
import androidx.work.Worker
import dagger.MapKey
import kotlin.reflect.KClass

@MapKey
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class WorkerKey(val value: KClass<out ListenableWorker>)