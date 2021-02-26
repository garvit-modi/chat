package com.example.chat

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import kotlinx.android.synthetic.main.activity_pdf_view.*

class PdfViewActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_view)
        var x = intent.getStringExtra("pdf")
        Toast.makeText(this,"$x",Toast.LENGTH_SHORT).show()
        pdfView.fromUri(x?.toUri())
            .defaultPage(0)
            .spacing(10)
            .load()

    }








}