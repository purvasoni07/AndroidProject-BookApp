package com.purva.bookhub.adapter

import android.content.Context
import android.content.Intent
import android.media.Image
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.purva.bookhub.DiscriptionActivity
import com.purva.bookhub.R
import com.purva.bookhub.model.Book
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_dashboard_single_row.view.*
import java.security.AccessControlContext

class DashboardRecyclerAdapter(val context: Context,val itemList:ArrayList<Book>) :RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_single_row,parent,false)
        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val book=itemList[position]
        holder.txtbookName.text=book.bookName
        holder.txtbookAuthor.text=book.bookAuthor
        holder.txtbookCost.text=book.bookPrice
        holder.txtbookRating.text=book.bookRating
                                                                                                            //        holder.imgicon.setImageResource(book.bookImg)
        Picasso.get().load(book.bookImg).error(R.drawable.default_book_cover).into(holder.imgicon)

        holder.llContent.setOnClickListener{
            val intent=Intent(context,DiscriptionActivity::class.java)                                   // dashboard fragment to discription activity
            intent.putExtra("book_id",book.bookId)
            context.startActivity(intent)
        }
    }

    class DashboardViewHolder(view:View):RecyclerView.ViewHolder(view){
        val txtbookName:TextView=view.findViewById(R.id.txtbookName)
        val txtbookAuthor:TextView=view.findViewById(R.id.txtAuthor)
        val txtbookCost:TextView=view.findViewById(R.id.txtBookCost)
        val txtbookRating:TextView=view.findViewById(R.id.txtBookRating)
        val imgicon:ImageView=view.findViewById(R.id.imgicon)
        val llContent:LinearLayout=view.findViewById(R.id.llContent)
    }
}