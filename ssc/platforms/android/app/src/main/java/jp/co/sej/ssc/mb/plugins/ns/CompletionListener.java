package jp.co.sej.ssc.mb.plugins.ns;

import android.media.AudioManager;
import android.media.MediaPlayer;

/**
 * @author wangning
 */
public class CompletionListener implements MediaPlayer.OnCompletionListener {
    private final AudioManager audioManager;

    public CompletionListener(AudioManager audioManager) {
        this.audioManager = audioManager;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (NotificationSound.current == 0) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        }
    }
}
