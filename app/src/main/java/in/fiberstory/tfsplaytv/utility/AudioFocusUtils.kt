package `in`.fiberstory.tfsplaytv.utility

import android.content.Context
import android.media.AudioManager
import android.util.Log

object AudioFocusUtils {
    fun getFocus(context: Context): Boolean {
        var isfocused = false
        val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val focusChangeListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
            when (focusChange) {
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {}
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {}
                AudioManager.AUDIOFOCUS_LOSS -> {}
                AudioManager.AUDIOFOCUS_GAIN -> {}
                else -> {}
            }
        }
        // Request audio focus for playback
        val result = am.requestAudioFocus(
            focusChangeListener,  // Use the music stream.
            AudioManager.STREAM_MUSIC,  // Request permanent focus.
            AudioManager.AUDIOFOCUS_GAIN
        )
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Log.e("focus", "gained")
            isfocused = true
        }
        return isfocused
    }
}