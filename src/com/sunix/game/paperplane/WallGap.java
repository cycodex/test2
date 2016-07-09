package com.sunix.game.paperplane;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class WallGap {
	private Context mContext;
	
	private float mTop;
	
	private WallSprite mWallLeft;
	
	private WallSprite mWallRight;
	
	private float mHeight;
	
	private float mSpeed;
	
	private float mWallWidth;
	
	public static final int STYLE_RAMDOM = 0;
	public static final int STYLE_LEFT_LONG = 1;
	public static final int STYLE_RIGHT_LONG = 2;
	public static final int STYLE_BOTH_SHORT = 3;
	
	public WallGap(Context context, float top) {
		mTop = top;
		mContext = context;
		
		Bitmap wallLeftBmp = BmpManage.getInstance(mContext).getBitmap("walll.png");
		mHeight = wallLeftBmp.getHeight();
		mWallLeft = new WallSprite(context, 0, top, wallLeftBmp.getWidth(), mHeight);
		mWallWidth = wallLeftBmp.getWidth();
		mWallLeft.setFrame(wallLeftBmp);
		
		Bitmap wallRightBmp = BmpManage.getInstance(mContext).getBitmap("wallr.png");
		mWallRight = new WallSprite(context, GameAct.sWidth - wallRightBmp.getWidth(), top, wallRightBmp.getWidth(), wallRightBmp.getHeight());
		mWallRight.setFrame(wallRightBmp);
		
	}
	
	public float getHeight() {
		return mHeight;
	}
	
	public float getWidth() {
		return mWallWidth;
	}
	
	public float getTop() {
		return mTop;
	}
	
	public void setSpeed(float speed) {
		mSpeed = speed;
		mWallLeft.mSpeed.set(speed,0);
		mWallRight.mSpeed.set(speed,0);
	}
	
	

	public void draw(Canvas canvas) {
		mWallLeft.draw(canvas);
		mWallRight.draw(canvas);		
		mTop = mTop + mSpeed;
	}
}
