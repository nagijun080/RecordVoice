package com.example.recordvoice;

import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Setting extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        this.setContentView(R.layout.setting);
        
        // 画面のロックを防ぐ
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        //リストビューのオブジェクトを取得
        ListView listView = (ListView) findViewById(R.id.listView1);
        //リストビュー用のArrayAdapterを作成
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
        		android.R.layout.simple_list_item_1);
        //PackageManagerのオブジェクトを取得
        PackageManager pm = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        //Intent intent = new Intent(Intent.ACTION_CHOOSER);
        
//        //インストール済パッケージ情報を取得する
//        List<ApplicationInfo> list = pm.getInstalledApplications(0);
//        //パッケージ情報をリストビューに追記
//        for (ApplicationInfo ai : list) {
//            arrayAdapter.add(ai.packageName);
//            listView.setAdapter(arrayAdapter);
//        }
        
        //呼び出したいActivityのカテゴリを指定する
        intent.addCategory(Intent.CATEGORY_HOME);
        //カテゴリとアクションに一致するアクティビティの情報を取得する
        final List<ResolveInfo> appInfoList = pm.queryIntentActivities(intent, 0);
        
        
        //以下、取得したアクティビティ情報からアプリケーションリストの作成
        for(ResolveInfo ri : appInfoList){
            if(ri.loadLabel(pm).toString()!=null){
            	arrayAdapter.add(ri.loadLabel(pm).toString());
            }else{
            	arrayAdapter.add("NoName");
            }
        }
        listView.setAdapter(arrayAdapter);
        
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ResolveInfo ri = appInfoList.get(position);
                Uri uri=Uri.fromParts("package",ri.activityInfo.packageName,null);
                Intent intent=new Intent();
                //アプリケーションのパッケージ名を登録する
                intent.setData(uri);
                //アプリケーション管理の詳細画面を登録する
                ComponentName cn = ComponentName.unflattenFromString("com.android.settings/.applications.InstalledAppDetails");
                intent.setComponent(cn);
                startActivity(intent);
            }
        });
        
        //startActivity(intent);
//        Intent intent = new Intent(this, Setting.class);
        //startActivity(intent);
        
	}
	
	@Override
	public void onUserLeaveHint(){
		//ホームボタンが押された時や、他のアプリが起動した時に呼ばれる
		//戻るボタンが押された場合には呼ばれない
		//Toast.makeText(getApplicationContext(), "Good bye!" , Toast.LENGTH_SHORT).show();
		//Intent intent = new Intent(this, Lock.class);
        //startActivity(intent);
	}

//	//ロック解除ボタンでCallクラスへ移動
//	public void kaijo(View v){
//		Intent intent = new Intent(this, Call.class);
//		//Intent intent = new Intent(this, Sorry.class);
//		this.startActivity(intent);
//		//this.finish();	//このアクティビティを消滅する
//	}
	
	public void onClickButton1(View v){
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
		
	}
	public void onClickButton2(View v){
		Intent intent = new Intent(this, LockActivity.class);
		startActivity(intent);
		Setting.this.finish();
	}
	public void onClickButton3(View v){
		getPackageManager().clearPackagePreferredActivities(getPackageName());
	}
	
	
	
	
//	//アクティビティ終了時
//	@Override
//	protected void onStop() {
//			super.onStop();
////	protected void onPause(){
////		super.onPause();
//        this.finish();	//このアクティビティを消滅する
//	}
	
	
	//メニューから設定画面へ（もしものために実装）
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getMenuInflater().inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.item1:
			Intent intent = new Intent("android.settings.SETTINGS");
			startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(item);
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
