package com.example.chat

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import java.io.InputStream
import java.net.URL


class CustomAdapter(private val context: Context, var list: ArrayList<MessageModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    //    lateinit var view: View
    private val contactArrayList: ArrayList<MessageModel>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var x: CustomAdapter.ViewHolder1

        when (viewType) {
            1 -> x = ViewHolder1(
                LayoutInflater.from(context).inflate(
                    R.layout.item_text_in,
                    parent,
                    false
                )
            )
            2 -> x = ViewHolder1(
                LayoutInflater.from(context).inflate(
                    R.layout.item_text_out,
                    parent,
                    false
                )
            )
            4 -> x = ViewHolder1(
                LayoutInflater.from(context).inflate(
                    R.layout.btn_right,
                    parent,
                    false
                )
            )
            5 -> x = ViewHolder1(
                LayoutInflater.from(context).inflate(
                    R.layout.btn_right,
                    parent,
                    false
                )
            )
            6 -> {

                x = ViewHolder1(
                    LayoutInflater.from(context).inflate(R.layout.image_left, parent, false)
                )
            }
            else -> {
                x = ViewHolder1(
                    LayoutInflater.from(context).inflate(R.layout.image_left, parent, false)
                )
            }

        }
        return x

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


        when (list[position].messageType) {
            1 -> {
                (holder as ViewHolder1).yourView?.text = list[position].message
                holder.yourView?.setOnClickListener {
                    var x = list[position].message
                    var y = list[position].messageType
                    AlertDialog.Builder(context)
                        .setTitle("My Title")
                        .setMessage("Want to delete")
                        .setPositiveButton("Yes") { dialog, which -> delted(x, y) }
                        .setNegativeButton("No") { dialog, which -> dialog.dismiss() }
                        .show()

                }

            }

            2 -> {
                (holder as ViewHolder1).yourView?.text = list[position].message
                holder.yourView?.setOnClickListener {
                    var x = list[position].message
                    var y = list[position].messageType
                    AlertDialog.Builder(context)
                        .setTitle("My Title")
                        .setMessage("Want to delete")
                        .setPositiveButton("Yes") { dialog, which -> delted(x, y) }
                        .setNegativeButton("No") { dialog, which -> dialog.dismiss() }
                        .show()

                }
            }


            3 -> {
                (holder as ViewHolder1).image?.setImageURI(list[position].message.toUri())
var x = list[position].message.toUri()
                holder.image?.isVisible = true
                holder.image?.setOnClickListener {
                    var x = list[position].message
                    var y = list[position].messageType
                    AlertDialog.Builder(context)
                        .setTitle("My Title")
                        .setMessage("Want to delete")
                        .setPositiveButton("Yes") { dialog, which ->
                            delted(x, y)
                            holder.image?.isVisible = false
                        }
                        .setNegativeButton("No") { dialog, which -> dialog.dismiss() }
                        .show()

                }
            }


            4 -> {
                (holder as ViewHolder1).btn?.isVisible = true
                holder.btn?.setOnClickListener {
                    var x = list[position].message
//                    val result = x.replace("content:/", "")
//
//                    val intent = Intent(Intent.ACTION_VIEW)
//
//
//                    intent.setDataAndType(x.toUri(), "application/pdf")
//                    context.startActivity(intent)
//                    //    var y
//                    Toast.makeText(context, "$x", Toast.LENGTH_LONG).show()
                    var intet = Intent(context, PdfViewActivity::class.java)
                    Toast.makeText(context, "sdfsadf$x", Toast.LENGTH_SHORT).show()
                    intet.putExtra("pdf", x)
                    context.startActivity(intet)
                }

                holder.btn?.setOnLongClickListener {
                    var x = list[position].message
                    var y = list[position].messageType
                    AlertDialog.Builder(context)
                        .setTitle("My Title")
                        .setMessage("Want to delete")
                        .setPositiveButton("Yes") { dialog, which ->
                            delted(x, y)
                            holder.btn?.isVisible = false
                        }
                        .setNegativeButton("No") { dialog, which -> dialog.dismiss() }
                        .show()


                    return@setOnLongClickListener true
                }
            }

            5 -> {


                (holder as ViewHolder1).btn?.isVisible = true
                holder.btn?.text = "video"
                holder.btn?.setOnClickListener {
                    var x = list[position].message
//                    holder.video!!.isVisible = true
//                    holder.video!!.setVideoPath(
//                        x.toUri().toString()
//                    )
//                 var   mediaController = MediaController(context)
//                    mediaController?.setAnchorView(holder.video)
//                    holder.video!!.setMediaController(mediaController)
//                    holder.video!!.setKeepScreenOn(true)
//                    holder.video!!.start()
                    var intet = Intent(context, video_play::class.java)
                    intet.putExtra("video", x)
                    context.startActivity(intet)
                    // mVideosPath[i] = /storage/emulated/0/Movies/test.mp4
//                        val mVideoWatch = Intent(Intent.ACTION_VIEW)
//                        mVideoWatch.setDataAndType(
//                           x.toUri(), "video/*"
//                        )
//
//                        context.startActivity(mVideoWatch)
//                    Toast.makeText(context, "$x", Toast.LENGTH_LONG).show()
                    //    var y
                }

                holder.btn?.setOnLongClickListener {
                    var x = list[position].message
                    var y = list[position].messageType
                    AlertDialog.Builder(context)
                        .setTitle("My Title")
                        .setMessage("Want to delete")
                        .setPositiveButton("Yes") { dialog, which ->
                            delted(x, y)
                            holder.btn?.isVisible = false
                        }
                        .setNegativeButton("No") { dialog, which -> dialog.dismiss() }
                        .show()


                    return@setOnLongClickListener true
                }
            }
            6 -> {
                var x = decodeBase64(list[position].message)
                (holder as ViewHolder1).image?.setImageBitmap(x)
                holder.image?.isVisible = true
                holder.image?.setOnClickListener {
                    var x = list[position].message
                    var y = list[position].messageType
                    AlertDialog.Builder(context)
                        .setTitle("My Title")
                        .setMessage("Want to delete")
                        .setPositiveButton("Yes") { dialog, which ->
                            delted(x, y)
                            holder.image?.isVisible = false
                        }
                        .setNegativeButton("No") { dialog, which -> dialog.dismiss() }
                        .show()

                }
            }
        }


    }

    fun LoadImageFromWebOperations(url: String?): Drawable? {
        return try {
            val `is`: InputStream = URL(url).getContent() as InputStream
            Drawable.createFromStream(`is`, "src name")
        } catch (e: Exception) {
            null
        }
    }

    fun decodeBase64(input: String?): Bitmap? {
        val decodedByte: ByteArray = Base64.decode(input, 0)
        return BitmapFactory
            .decodeByteArray(decodedByte, 0, decodedByte.size)
    }

    fun delted(x: String, y: Int) {
        var i = 0
        for (item in list) {
            if (item.message == x && item.messageType == y) {
                list.remove(item)
                var gson = Gson()
                val editor = MainActivity.shared.edit()
                val json: String = gson.toJson(list)
                editor.putString("TAG", json)
                editor.commit()
                notifyDataSetChanged()
            }
            i++

        }
    }


    override fun getItemCount(): Int {
        return list.size
    }


    override fun getItemViewType(position: Int): Int {
        return list[position].messageType
    }


    private inner class ViewHolder1 constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var yourView: TextView? = itemView.findViewById(R.id.message_text)

        var image: ImageView? = itemView.findViewById(R.id.img12)
        var btn: Button? = itemView.findViewById(R.id.btnPDf)
//var rc : CardView? = itemView.findViewById(R.id.message_layout)
//var video: VideoView? = itemView.findViewById(R.id.videoView1)
    }


}