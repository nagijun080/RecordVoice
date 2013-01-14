package com.example.recordvoice;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

//ロック画面
public class LockActivity extends ActivityGroup{
	
	Button button;
	ImageView dragView;
	View image;
	Resources resources;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.lock);
        
        // 画面のロックを防ぐ
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
      //ダイアログ
      alertDialog();
	}

	//ダイアログ
		public void alertDialog(){
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
		
			adb.setTitle("設定画面を開きますか？");
			adb.setMessage("このアプリを始めて使う場合は、必ず「はい」を押してください。");
		
			//ポジティブボタン
			adb.setPositiveButton("はい",
					new DialogInterface.OnClickListener() {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		        	Intent intent = new Intent(LockActivity.this, Setting.class);
		        	startActivity(intent);
		        }
			});
			//ネガティブボタン
			adb.setNegativeButton("いいえ",
					new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	            	
	            }
			});
			AlertDialog ad = adb.create();
			ad.show();
		}
		
		
		
		
		
		@Override
		protected void onRestart() {
			super.onRestart();
			Log.d("Lock", "onRestart");
		}
		@Override
		protected void onStart() {
			super.onStart();
			Log.d("Lock", "onStart");
		}
		@Override
		protected void onResume() {
			super.onResume();
			Log.d("Lock", "onResume");
		}
		@Override
		protected void onPause(){
			super.onPause();
			Log.d("Lock", "onPause");
		}
		@Override
		protected void onStop() {
			super.onStop();
			Log.d("Lock", "onStop");
		}
		@Override
		protected void onDestroy() {
			super.onDestroy();
			Log.d("Lock", "onDestroy");
		}
		@Override
		public void onUserLeaveHint(){
			//ホームボタンが押された時や、他のアプリが起動した時に呼ばれる
			//戻るボタンが押された場合には呼ばれない
			Log.d("Lock", "onUserLeaveHint");
		}

		

		

		//test用
		public void test(View v){
		}

		//メニューから設定画面へ（もしものために実装）
//		@Override
//		public boolean onCreateOptionsMenu(Menu menu) {
//			this.getMenuInflater().inflate(R.menu.menu, menu);
//			return super.onCreateOptionsMenu(menu);
//		}
//		@Override
//		public boolean onOptionsItemSelected(MenuItem item) {
//			switch(item.getItemId()){
//			case R.id.item1:
//				Intent intent = new Intent("android.settings.SETTINGS");
//				startActivity(intent);
//				break;
//			}
//			return super.onOptionsItemSelected(item);
//		}
		
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

		//ロック解除ボタンでCallクラスへ移動
		public void kaijo(){
			//lock.finish();	//このアクティビティを消滅する
			Intent intent = new Intent(this, CallActivity.class);
//			startActivity(intent);
			((ActivityGroupMain)getParent()).startInnerActivity("CallActivity", intent);
		}
		
		
		//アプリ終了のダブルクリック
		//sdカードマウントエラーなど
		//電源ボタン押したときの処理
		//音量調整、マナーモード→設定画面を作る
		//端末解像度にあったボタン位置に調整
		//ホーム切り替え（端末依存）
		//画像回転方向（端末依存）
	

}
