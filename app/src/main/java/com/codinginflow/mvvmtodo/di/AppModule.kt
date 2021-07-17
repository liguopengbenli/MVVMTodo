package com.codinginflow.mvvmtodo.di

import android.app.Application
import androidx.room.Room
import com.codinginflow.mvvmtodo.data.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class) // auto generate
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        app: Application, //new version dagger konw how to create this
        callback: TaskDatabase.Callback
    ) = Room.databaseBuilder(app, TaskDatabase::class.java, "task_database")
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()

    @Provides
    fun provideTaskDao(db: TaskDatabase) = db.taskDao()

    // create coroutine scope with dagger
    // supervisor job when one of the child failed keep others running
    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())

}

// distinguish our custom coroutine scope
@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope