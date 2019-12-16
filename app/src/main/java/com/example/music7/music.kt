package com.example.music7

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.SeekBar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_music.*

class music : AppCompatActivity(),View.OnClickListener {
    var firebaseAuth: FirebaseAuth= FirebaseAuth.getInstance()
    private lateinit var mp: MediaPlayer
    private var totalTime: Int = 0;
    //Below line of code is used if music files are read from asset folder
   val A = intArrayOf(R.raw.music1,R.raw.music,R.raw.music2)
    var b = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)
        mp= MediaPlayer()

        mp.isLooping = true
        mp.setVolume(0.5f, 0.5f)
        totalTime = mp.duration
        // Below lines of code are used to read music files from firebase
        // val a= mp.setDataSource('https://firebasestorage.googleapis.com/v0/b/music7-11d0e.appspot.com/o/music.mp3?alt=media&token=9cf0c06f-6b11-4980-8ee3-f4b9f8b6a4bf');
        // val b= mp.setDataSource('https://firebasestorage.googleapis.com/v0/b/music7-11d0e.appspot.com/o/music1.mp3?alt=media&token=93030e50-2647-49a8-ab24-ad62504dc67d');
        // val c= mp.setDataSource('https://firebasestorage.googleapis.com/v0/b/music7-11d0e.appspot.com/o/music2.mp3?alt=media&token=47bb8660-6d6e-4b67-a6e7-9cb6bebe6604');
        // val A = intArrayOf(a,b,c)
        //mp = MediaPlayer.create(this, R.raw.music)
        playBtn.setOnClickListener(this)
        BtnPrev.setOnClickListener(this)
        BtnNext.setOnClickListener(this)
        logout.setOnClickListener(
            {
                if(firebaseAuth!=null)
                {
                    firebaseAuth.signOut()
                    firebaseAuth.addAuthStateListener {
                        if(firebaseAuth.currentUser==null)
                        {
                            this.finish()
                        }
                    }
                }
            }
        )


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


        fun createTimeLabel(time: Int): String {
            var timeLabel = ""
            var min = time / 1000 / 60
            var sec = time / 1000 % 60

            timeLabel = "$min:"
            if (sec < 10) timeLabel += "0"
            timeLabel += sec

            return timeLabel
        }

    }
    fun audioplay(pos:Int)
    {
        //mp=MediaPlayer.create(this,A[pos])
        mp = MediaPlayer.create(this,A[pos])


        positionBar.max=mp.duration
        //maxtime.setText(milliSecondToString(seeksong.max))
        //curtime.setText(milliSecondToString(mediaPlayer.currentPosition))
        positionBar.progress=mp.currentPosition
        //name.setText()
        mp.start()
        //var updateSeekBarThread=UpdateSeekBarProgressThread()
        // handler.postDelayed(updateSeekBarThread,50)
    }
    //Logic for playing next song in the list
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
//Logic for playing previous song in the list
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


}

//private fun MediaPlayer.setDataSource(c: Char) {

//}
