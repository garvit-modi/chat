package com.example.chat

import android.media.MediaPlayer.OnPreparedListener
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_video_play.*


class video_play : AppCompatActivity() {
    var x : String? = null
    var y : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_play)

        var   mediaController = MediaController(this)
                    mediaController?.setAnchorView(videoView1)
        x = intent.getStringExtra("video")
        videoView1.setVideoPath(
            x!!
        )

        videoView1.setMediaController(mediaController)
        videoView1.setKeepScreenOn(true)

        videoView1.setOnPreparedListener(OnPreparedListener { /*seekBar.setMax(videoView.getDuration())*/ })


        videoView1.start()




    }

    fun PlayButton(view: View?) {
        if (videoView1.isPlaying()) {
            videoView1.resume()
        } else {
            videoView1.start()
        }
        object : CountDownTimer(videoView1.getDuration().toLong(), 1) {
            override fun onTick(l: Long) {
//                seekBar.setProgress(videoView1.getCurrentPosition(), true)
            }

            override fun onFinish() {}
        }.start()
    }

    fun PauseButton(view: View?) {
        videoView1.pause()
    }

    fun RestartButton(view: View?) {
        videoView1.stopPlayback()
        videoView1.setVideoPath(x)
        videoView1.start()
    }

    fun StopButton(view: View?) {
        videoView1.stopPlayback()
        videoView1.setVideoPath(x)
    }

    override fun onPause() {
        super.onPause()

        y = videoView1.getCurrentPosition(); //stopPosition is an int
        videoView1.pause();

    }

    override fun onResume() {
        super.onResume()
        videoView1.seekTo(y)
        videoView1.start() //Or use resume() if it doesn't work. I'm not sure
    }


    // This gets called before onPause so pause video here.


}