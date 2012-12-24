package com.example.recordvoice;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class LockView extends View {
	
	private int width;
    private int height;
    //private Drawable image;
    private Bitmap image;
    LockActivity lock;
    
    public LockView(Context context, AttributeSet attrs){
    	super(context, attrs);
    	this.lock = (LockActivity)context;
    	
    	// リソースの画像ファイルの読み込み
        Resources r = context.getResources();
        //image = r.getDrawable(R.drawable.joke2);
        image = BitmapFactory.decodeResource(r, R.drawable.lockbar);
        
        //ディスプレイサイズの取得
        WindowManager windowmanager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display disp = windowmanager.getDefaultDisplay();
        width = disp.getWidth();
        height = disp.getHeight();
    }

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(image, left, 0, new Paint());
	}
    
	
	int left = -400;
	int x, oldx;
	boolean down;
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int)event.getX();
		//int y = (int)event.getY();
		
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			if(x < width*3/10){
				oldx = x;
				down = true;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if(down && x>oldx && x<(width*6/10)){
				left = -400 + x-oldx;
			}
			if(down && x>(width*6/10)){
				down = false;
				kaijo();
			}
			break;
		case MotionEvent.ACTION_UP:
			left = -400;
			down = false;
			break;
		}
		
		// 再描画の指示
		invalidate();
		
		return true;
	}
	
	//ロック解除ボタンでCallクラスへ移動
	public void kaijo(){
		//lock.finish();	//このアクティビティを消滅する
		Intent intent = new Intent(lock, CallActivity.class);
		lock.startActivity(intent);
	}
}
