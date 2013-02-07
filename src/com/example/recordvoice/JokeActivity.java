package com.example.recordvoice;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

//ネタばらし
public class JokeActivity extends Activity {

	private ImageView iv;
	String path;
	VoicePlayer vplayer;
	Bitmap bmp[];	//撮影した画像リスト
	int startNum;	//毎回の最初の画像ナンバー
	int number;
	int showNum;	//表示している画像ナンバー
	Button previousBTN,nextBTN;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joke);
        //マナーモードを解除する
        SoundControl soundCon = new SoundControl(this);
        soundCon.setNormalRinger();
        soundCon.setMaxVolume();
        // 画面のロックを防ぐ
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        //ディスプレイサイズの取得
        WindowManager windowmanager = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        Display disp = windowmanager.getDefaultDisplay();
        if (disp.getWidth() >= 480) {
        	setContentView(R.layout.joke);
        	if (disp.getWidth() >= 720) {
        		setContentView(R.layout.joke_1280x720);
        	}
        }
        
        //InCallクラスから画像ナンバーの取得
        Bundle extras = getIntent().getExtras();
        if(extras != null){
        	this.number = extras.getInt("number");
        	this.startNum = extras.getInt("startNum");
        }
        //撮影枚数分のbmp配列を作成
		bmp = new Bitmap[number-startNum];
		//パスの取得
		path = Environment.getExternalStorageDirectory()+ "/RecordVoice/joke"+ this.startNum +".jpg";
		//path = Environment.getExternalStorageDirectory()+ "/RecordVoice/joke.jpg";
		
		iv = (ImageView)findViewById(R.id.imageView1);
		previousBTN = (Button)this.findViewById(R.id.previous);
		nextBTN = (Button)this.findViewById(R.id.next);
//	}
//	
//	@Override
//	protected void onResume() {
//		super.onResume();
		
        //録音音声の再生
		vplayer = new VoicePlayer(this.startNum);
      	vplayer.play();
      	
        //画像の表示
        show();
	}
	
	//画像の表示
	private void show(){
		BitmapFactory.Options opt = new BitmapFactory.Options();
//		opt.inJustDecodeBounds = true;	//サイズ情報のみ取得するフラグ
		opt.inJustDecodeBounds = false;	//画像を取得するためのフラグ
		if(BitmapFactory.decodeFile(path, opt) != null){
			//画像がある場合
			//BitmapFactory.decodeFile(path, opt);// サイズ情報を取得する
			
//			int scaleW = opt.outWidth / 300; // →2(1/2サイズ)
//			int scaleH = opt.outHeight / 200; // →3(1/3サイズ)
//			int sampleSize = Math.max(scaleW, scaleH); // →3(1/3サイズ)
//			opt.inSampleSize = sampleSize;	// 2の乗数に丸められるのでここでは3で指定しても2と同様に扱われる
			opt.inSampleSize = 2;
			
//			opt.inJustDecodeBounds = false;	//画像を取得するためのフラグ
			
			
			for(int i=0; i<bmp.length; i++){
				path = Environment.getExternalStorageDirectory()+ "/RecordVoice/joke"+(this.startNum+i)+".jpg";
				bmp[i] = BitmapFactory.decodeFile(path, opt);// 元画像の1/2で読み込まれる
			}
			
//			int w = bmp.getWidth(); // →480/2
//			int h = bmp.getHeight(); // →640/2
//			float scale = Math.min((float)300/w, (float)200/h); // →0.6666667
//			Matrix matrix = new Matrix();
//			matrix.postScale(scale, scale);
//			
//			bmp = Bitmap.createBitmap(bmp, 0, 0, w, h, matrix, true);
			
			iv.setImageBitmap(bmp[showNum]);
			if(showNum == 0) previousBTN.setVisibility(View.INVISIBLE);
			if(showNum == bmp.length-1) nextBTN.setVisibility(View.VISIBLE);
			System.out.println(bmp.length);
			
		}else{
			//画像がない場合　xmlで指定した画像
		}
	}
	
	//前の写真ボタン
	public void previous(View v){
		if(showNum <= 0) return;
		showNum--;
		System.out.println(showNum);
		if(showNum >= 0) iv.setImageBitmap(bmp[showNum]);	//写真の表示
		if(showNum <= 0) previousBTN.setVisibility(View.INVISIBLE);	//前へボタン非表示
		if(showNum < bmp.length-1) nextBTN.setVisibility(View.VISIBLE);	//次へボタン表示
	}
	//次の写真ボタン
	public void next(View v){
		if(showNum >= bmp.length-1) return;
		showNum++;
		System.out.println(showNum);
		if(showNum <= bmp.length-1) iv.setImageBitmap(bmp[showNum]);	//写真の表示
		if(showNum >= bmp.length-1) nextBTN.setVisibility(View.INVISIBLE);	//次へボタン非表示
		if(showNum > 0) previousBTN.setVisibility(View.VISIBLE);	//前へボタン表示
	}
	
	//ごめんなさいボタンでSorryアクティビティへ
	public void sorryOnClick(View v){
		//this.finish();	//このアクティビティを消滅する
		Intent intent = new Intent(this, SorryActivity.class);
		((ActivityGroupMain)getParent()).startInnerActivity("JokeActivity", intent);
	}
	
	//アクティビティ終了時
	@Override
	protected void onStop() {
		super.onStop();
		vplayer.stop();
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
