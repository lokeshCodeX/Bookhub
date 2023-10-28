package com.example.bookhub

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import module.Book

class DashboardRecyclerdAdapter(val context:Context,val itemList:ArrayList<Book>):RecyclerView.Adapter<DashboardRecyclerdAdapter.DashBoardViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashBoardViewHolder{
    val view=LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_single_row,parent,false)
        return DashBoardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: DashBoardViewHolder, position: Int) {

        val book=itemList[position]

        holder.textBookName.text=book.bookName
        holder.txtBookAuthor.text=book.bookAuthor
        holder.txtBookPrice.text=book.bookCost
        //holder.imgBookImage.setImageResource(book.bookImage)
        Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover_img).into(holder.imgBookImage)
        holder.llContent.setOnClickListener{
            val intent =Intent(context,DescriptionActivity::class.java)
            intent.putExtra("book_id",book.bookId)
            context.startActivity(intent)
        }

    }
    class  DashBoardViewHolder(view:View):RecyclerView.ViewHolder(view){

        val textBookName:TextView=view.findViewById(R.id.txtBookName)
        val txtBookAuthor:TextView=view.findViewById(R.id.txtBookAuthor)
        val txtBookPrice:TextView=view.findViewById(R.id.txtBookPrice)
        val imgBookImage:ImageView=view.findViewById(R.id.imgBookImage)
        val llContent:LinearLayout=view.findViewById(R.id.llContent)
    }
}