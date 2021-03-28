package com.purva.bookhub.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BookEntity::class],version = 1)           // to tell that this class is being used as the database to this project
abstract class BookDatabase:RoomDatabase() {

    abstract fun bookDao():BookDao                                      //all functions v'll perform on the data will be performed by DAO

}