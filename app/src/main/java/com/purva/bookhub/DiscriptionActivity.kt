package com.purva.bookhub

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.loader.content.AsyncTaskLoader
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.purva.bookhub.database.BookDatabase
import com.purva.bookhub.database.BookEntity
import com.purva.bookhub.util.ConnectivityManager
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.lang.Exception

@Suppress("DEPRECATION")
class DiscriptionActivity : AppCompatActivity() {

     lateinit var txtbookName:TextView
    lateinit var txtbookAuthor:TextView
    lateinit var txtprice:TextView
    lateinit var txtbookRating:TextView
    lateinit var imgbookImage:ImageView
    lateinit var txtDescription:TextView
    lateinit var btnAddToFav:Button
    lateinit var progressBar:ProgressBar
    lateinit var progressLayout :RelativeLayout
    lateinit var toolbar: Toolbar

    var bookId:String?="100"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discription)

        txtbookName=findViewById(R.id.txtbookName)
        txtbookAuthor=findViewById(R.id.txtbookAuthor)
        txtprice=findViewById(R.id.txtprice)
        txtbookRating=findViewById(R.id.txtbookRating)
        imgbookImage=findViewById(R.id.imgbookImage)
        txtDescription=findViewById(R.id.txtDescription)
        btnAddToFav=findViewById(R.id.btnAddToFav)
        progressBar=findViewById(R.id.progressBar)
        progressBar.visibility=View.VISIBLE
        progressLayout=findViewById(R.id.progressLayout)
        progressLayout.visibility=View.VISIBLE

        toolbar=findViewById(R.id.ToolBar)
        setSupportActionBar(toolbar)
        supportActionBar?.title="Book Details"

        if (intent!=null){                                                                      //receiving bookid via intent
            bookId =intent.getStringExtra("book_id")
        }else{
            finish()
            Toast.makeText(this@DiscriptionActivity, "Some Unexpected Error Occurred", Toast.LENGTH_LONG).show()
        }
        if (bookId == "100"){
            finish()
            Toast.makeText(this@DiscriptionActivity, "Some unexpected error occured", Toast.LENGTH_SHORT).show()
        }

        val queue = Volley.newRequestQueue(this@DiscriptionActivity)
        val url = "http://13.235.250.119/v1/book/get_book/"

        val jsonParams=JSONObject()
        jsonParams.put("book_id",bookId)

        if (ConnectivityManager().checkConnectivity(this@DiscriptionActivity)){
            val jsonObjectRequest=object: JsonObjectRequest(Request.Method.POST,url,jsonParams,Response.Listener {
                try {
                    val success = it.getBoolean("success")
                    if (success){
                        val bookJsonObject = it.getJSONObject("book_data")
                        progressLayout.visibility=View.GONE

                        val bookImageUrl=bookJsonObject.getString("image")                  // object for book image , for passing the url

                        Picasso.get().load(bookJsonObject.getString("image")).error(R.drawable.default_book_cover).into(imgbookImage)
                        txtbookName.text=bookJsonObject.getString("name")
                        txtbookAuthor.text=bookJsonObject.getString("author")
                        txtprice.text=bookJsonObject.getString("price")
                        txtbookRating.text=bookJsonObject.getString("rating")
                        txtDescription.text=bookJsonObject.getString("description")

                        val bookEntity=BookEntity(
                            bookId?.toInt() as Int,                                                     //object of BookEntity class
                            txtbookName.text.toString(),                                                //putting values for properties
                            txtbookAuthor.text.toString(),
                            txtprice.text.toString(),
                            txtbookRating.text.toString(),
                            txtDescription.text.toString(),
                            bookImageUrl
                        )
                        val checkFav=DBAsyncTask(applicationContext,bookEntity,mode = 1).execute()   //val to check for favourites

                        val isFav=checkFav.get()             //get will tell whether the result of background process is TorF,i.e the book is Fav or not

                        if (isFav){         //if Y, then suggest this text & change color of button
                            btnAddToFav.text="Remove From Favourites"
                            val favColor=ContextCompat.getColor(applicationContext,R.color.colorFavourites)
                            btnAddToFav.setBackgroundColor(favColor)

                        }else{
                            btnAddToFav.text="Add To Favourites"
                            val noFavColor=ContextCompat.getColor(applicationContext,R.color.colorPrimary)
                            btnAddToFav.setBackgroundColor(noFavColor)

                        }       //else suggest this text

                        btnAddToFav.setOnClickListener{
                            if (!DBAsyncTask(applicationContext,bookEntity,mode = 1).execute().get()) {

                                val async=DBAsyncTask(applicationContext,bookEntity,mode = 2).execute()
                                val result=async.get()
                                if (result){
                                    Toast.makeText(this@DiscriptionActivity, "Book Added To Favourites", Toast.LENGTH_SHORT).show()

                                    btnAddToFav.text="Remove Form Favourites"
                                    val favColor = ContextCompat.getColor(applicationContext,R.color.colorFavourites)
                                    btnAddToFav.setBackgroundColor(favColor)

                                }else{
                                    Toast.makeText(this@DiscriptionActivity, "Some Error Occurred", Toast.LENGTH_SHORT).show()
                                }
                            }else{
                                val async=DBAsyncTask(applicationContext,bookEntity,mode = 3).execute()
                                val result=async.get()
                                if (result){
                                    Toast.makeText(this@DiscriptionActivity, "Book Removed Form Favourites", Toast.LENGTH_SHORT).show()

                                    btnAddToFav.text="Add To Favourites"
                                    val noFavColor=ContextCompat.getColor(applicationContext,R.color.colorPrimary)
                                    btnAddToFav.setBackgroundColor(noFavColor)
                                }else{
                                    Toast.makeText(this@DiscriptionActivity, "Some Error Occurred", Toast.LENGTH_SHORT).show()
                                }
                            }

                        }




                    }else{
                        Toast.makeText(this@DiscriptionActivity, "Some Error occurred !!", Toast.LENGTH_SHORT).show()
                    }
                }catch (e:Exception){
                    Toast.makeText(this@DiscriptionActivity, "Some Error occurred !!", Toast.LENGTH_SHORT).show()
                }

            },Response.ErrorListener {

                Toast.makeText(this@DiscriptionActivity, "Volley Error  $it!!", Toast.LENGTH_SHORT).show()
            }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers =HashMap<String,String>()
                    headers["Content-type"]="application/json"
                    headers["token"]="91355964be6ad9"
                    return headers
                }
            }
            queue.add(jsonObjectRequest)

        }
        else{
            val dialog= AlertDialog.Builder(this@DiscriptionActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection Not Found")
            dialog.setPositiveButton("Oen Setings"){text,listner->
                val settingIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                finish()
            }
            dialog.setNegativeButton("Cancel"){text,listner->
                ActivityCompat.finishAffinity(this@DiscriptionActivity)
            }
            dialog.create()
            dialog.show()
        }
    }

    class DBAsyncTask(val context: Context,val bookEntity: BookEntity,val mode:Int) : AsyncTask<Void, Void, Boolean>() {

        //1]check
        //2]add
        //3]remove

        val db = Room.databaseBuilder(context,BookDatabase::class.java,"book-db").build()

        override fun doInBackground(vararg p0: Void?): Boolean {
            when(mode){
                1->{
                    val book:BookEntity?=db.bookDao().getBookById(bookEntity.book_Id.toString())
                    db.close()
                    return book!=null

                }
                2->{
                    db.bookDao().insertBook(bookEntity)
                    db.close()
                    return true

                }
                3->{

                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true

                }
            }
            return false
        }
    }
}