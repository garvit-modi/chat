package com.example.chat

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.image_left.*
import java.io.ByteArrayOutputStream
import java.lang.reflect.Type


class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var shared: SharedPreferences
        var messagesList: ArrayList<MessageModel> = ArrayList()
    }

    //Our variables
    private var mImageView: ImageView? = null
    private var mUri: Uri? = null

    //Our widgets
    private lateinit var btnCapture: Button
    private lateinit var btnChoose: Button
    var galleryImageUrls: ArrayList<String>? = null
    //Our constants
    private val OPERATION_CAPTURE_PHOTO = 1
    private val OPERATION_CHOOSE_PHOTO = 2

    lateinit var adapter: CustomAdapter

    //    lateinit var shared: SharedPreferences
    var gson = Gson()
    lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        shared = PreferenceManager.getDefaultSharedPreferences(this)
        recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val json = shared.getString("TAG", null)
        if (json != null) {
            val type: Type = object : TypeToken<List<MessageModel?>?>() {}.type
            messagesList = gson.fromJson(json, type)
        } else {

        }

        clicked()

        var popup = findViewById<ImageView>(R.id.listing)
        popup?.setOnClickListener()
        {
            showPopUp(popup)
        }

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


    fun showPopUp(view: View) {
        val popupMenu = android.widget.PopupMenu(view.context, view)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.header_menu, popupMenu.menu)
        popupMenu.show()


        popupMenu.setOnMenuItemClickListener(android.widget.PopupMenu.OnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.header1 -> {
                    Toast.makeText(view.context, "All Chat Deleted", Toast.LENGTH_SHORT).show();
                    val editor = shared.edit()
                    editor.remove("TAG");
                    editor.commit();
                    var myList: ArrayList<MessageModel> = arrayListOf()
                    messagesList = myList
                    clicked()
                }


            }
            true
        })

    }

    fun selectImage(view: View) {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder: android.app.AlertDialog.Builder =
            android.app.AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Add Photo!")
        builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
            if (options[item] == "Take Photo") {
//                capturePhoto()

                if (ContextCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.CAMERA
                    )
                    == PackageManager.PERMISSION_DENIED
                )
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.CAMERA),
                        1888
                    )


                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, 1888)


            } else
                if (options[item] == "Choose from Gallery") {
//

                    /*  val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.type = "image/*"
                startActivityForResult(intent, 2)*/
               */

                    openGalleryForImages()


                } else
                    if (options[item] == "Cancel") {
                        dialog.dismiss()
                    }
        })
        builder.show()
    }


    private fun show(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    private fun openGallery() {
        val intent = Intent("android.intent.action.GET_CONTENT")
        intent.type = "image/*"
        startActivityForResult(intent, 2)
    }

    private fun openGalleryForImages() {

        if (Build.VERSION.SDK_INT < 19) {
//            var intent = Intent()
//            intent.type = "image/*"
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//            intent.action = Intent.ACTION_GET_CONTENT
//            startActivityForResult(
//                Intent.createChooser(intent, "Choose Pictures"), 2
//            )

            if (!checkSelfPermission()) {
                requestPermission()
            } else {
                // if permission granted read images from storage.
                //  source code for this function can be found below.
//                fetchGalleryImages(this)

                var intent = Intent()
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(
                    Intent.createChooser(intent, "Choose Pictures")
                    , 2
                )
            }
        } else { // For latest versions API LEVEL 19+
//            var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//            intent.addCategory(Intent.CATEGORY_OPENABLE)
//            intent.type = "image/*"
//            startActivityForResult(intent, 2);
            if (!checkSelfPermission()) {
                requestPermission()
            } else {
                // if permission granted read images from storage.
                //  source code for this function can be found below.
//                fetchGalleryImages(this)
                var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "image/*"
                startActivityForResult(intent, 2);
            }
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 6036)
    }


    private fun checkSelfPermission(): Boolean {

        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }
    fun fetchGalleryImages(context: Activity) {

        val columns = arrayOf(
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media._ID
        ) //get all columns of type images
        val orderBy = MediaStore.Audio.Media.DATE_TAKEN //order data by date
        val imagecursor = context.managedQuery(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
            null, null
        ) //get all data in Cursor by sorting in DESC order
        galleryImageUrls = ArrayList()
        for (i in 0 until imagecursor.count) {
            imagecursor.moveToPosition(i)
            val dataColumnIndex =
                imagecursor.getColumnIndex(MediaStore.Images.Media.DATA) //get column index
            galleryImageUrls!!.add(imagecursor.getString(dataColumnIndex)) //get Image from column index
            messagesList.add(MessageModel((imagecursor.getString(dataColumnIndex)), 5))
        }
        Log.e("fatch in", "images")

//            val editor = shared.edit()
//            val json: String = gson.toJson(messagesList)
//            editor.putString("TAG", json)
//            editor.commit()
//            clicked()
        }

//        return galleryImageUrls

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantedResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantedResults)
        when (requestCode) {
            1 ->
                if (grantedResults.isNotEmpty() && grantedResults.get(0) ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    openGallery()
                } else {
                    show("Unfortunately You are Denied Permission to Perform this Operataion.")
                }
        }
    }

    fun encodeTobase64(image: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b: ByteArray = baos.toByteArray()
        val imageEncoded: String = Base64.encodeToString(b, Base64.DEFAULT)
        Log.d("Image Log:", imageEncoded)
        return imageEncoded
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//

        when (requestCode) {
//gallery
            2 ->
                if (resultCode == RESULT_OK) {


                    if (resultCode == Activity.RESULT_OK && requestCode == 2) {

                        // if multiple images are selected
                        if (data?.getClipData() != null) {
                            var count = data.clipData!!.itemCount

                            for (i in 0..count - 1) {
                                var imageUri: Uri = data.clipData?.getItemAt(i)!!.uri
                                if (imageUri != null) {
                                    messagesList.add(MessageModel(imageUri.toString(), 3))
                                    val editor = shared.edit()
                                    val json: String = gson.toJson(messagesList)
                                    editor.putString("TAG", json)
                                    editor.commit()
                                    clicked()
                                }

                            }

                        } else if (data?.getData() != null) {
                            // if single image is selected
                            if (data.data != null) {

                                if (resultCode != RESULT_CANCELED) {
                                    val selectedImage = data.data
                                    if (selectedImage != null) {
                                        messagesList.add(MessageModel(selectedImage.toString(), 3))
                                        val editor = shared.edit()
                                        val json: String = gson.toJson(messagesList)
                                        editor.putString("TAG", json)
                                        editor.commit()
                                        clicked()
                                    }

                                }
                            }

                        }
                    }

                }
//camera
            1888 -> {
                val photo: Bitmap = data?.extras?.get("data") as Bitmap
//            mImageView?.setImageBitmap(photo)
                var x = encodeTobase64(photo)
                var path = data?.data

                if (x != null) {
                    messagesList.add(MessageModel(x, 6))
                    val editor = shared.edit()
                    val json: String = gson.toJson(messagesList)
                    editor.putString("TAG", json)
                    editor.commit()
                    clicked()
                }
            }
            //pdf
            99 ->
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val selectedPdfFromStorage = data.data
//                    var selectedImagePath = getPath(selectedPdfFromStorage)
                    if (selectedPdfFromStorage != null) {

                        messagesList.add(MessageModel(selectedPdfFromStorage.toString(), 4))
//                        Toast.makeText(this, "$selectedImagePath", Toast.LENGTH_SHORT).show()
                        val editor = shared.edit()
                        val json: String = gson.toJson(messagesList)
                        editor.putString("TAG", json)
                        editor.commit()
                        clicked()

                    }

                }
//VIDEO
            100 ->
                if (resultCode == RESULT_OK && data != null) {


                    val selectedImageUri = data.data

                    var selectedImagePath = getRealPathFromURI(selectedImageUri)
                    if (selectedImagePath != null) {
                        messagesList.add(MessageModel(selectedImagePath, 5))

                        val editor = shared.edit()
                        val json: String = gson.toJson(messagesList)
                        editor.putString("TAG", json)
                        editor.commit()
                        clicked()
                    }


                }

        }

    }


    fun Pdf(view: View) {

//        Toast.makeText(this, "selectPDF", Toast.LENGTH_LONG).show()
        val browseStorage = Intent(Intent.ACTION_OPEN_DOCUMENT)
        browseStorage.type = "application/pdf"
        browseStorage.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        browseStorage.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(
            Intent.createChooser(browseStorage, "Select PDF"), 99
        )
    }

    fun Video(view: View) {
        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Video"),
            100
        )

    }

    fun getPath(uri: Uri?): String? {
        val result: String?
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        val cursor = contentResolver.query(uri!!, null, null, null, null)
        if (cursor != null) {
//            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
//            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
//            return uri.path
            val filePath = arrayOf(MediaStore.Video.Media.DATA)
            val c: Cursor? =
                contentResolver.query(uri, filePath, null, null, null)
            c?.moveToFirst()
            val columnIndex: Int = c!!.getColumnIndex(filePath[0])
            val picturePath: String = c.getString(columnIndex)
            c.close()
            return picturePath
//
        } else return uri.path
//        {     val idx = cursor.getColumnIndex(MediaStore.Video.Media.DATA)
//////            cursor.moveToFirst()
////            result = cursor.getString(idx)
////            cursor.close()
////        }
////
//


    }


    private fun getRealPathFromURI(contentURI: Uri?): String? {
        val result: String?
        val cursor = contentResolver.query(contentURI!!, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }

}