package com.example.recordvoice;

import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

public class ActivityGroupMain extends ActivityGroup {
	
	// アクティビティを管理するクラス
    private LocalActivityManager lam;
    Window window,windowLock;
    ViewGroup group;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
	    // インスタンスの取得
	    lam = getLocalActivityManager();
	    
	    
	 
//	    // 利用したいActivityのインテントを生成
//	    Intent intent = new Intent(getApplicationContext(), Lock.class);
//	    // lamを使いインテントからWindowを生成
//	    Window window = lam.startActivity("10000", intent);
		group = (ViewGroup)findViewById(R.id.layout);
//	    // WindowのオブジェクトからView情報を取得しレイアウトにセットする
//	    group.addView(window.getDecorView());
		
		//startInnerActivity(Lock.class);
		Intent intent = new Intent(this, LockActivity.class);
		//startInnerActivity("Lock", intent);
		windowLock = lam.startActivity("LockActivity", intent);
		group.addView(windowLock.getDecorView());
		
		
    }
    
    //public void startInnerActivity(Class<? extends Activity> clazz){
    public void startInnerActivity(String strId, Intent intent){
    	Log.d("アクティビティ起動", ""+strId);
    	// インスタンスの取得
//	    lam = getLocalActivityManager();
    	
    	group.removeAllViews();
//    	if(strId == "InCall") lam.getActivity("Call").finish();
//    	if(strId == "Joke") lam.getActivity("InCall").finish();
//    	if(strId == "Sorry") lam.getActivity("Joke").finish();
    	
	    // 利用したいActivityのインテントを生成
	    //Intent intent = new Intent(getApplicationContext(), clazz);
	    // lamを使いインテントからWindowを生成
	    window = lam.startActivity(strId, intent);
	    //ViewGroup group = (ViewGroup)findViewById(R.id.layout);
	    
	    // WindowのオブジェクトからView情報を取得しレイアウトにセットする
	    group.addView(window.getDecorView());
	    
    }
    
	//メニューから設定画面へ（もしものために実装）
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		this.getMenuInflater().inflate(R.menu.menu, menu);
//		return super.onCreateOptionsMenu(menu);
//	}
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch(item.getItemId()){
//		case R.id.item1:
//			Intent intent = new Intent("android.settings.SETTINGS");
//			startActivity(intent);
//			break;
//		}
//		return super.onOptionsItemSelected(item);
//	}
	
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
	
	//戻るボタン無効
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
	    // TODO Auto-generated method stub
	    if (event.getAction()==KeyEvent.ACTION_DOWN) {
	    	if(event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
	            return false;
	        }else if(event.getKeyCode() == KeyEvent.KEYCODE_POWER){
	        	return false;
	        }
	    }
	    return super.dispatchKeyEvent(event);
	}
	
	
	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d("ActivityGroup", "onRestart");
	}
	@Override
	protected void onStart() {
		super.onStart();
		Log.d("ActivityGroup", "onStart");
	}
	@Override
	protected void onResume() {
		super.onResume();
		Log.d("ActivityGroup", "onResume");
	}
	@Override
	protected void onPause(){
		super.onPause();
		Log.d("ActivityGroup", "onPause");
	}
	@Override
	protected void onStop() {
		super.onStop();
		Log.d("ActivityGroup", "onStop");
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d("ActivityGroup", "onDestroy");
	}
	@Override
	public void onUserLeaveHint(){
		//ホームボタンが押された時や、他のアプリが起動した時に呼ばれる
		//戻るボタンが押された場合には呼ばれない
		Log.d("ActivityGroup", "onUserLeaveHint");
	}
}
