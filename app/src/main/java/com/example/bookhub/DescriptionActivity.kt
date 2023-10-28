package com.example.bookhub

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import database.bookInteties
import database.dbClass
import org.json.JSONObject
import util.ConnectionManager


lateinit var txtBookName:TextView
lateinit var txtBookAuthor:TextView
lateinit var txtBookPrice:TextView
lateinit var imgBookImage:ImageView
lateinit var txtBookDisc:TextView
lateinit var btnAddToFav:TextView
lateinit var progressBar:ProgressBar
lateinit var progressLayout:RelativeLayout
lateinit var toolBar:androidx.appcompat.widget.Toolbar


var bookId:String? ="100"


class DescriptionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)


        txtBookName = findViewById(R.id.txtBookName)
        txtBookAuthor = findViewById(R.id.txtBookAuthor)
        txtBookPrice = findViewById(R.id.txtBookAuthor)
        imgBookImage = findViewById(R.id.imgBookImage)
        txtBookDisc = findViewById(R.id.txtBookDescription)
        btnAddToFav = findViewById(R.id.btnAddToFav)
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE


        toolBar = findViewById(R.id.toolBar)
        setSupportActionBar(toolBar)
        supportActionBar?.title = "Book Details "


        if (intent != null) {
            bookId = intent.getStringExtra("book_id")

        } else {
            finish()
            Toast.makeText(
                this@DescriptionActivity, "Some Error accured",
                Toast.LENGTH_LONG
            ).show()
        }


        if (bookId == "100") {
            finish()
            Toast.makeText(
                this@DescriptionActivity, "Some unexpexted accur accured ",
                Toast.LENGTH_LONG
            ).show()
        }


        val queve = Volley.newRequestQueue(this@DescriptionActivity)
        val url = "http://13.235.250.119/v1/book/get_book/"
        val jsonParams = JSONObject()
        jsonParams.put("book_id", bookId)


        if (ConnectionManager().checkConnectivity(this@DescriptionActivity)) {
            val jasonRequest =
                object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {


                    try {
                        val success = it.getBoolean("success")
                        if (success) {
                            val bookJsonObject = it.getJSONObject("book_data")
                            progressLayout.visibility = View.GONE


                            val bookImageUrl=bookJsonObject.getString("image")

                            Picasso.get().load(bookJsonObject.getString("image"))
                                .error(R.drawable.default_book_cover_img).into(imgBookImage)
                            txtBookName.text = bookJsonObject.getString("name")
                            txtBookAuthor.text = bookJsonObject.getString("author")
                            txtBookPrice.text = bookJsonObject.getString("price")
                            txtBookDisc.text = bookJsonObject.getString("description")

                           val bookEntity=bookInteties(

                               bookId?.toInt() as Int,
                               txtBookName.text.toString(),
                               txtBookAuthor.text.toString(),
                               txtBookPrice.text.toString(),
                               txtBookDisc.text.toString(),
                               bookImageUrl


                           )
                            val checkFav=DBAsynTask(applicationContext,bookEntity,1).execute()

                            val isFav=checkFav.get()

                            if (isFav){
                                btnAddToFav.text="Remove from favourites"
                                val favColor=ContextCompat.getColor(applicationContext,R.color.colorFavourites0)
                                btnAddToFav.setBackgroundColor(favColor)
                            }
                            else{
                                btnAddToFav.text="Add to favourites"
                                val noFavColor=ContextCompat.getColor(applicationContext,R.color.colorFavourites0)
                                btnAddToFav.setBackgroundColor(noFavColor)

                            }
                               btnAddToFav.setOnClickListener {
                                   if (!DBAsynTask(applicationContext,
                                       bookEntity,
                                       1).execute().get()
                                   ){
                                       val async=
                                           DBAsynTask(applicationContext,bookEntity,2).execute()
                                       val result=async.get()
                                       if(result){
                                           Toast.makeText(this@DescriptionActivity,"Book added to Favourites", Toast.LENGTH_SHORT)
                                               .show()
                                           btnAddToFav.text="Remove from favourites"
                                           val favColor=ContextCompat.getColor(applicationContext,R.color.colorFavourites0)
                                           btnAddToFav.setBackgroundColor(favColor)

                                       }
                                       else{
                                           Toast.makeText(
                                               this@DescriptionActivity,"some error occured",
                                               Toast.LENGTH_SHORT
                                           ).show()


                                       }
                                   }

                                   else{
                                       val async=DBAsynTask(applicationContext,bookEntity,3).execute()
                                       val result=async.get()

                                       if(result){
                                           Toast.makeText(this@DescriptionActivity,"book removed from favourites",
                                           Toast.LENGTH_SHORT).show()
                                           val noFavColor=
                                               ContextCompat.getColor(applicationContext,R.color.colorFavourites0)
                                           btnAddToFav.setBackgroundColor(noFavColor)


                                       }


                                       else{
                                           Toast.makeText(this@DescriptionActivity,
                                                   "Some error accured",Toast.LENGTH_SHORT).show()


                                       }

                                   }
                               }

                        } else {

                            Toast.makeText(
                                this@DescriptionActivity, "Some Error Occured",
                                Toast.LENGTH_LONG
                            ).show()

                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@DescriptionActivity, "some error accured",
                            Toast.LENGTH_LONG
                        ).show()

                    }


                },
                    Response.ErrorListener {


                        Toast.makeText(
                            this@DescriptionActivity, "Volley Error $it",
                            Toast.LENGTH_SHORT
                        ).show()

                    }) {

                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "9bf534118365f1"
                        return headers
                    }

                }

            queve.add(jasonRequest)

        } else {
            val dialog = AlertDialog.Builder(this@DescriptionActivity)
            dialog.setTitle("error")
            dialog.setMessage("Internet connection is not  found")
            dialog.setPositiveButton("open setting") { text, listener ->

                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                finish()
            }
            dialog.setNegativeButton("Exit") { text, listener ->
                ActivityCompat.finishAffinity(this@DescriptionActivity)

            }
            dialog.create()
            dialog.show()

        }

}
  class  DBAsynTask(val context: Context,val bookEntity: bookInteties,val mode:Int): AsyncTask<Void, Void, Boolean>() {



        val db= Room.databaseBuilder(context,dbClass::class.java, "books-db").build()


      override fun doInBackground(vararg params: Void?): Boolean {
          when(mode){


              1 ->{
                  val book:bookInteties?=db.bookDao().getBookById(bookEntity.book_id.toString())
                  db.close()
                  return book!=null
              }

              2 ->{
                  db.bookDao().insertBook((bookEntity))
                  db.close()
                  return true
              }

              3 ->{
                  db.bookDao().deleteBook(bookEntity)
                  db.close()
                  return true
              }
          }



          return false

      }

  }
}


