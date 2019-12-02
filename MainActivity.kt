package com.example.musicplayerv1

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),View.OnClickListener {
    private lateinit var mp: MediaPlayer
    private var totalTime: Int = 0
    val A = intArrayOf(R.raw.music1,R.raw.music)
    var b = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mp= MediaPlayer()

        //mp = MediaPlayer.create(this, R.raw.music)
        mp.isLooping = true
        mp.setVolume(0.5f, 0.5f)
        totalTime = mp.duration
        playBtn.setOnClickListener(this)

        BtnPrev.setOnClickListener(this)
        BtnNext.setOnClickListener(this)

        // Volume Bar
        volumeBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekbar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        var volumeNum = progress / 100.0f
                        mp.setVolume(volumeNum, volumeNum)
                    }
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                }
            }
        )

        // Position Bar
        positionBar.max = totalTime
        positionBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        mp.seekTo(progress)
                    }
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                }
            }
        )

        // Thread
        Thread(Runnable {
            while (mp != null) {
                try {
                    var msg = Message()
                    msg.what = mp.currentPosition
                    handler.sendMessage(msg)
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                }
            }
        }).start()
    }

    @SuppressLint("HandlerLeak")
    var handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            var currentPosition = msg.what

            // Update positionBar
            positionBar.progress = currentPosition

            // Update Labels
            var elapsedTime = createTimeLabel(currentPosition)
            elapsedTimeLabel.text = elapsedTime

            var remainingTime = createTimeLabel(totalTime - currentPosition)
            remainingTimeLabel.text = "-$remainingTime"
        }
    }

    fun createTimeLabel(time: Int): String {
        var timeLabel = ""
        var min = time / 1000 / 60
        var sec = time / 1000 % 60

        timeLabel = "$min:"
        if (sec < 10) timeLabel += "0"
        timeLabel += sec

        return timeLabel
    }



    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.playBtn -> {
                if (mp.isPlaying) {
                    // Stop
                    mp.pause()
                    //playBtn.setBackgroundResource(R.drawable.play)

                } else {
                    audioplay(b)
                }
            }
            R.id.BtnPrev -> {
                audioPrev()
            }
            R.id.BtnNext -> {
                audioNext()
            }
        }


    }
    fun audioplay(pos:Int)
    {
        mp=MediaPlayer.create(this,A[pos])


        positionBar.max=mp.duration
        //maxtime.setText(milliSecondToString(seeksong.max))
        //curtime.setText(milliSecondToString(mediaPlayer.currentPosition))
        positionBar.progress=mp.currentPosition
        //name.setText()
        mp.start()
        //var updateSeekBarThread=UpdateSeekBarProgressThread()
       // handler.postDelayed(updateSeekBarThread,50)
    }

    fun audioNext()
    {
        if (mp.isPlaying)mp.stop()
        if (b<(A.size-1)){
            b++
        }else {
            b =0
        }
        audioplay(b)
    }

    fun audioPrev()
    {
        if (mp.isPlaying)mp.stop()
        if (b>0){
            b--
        }else
        {
            b=A.size-1
        }
        audioplay(b)
    }


}