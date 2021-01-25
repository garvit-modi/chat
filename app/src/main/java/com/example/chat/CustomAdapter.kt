package com.example.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(private val context: Context, internal var list: ArrayList<MessageModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    lateinit var view: View


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            ViewHolder1(LayoutInflater.from(context).inflate(R.layout.item_text_in, parent, false))
        } else
            ViewHolder1(LayoutInflater.from(context).inflate(R.layout.item_text_out, parent, false))


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


        (holder as ViewHolder1).yourView.text = list[position].message

    }

    override fun getItemCount(): Int {
        return list.size
    }


    override fun getItemViewType(position: Int): Int {
        return list[position].messageType
    }


    private inner class ViewHolder1 constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var yourView: TextView = itemView.findViewById(R.id.message_text)


    }


}