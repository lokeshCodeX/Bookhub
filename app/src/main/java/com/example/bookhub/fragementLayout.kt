package com.example.bookhub

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import module.Book
import org.json.JSONException
import util.ConnectionManager


class fragementLayout : Fragment() {
    lateinit var recyclerDashboard:RecyclerView
    lateinit var layoutManger:RecyclerView.LayoutManager
    lateinit var btnCheckInternet:Button

    lateinit var progressLayout:RelativeLayout
    lateinit var progressBar:ProgressBar



     val bookInfoList= arrayListOf<Book>()



    /* val bookList= arrayListOf(
         "i am lokesh",
         "The Mathematics",
         "the HC Verma",
         "the Allen Solly",
         "the Lord OF the Ring",
         "the pink city:PALI",
         "the Godaaaaaan",
         "the freindship Jarourny",
         "The iit main Book",
         "R.D sharma "


     )

     */


    lateinit var recyclerdAdapter: DashboardRecyclerdAdapter


 /* val bookInfoList= arrayListOf<Book>(

        Book("I am Lokesh","Lokesh Thakur","Rs.299/-",R.drawable.i_am_lokesh),
        Book("the mathematics","S.Chand","Rs.489/-",R.drawable.mathematics_image),
        Book("Concept of Physics","H.C. Verma","Rs.567/-",R.drawable.hc_verma_image),
        Book("The Allen Solly","Allen Solly","Rs.345/-",R.drawable.allen_solly_image),
        Book("the Lord OfThe Ring","Crameron Grren","Rs.897/-",R.drawable.lord_ofthe_ring_image),
        Book("the Pink city:PALI","Manoj Thakur","Rs.899/-",R.drawable.thepink_city_pali_image),
        Book("The Goodan","Munshi Premchand","Rs.567/-",R.drawable.goodan_image),
        Book("the freindship","Kamal Gadiyaaa","Rs.122/-",R.drawable.freindship_image),
        Book("IIt-Jee Book","Pawan Pahlwaan","Rs.657/-",R.drawable.iit_jee_image),
        Book("RD sharma","RD Sharma","Rs.676/-",R.drawable.rd_sharma_image)



    )

*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fragement_layout, container, false)

        recyclerDashboard=view.findViewById(R.id.recyclerDashboard)
        btnCheckInternet=view.findViewById(R.id.btnCheckInternet)

     progressLayout=view.findViewById(R.id.progressLayout)
     progressBar=view.findViewById(R.id.progressBar)

     progressLayout.visibility=View.VISIBLE


      btnCheckInternet.setOnClickListener{
          if (ConnectionManager().checkConnectivity(activity as Context)){
              //enternet available

              val dialog=AlertDialog.Builder(activity as Context)
              dialog.setTitle("success")
              dialog.setMessage("Internet connection found")
              dialog.setPositiveButton("ok"){text,listener ->

              }
              dialog.setNegativeButton("cancel"){text,listener->

              }
              dialog.create()
              dialog.show()



          }else{
              //enternet not available

              val dialog=AlertDialog.Builder(activity as Context)
              dialog.setTitle("error")
              dialog.setMessage("Internet connection is not  found")
              dialog.setPositiveButton("ok"){text,listener ->

              }
              dialog.setNegativeButton("cancel"){text,listener->

              }
              dialog.create()
              dialog.show()

          }
      }





        layoutManger=LinearLayoutManager(activity)



        val queue=Volley.newRequestQueue(activity as Context)
        val url= "http://13.235.250.119/v1/book/fetch_books/"
         if(ConnectionManager().checkConnectivity(activity as Context)) {


             val jsonObjectRequest =
                 object : JsonObjectRequest(Method.GET, url, null, Response.Listener {


                     // Here we will handle the request
                     try {

                        progressLayout.visibility=View.GONE
                         val success = it.getBoolean("success")
                         if (success) {
                             val data = it.getJSONArray("data")
                             for (i in 0 until data.length()) {
                                 val bookJsonObject = data.getJSONObject(i)
                                 val bookObject = Book(
                                     bookJsonObject.getString("book_id"),
                                     bookJsonObject.getString("name"),
                                     bookJsonObject.getString("author"),
                                     bookJsonObject.getString("price"),
                                     bookJsonObject.getString("image")
                                 )
                                 bookInfoList.add(bookObject)

                                 recyclerdAdapter =
                                     DashboardRecyclerdAdapter(activity as Context, bookInfoList)

                                 recyclerDashboard.adapter = recyclerdAdapter
                                 recyclerDashboard.layoutManager = layoutManger





                                 recyclerDashboard.addItemDecoration(
                                     DividerItemDecoration(
                                         recyclerDashboard.context,
                                         (layoutManger as LinearLayoutManager).orientation
                                     )
                                 )


                             }

                         } else {
                             Toast.makeText(
                                 activity as Context,
                                 "Some error are occurred ",
                                 Toast.LENGTH_SHORT
                             ).show()
                         }
                     }

                     catch (e:JSONException){
                         Toast.makeText(activity as Context,"Some unexpected error occured!!!!",Toast.LENGTH_LONG
                          )
                     }

                 }, Response.ErrorListener {

                     // here we will handle the error

                     if (activity !=null) {
                         Toast.makeText(
                             activity as Context,
                             "volley error occurred ",
                             Toast.LENGTH_SHORT
                         ).show()


                     }


                 // it is a variable in which response will be store in the form of String

                 }) {
                     override fun getHeaders(): MutableMap<String, String> {
                         // mutableMap and hashmap both are same
                         val headers = HashMap<String, String>()
                         headers["Content-type"] = "application/json"
                         headers["token"] = "9bf534118365f1"
                         return headers
                     }
                 }
             queue.add(jsonObjectRequest)
         }else{


             val dialog=AlertDialog.Builder(activity as Context)
             dialog.setTitle("error")
             dialog.setMessage("Internet connection is not  found")
             dialog.setPositiveButton("open setting"){text,listener ->

                 val settingIntent=Intent(Settings.ACTION_WIRELESS_SETTINGS)
                 startActivity(settingIntent)
                 activity?.finish()
             }
             dialog.setNegativeButton("Exit"){text,listener->
                 ActivityCompat.finishAffinity(activity as Activity)

             }
             dialog.create()
             dialog.show()

         }


        return view
    }
}



