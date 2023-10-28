package database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [bookInteties::class], version = 1)
  abstract  class dbClass:RoomDatabase() {

      abstract  fun bookDao():BookDao



}