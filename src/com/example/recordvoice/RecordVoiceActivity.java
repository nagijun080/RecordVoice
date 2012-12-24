package com.example.recordvoice;

import java.io.File;
import java.io.IOException;

import com.example.recordvoice.VoicePlayer;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class RecordVoiceActivity extends Activity {
    private MediaRecorder mRecorder;

    private TextView txtView;
    private Button btn;
    private boolean isRecording = false;	//録音中かどうか

   // private final static String dirName = "RecordVoice";	//ファイル名

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        txtView = (TextView)findViewById(R.id.txtView01);
        btn = (Button)this.findViewById(R.id.button1);
        btn.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				voice();
		    	vibrate();
			}
        });
    }
    // Startボタン（録音開始）
    public void onClickStartButton(View view) {
        if (isRecording) return;

        txtView.setText("録音中...");

        // SDカードのディレクトリ
        File dir = Environment.getExternalStorageDirectory();
        // アプリ名で
        File appDir = new File(dir, "RecordVoice");
        // ディレクトリを作る
        if (!appDir.exists()) appDir.mkdir();
        // ファイル名
        //String name = dirName + System.currentTimeMillis() + ".3gp";
        String name = "joke" + 0 + ".3gp";
        // 出力ファイルのパス
        String path = new File(appDir, name).getAbsolutePath();

        // インスタンスの取得
        mRecorder = new MediaRecorder();
        // 入力ソースにマイクを指定
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // 出力フォーマットに3gpを指定
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        // 音声エンコーダにAMRを指定
        //mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        // AMR_NBとDEFAULTとの違いは
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        // 出力ファイルのパスを指定
        mRecorder.setOutputFile(path);
        try {
            // 準備して
            mRecorder.prepare();
            // 録音スタート！
            mRecorder.start();
            isRecording = true;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Stopボタン（録音終了）
    public void onClickStopButton(View view) {
        if (!isRecording) return;

        txtView.setText("録音しました");

        // 録音を停止して
        mRecorder.stop();
        mRecorder.reset();
        // メモリの解放
        mRecorder.release();
        isRecording = false;
    }
    
//	//再生（xmlでonClick不可・・・リスナーで登録する）
//    public void onClickPlay(View view) {
//    	voice();
//    	vibrate();
//    }
    
	//音声
  	private void voice() {
  		//インスタンス生成
      	VoicePlayer vplayer = new VoicePlayer();
      	vplayer.play();
  	}
	//バイブレータ
  	private void vibrate() {
  		//インスタンス生成
      	Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
      	//振動パターン(OFF,ON,OFF,ON...の時間)
      	long[] pattern = {1000, 1000, 1000, 1000};
      	//振動開始(パターン,繰り返し開始位置)
      	vibrator.vibrate(pattern, -1);	//繰り返しなし
  	}
}