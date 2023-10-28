package database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface BookDao {
    @Insert
    fun insertBook(bookInteties: bookInteties)
    @Delete
    fun deleteBook(bookInteties: bookInteties)
    @Query("SELECT * FROM books ")
    fun getAllBooks():List<bookInteties>

    @Query("SELECT * FROM books WHERE book_id = :bookId")
    fun getBookById(bookId:String):bookInteties






}