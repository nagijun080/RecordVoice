package com.example.recordvoice;

import android.content.Context;
import android.media.AudioManager;

public class SoundControl {
	
	public void setRingerMode(Context context) {
		// AudioManager取得
		AudioManager am = (AudioManager)context.getSystemService(context.AUDIO_SERVICE);
		am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
	}

}
