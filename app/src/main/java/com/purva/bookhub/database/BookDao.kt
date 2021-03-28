package com.purva.bookhub.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BookDao {

    @Insert
    fun insertBook(bookEntity: BookEntity)

    @Delete
    fun deleteBook(bookEntity: BookEntity)

    @Query("SELECT * FROM books")
    fun getAllBooks():List<BookEntity>                                                             //to add & display every book in the favourites window

    @Query("SELECT * FROM books WHERE book_Id = :bookId ")                      //checking for a particular book
    fun getBookById(bookId:String):BookEntity

}
//in functions ko DescripActiv me use krenge