package com.example.recordvoice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

//ロック画面
public class Lock extends Activity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.lock);
        
        // 画面のロックを防ぐ
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	//ロック解除ボタンでCallクラスへ移動
	public void kaijo(View v){
		Intent intent = new Intent(this, Call.class);
		this.startActivity(intent);
		//this.finish();	//このアクティビティを消滅する
	}
	
	//録音再生テストボタンでRecordVoiceクラスへ移動
	public void test(View v){
		Intent intent = new Intent(this, RecordVoice.class);
		this.startActivity(intent);
		//this.finish();	//このアクティビティを消滅する
	}
	

}
