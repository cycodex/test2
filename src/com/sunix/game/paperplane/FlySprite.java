package com.sunix.game.paperplane;

import android.content.Context;
import android.graphics.Canvas;

public class FlySprite extends Sprite {
	
	private float mFlyTop, mFlyHeight, mFlyWidth;
	
	public SPoint  mFlyLeft, mFlyRight; // �������ܺ�ǽ�Ӵ��ĵ㣬��������������ײ���͹���
	
	private final float PADDING = 15;
	
	private float mWallWidth;
	
	private Context mContext;
	
	public FlySprite(Context context, float left, float top, float width, float height) {
		super(context, left, top, width, height);
		// TODO Auto-generated constructor stub
		mContext = context;
		mFlyHeight = height;
		mFlyWidth = width;
		mFlyLeft = new SPoint(0, 0);
		mFlyRight = new SPoint(0, 0);
	}

	public void setTop(float top) {
		mFlyTop = top;
		mFlyLeft.top = top + mFlyHeight - PADDING;
		mFlyRight.top = top + mFlyHeight - PADDING;
	}
	
	public float getTop() {
		return mFlyTop;
	}
	
	public void setWallWidth(float wallWidth) {
		mWallWidth = wallWidth;
	}
	
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.draw(canvas);
		
		mFlyLeft.left = mPoint.left + PADDING;
		mFlyRight.left = mPoint.left + mFlyWidth - PADDING;
		
		if (mFlyLeft.left <= mWallWidth || mFlyRight.left >= GameAct.sWidth - mWallWidth) {
			GameControler.getInstance(mContext).gameOver();
		}
	}
}
