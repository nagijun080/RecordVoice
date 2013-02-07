package com.example.recordvoice;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;


//ごめんなさい
public class SorryActivity extends Activity implements SensorEventListener{
	
	private SensorManager sensorManager;
	private float x,xx;
//	private float y,yy;
//	private float z,zz;
	private int count = 100;
	private int countUpSpeed = 1000;
	long now;
	long shakeTime;
	TextView tv;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sorry);
        //マナーモードを解除する
        SoundControl soundCon = new SoundControl(this);
        soundCon.setNormalRinger();
        //センサーオブジェクト作成
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        
        // 画面のロックを防ぐ
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        tv = (TextView)findViewById(R.id.count);
//	}
//	
//	//アクティビティが動き始めたらリスナーを登録する
//    public void onResume() {
//    	super.onResume();
    	//カウントアップスタート
        startTimer();
    	//加速度センサーリストを取得
    	List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
    	if (sensorList != null && !sensorList.isEmpty()) {
    		Sensor sensor = sensorList.get(0);
    		//リスナー登録(p163)
    		sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
    	}
    }
    @Override
	protected void onStop() {
		super.onStop();
		//タイマーのキャンセル
		this.timer.cancel();
		//加速度センサーリスナー解除
		sensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		//ランドスケープ固定のためxyを入れ替える
		x = event.values[0];	//x軸
		//y = event.values[1];	//y軸
		//z = event.values[2];	//z軸
		
		//次のシェイクまで0.1秒あける
		//now = System.currentTimeMillis();
		//if(now - shakeTime > 100){
			countup();
		//}

//		((TextView)findViewById(R.id.x)).setText("x:"+x);
//		((TextView)findViewById(R.id.y)).setText("y:"+y);
//		((TextView)findViewById(R.id.z)).setText("z:"+z);

		
		tv.setText(String.valueOf(count));
	}
	public void countup(){
		//左右方向の加速度の前回との差が5を超える場合にカウントアップ（数値は適当）
		if(Math.abs(x - xx)>30 && count>3) count--;
		//else if(Math.abs(y - yy)>10) count++;
		//else if(Math.abs(z - zz)>10) count++;
		xx = x;
		//yy = y;
		//zz = z;
		//shakeTime = System.currentTimeMillis();
	}
	
	Timer timer;
	//カウントアップ(スレッド)
	public  void  startTimer(){
		if(this.timer != null) this.timer.cancel();
		this.timer = new Timer();
		final android.os.Handler handler = new android.os.Handler();//ハンドラー

		//スケジュール(一定時間ごとにスレッドを実行できる)
		this.timer.schedule(new TimerTask(){

			//スレッドの実装
			@Override
			public void run() {
				handler.post(new Runnable(){	//ハンドラーポスト
					public void run(){
						//カウント100以上の時
						if(count >= 100){
						//カウントアップ中
						}else{
							count++;
							if(count < 10) count += 3;
							else if(count < 30) count += 2;
							else if(count < 50) count += 1;
						}
					}
				});
			}
		},0,countUpSpeed);
	}
	
	//ストップを押してキャンセルする場合(間接的に呼ばれる？)
	@Override
	public void onDestroy() {
		super.onDestroy();
//		if(this.wl.isHeld()){
//			this.wl.release();	//ロック画面にしない設定を解除
//		}
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
