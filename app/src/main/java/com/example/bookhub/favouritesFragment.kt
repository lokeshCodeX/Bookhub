package com.example.bookhub

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import database.bookInteties
import database.dbClass


class favouritesFragment : Fragment() {

    lateinit var recyclerFavouroites:RecyclerView
    lateinit var progressLayout:RelativeLayout
    lateinit var progressBar:ProgressBar
    lateinit var layoutManager:RecyclerView.LayoutManager
    lateinit var recyclerAdapter:FavouritesRecyclerAdapter

    var dbBookList= listOf<bookInteties>()





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view= inflater.inflate(R.layout.fragment_favourites, container, false)

        recyclerFavouroites =view.findViewById(R.id.recyclerFavourites)

        progressLayout=view.findViewById(R.id.progressLayout)
        progressBar=view.findViewById(R.id.progressBar)

        layoutManager=GridLayoutManager(activity as Context,2)

     dbBookList=RetrieveFavourites(activity as Context).execute().get()

      if(activity!=null){
          progressLayout.visibility=View.GONE
          recyclerAdapter=FavouritesRecyclerAdapter(activity as Context,dbBookList)
          recyclerFavouroites.adapter=recyclerAdapter
          recyclerFavouroites.layoutManager=layoutManager

      }


        return view
    }


    class RetrieveFavourites(val context:Context):AsyncTask<Void,Void,List<bookInteties>>(){
        override fun doInBackground(vararg params: Void?): List<bookInteties> {

            val db =Room.databaseBuilder(context,dbClass::class.java,"books-db").build()

            return db.bookDao().getAllBooks()

        }

    }


}