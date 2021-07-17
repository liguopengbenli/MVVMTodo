package com.codinginflow.mvvmtodo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.codinginflow.mvvmtodo.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao

    // separation of concerne on create we execute after db is ready, we inject this callback to create db
    class Callback @Inject constructor(
        private val database: Provider<TaskDatabase>, // lazy way to get later
        @ApplicationScope private val applicationScope: CoroutineScope
    ): RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val dao = database.get().taskDao()

            applicationScope.launch {
               dao.insert(Task("Wash the dishes"))
               dao.insert(Task("Wash the clothe"))
               dao.insert(Task("Wash the shoes", important = true))
               dao.insert(Task("Wash the floor", completed = true))
               dao.insert(Task("Wash the wall"))
            }
        }
    }
}