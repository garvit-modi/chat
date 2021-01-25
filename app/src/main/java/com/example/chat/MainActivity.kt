package com.example.chat

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.reflect.Type


class MainActivity : AppCompatActivity() {

    private var messagesList: ArrayList<MessageModel> = ArrayList()
    lateinit var adapter: CustomAdapter
    lateinit var shared: SharedPreferences
    var gson = Gson()
    lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        shared = PreferenceManager.getDefaultSharedPreferences(this)
        recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
//        shared = PreferenceManager.getDefaultSharedPreferences(this)
        val gson = Gson()
        val json = shared.getString("TAG", null)
        val type: Type = object : TypeToken<List<MessageModel?>?>() {}.type
        messagesList= gson.fromJson(json, type)


        clicked()
    }

    fun btnClicked(view: View) {

        var str: String? = editText2.text.toString()
        if (str != null && str != "") {

            messagesList.add(MessageModel(str, 2))
            val editor = shared.edit()
            val json: String = gson.toJson(messagesList)
            editor.putString("TAG", json)
            editor.commit()





            clicked()
            editText2.setText("")
        } else {
            Toast.makeText(this, "Ples enter text", Toast.LENGTH_SHORT).show()
        }


    }

    fun btnClickedLeft(view: View) {


        var str: String? = editText1.text.toString()
        if (str != null && str != "") {

            messagesList.add(MessageModel(str, 1))
            val editor = shared.edit()
            val json: String = gson.toJson(messagesList)
            editor.putString("TAG", json)
            editor.commit()





            clicked()
            editText1.setText("")
        } else {
            Toast.makeText(this, "Ples enter text", Toast.LENGTH_SHORT).show()
        }

    }


    fun clicked() {


        adapter = CustomAdapter(this, messagesList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        val position = adapter.itemCount.minus(1)
        recyclerView.scrollToPosition(position)

    }
}