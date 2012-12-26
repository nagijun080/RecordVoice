package com.example.recordvoice;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

public class SoundControl {
	
	private AudioManager am;
	private final static int MIN_VOL = 3;
	private final static int MAX_VOL = 15;
	
	public SoundControl (Context context) {
		// AudioManager取得
		am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		int max = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		Log.d("maxVolume", String.valueOf(max));
	}
	public void setNormalRinger() {
		am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
	}
	
	public void setMinVolume() {
		am.setStreamVolume(AudioManager.STREAM_MUSIC, this.MIN_VOL, 0);
	}
	
	public void setMaxVolume() {
		am.setStreamVolume(AudioManager.STREAM_MUSIC, this.MAX_VOL, 0);
	}

}
