package com.example.recordvoice;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

//通話中
public class InCallActivity extends Activity {
	
	Timer timer;
	int counter;
	int limit = 15;	//次の画面へ移動するまでの秒
	TextView tv2;	//通話時間
	MediaPlayer mp;
	MediaRecorder mr;
	boolean isRecording = false;	//録音中かどうか
	String path;
	int startNum;	//毎回の最初の画像ナンバー
	int number;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.incall);
        tv2 = (TextView)this.findViewById(R.id.textView2);
        
        //Callクラスから画像ナンバーの取得
        Bundle extras = getIntent().getExtras();
        if(extras != null){
        	this.number = extras.getInt("number");
        	this.startNum = extras.getInt("startNum");
        }
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
        //10秒経過でJokeアクティビティへ
        this.startTimer();
        
        //応答の再生
        this.play();
        
        //録音開始
        recStart();
	}
	
	// Startボタン（録音開始）
    public void recStart() {
        if (isRecording) return;

        // SDカードのディレクトリ
        File dir = Environment.getExternalStorageDirectory();
        // アプリ名で
        File appDir = new File(dir, "RecordVoice");
        // ディレクトリを作る
        if (!appDir.exists()) appDir.mkdir();
        // ファイル名（現在時刻.3gp)
//		String name = System.currentTimeMillis() + ".3gp";
        String name = "joke"+ startNum +".3gp";
        // 出力ファイルのパス
        path = new File(appDir, name).getAbsolutePath();

        // インスタンスの取得
        mr = new MediaRecorder();
        // 入力ソースにマイクを指定
        mr.setAudioSource(MediaRecorder.AudioSource.MIC);
        //mr.setAudioSource(MediaRecorder.AudioSource.VOICE_UPLINK);	//送話のみ録音
        // 出力フォーマットに3gpを指定
        mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        //mr.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        // 音声エンコーダにAMRを指定
        //mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        // AMR_NBとDEFAULTとの違いは
        mr.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        // 出力ファイルのパスを指定
        mr.setOutputFile(path);
        try {
            // 準備して
        	mr.prepare();
            // 録音スタート！
        	mr.start();
            isRecording = true;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Stopボタン（録音終了）
    public void recStop() {
        if (!isRecording) return;

        // 録音を停止して
        mr.stop();
        mr.reset();
        // メモリの解放
        mr.release();
        isRecording = false;
    }
	
	//応答の再生
	public void play() {
		// リソースID指定
		//mp = MediaPlayer.create(this,R.raw.output);
		mp = MediaPlayer.create(this,R.raw.police00);
		//mp.setLooping(true);//ループ再生
		mp.seekTo(0);		//再生位置0ミリ秒
		mp.start();			//再生開始
    }
	
	//15秒タイマー
	public void startTimer(){
		if(timer != null) timer.cancel();
		timer = new Timer();
		final android.os.Handler handler = new android.os.Handler();
		timer.schedule(new TimerTask(){
			@Override
			public void run() {
				handler.post(new Runnable(){
					public void run(){
						if(counter == limit){
							timer.cancel();
							next();
							//if(wl.isHeld()) wl.release();
							//showAlarm();
						}else{
							//CountdownTimerActivity.countdown(counter);
							counter = counter+1;
							showCount();
						}
					}
				});
			}
		},0,1000);
	}
	
	//通話時間の表示
	public void showCount(){
		SimpleDateFormat form = new SimpleDateFormat("mm:ss");
		tv2.setText(form.format(counter*1000));
	}
	
	//10秒経過でJokeアクティビティへ
	public void next(){
		mp.stop();	//再生停止
		recStop();	//録音停止
		scan();		//ギャラリー登録
		
		this.finish();	//このアクティビティを消滅する
		Intent intent = new Intent(this, JokeActivity.class);
		intent.putExtra("number", this.number);
		intent.putExtra("startNum", this.startNum);
		this.startActivity(intent);
	}
	
	public void scan(){
		//ギャラリーに登録（APIレベル8）
		//第1引数:context,第2引数:path配列,第3引数:MimeType配列,第4引数:OnScanCompletedリスナー
		String[] paths = {path};
		MediaScannerConnection.scanFile(this, paths, null, sc);
	}
	//ギャラリーに登録したあと呼ばれる
    OnScanCompletedListener sc = new OnScanCompletedListener() {
		@Override
		public void onScanCompleted(String path, Uri uri) {
			
		}
	};
	
	//終了ボタンを押したとき
	public void onClick(View v){
	}
	
	//アクティビティ終了時
	@Override
	protected void onStop() {
		super.onStop();
        //タイマーのキャンセル
        this.timer.cancel();
        
        mp.stop();	//再生停止
        recStop();	//録音停止
        
//        //画像ナンバー保存（通話中でも写真が取れる場合）
//        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
//        Editor editor = pref.edit();
//        editor.putInt("number", this.number);
	}
	
	//戻るボタン無効
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
	    // TODO Auto-generated method stub
	    if (event.getAction()==KeyEvent.ACTION_DOWN) {
	        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
	            return false;
	        }
	    }
	    return super.dispatchKeyEvent(event);
	}
	
	private boolean finish;
	//隠しボタン（設定画面）
	public void setting(View v){
		if(finish){
			this.finish();	//このアクティビティを消滅する
			//Intent intent = new Intent("android.settings.SETTINGS");
			//startActivity(intent);
		}else{
			finish = true;
		}
	}
}
