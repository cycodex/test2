package com.sunix.game.paperplane;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class TubeGap {
	
	public boolean mIsPass = false;
	
	private Context mContext;
	
	private float mTop;
	
	private TubeSprite mTubeLeft;
	
	private TubeSprite mTubeRight;
	
	private float mHeight;
	
	private float mSpeed;
	
	private float mBottomLine;
	
	private float mTubeHeight;
	
	public static final float PADDING = 5;
	
	public static final int STYLE_RAMDOM = 0;
	public static final int STYLE_LEFT_LONG = 1;
	public static final int STYLE_RIGHT_LONG = 2;
	public static final int STYLE_BOTH_SHORT = 3;
	
	public TubeGap(Context context, float top, float height, int style) {
		mTop = top;
		mContext = context;
		mHeight = height;
		
		createTubeByStyle(style);
	}
	
	public float getHeight() {
		return mHeight;
	}
	
	public float getTop() {
		return mTop;
	}
	
	public void setSpeed(float speed) {
		mSpeed = speed;
		if (mTubeLeft != null) {
			mTubeLeft.mSpeed.set(speed,0);
		}
		
		if (mTubeRight != null) {
			mTubeRight.mSpeed.set(speed,0);
		}
	}
	
	private void createTubeByStyle(int style) {
		Bitmap tubelbg;
		Bitmap tuberbg;
		if (style == STYLE_RAMDOM) {
			Random random = new Random();
			style = random.nextInt(3) + 1;
		}
		switch (style) {
		case STYLE_LEFT_LONG:
			tubelbg = BmpManage.getInstance(mContext).getTubeBitmap(BmpManage.STYLE_TUBE_LEFT_LONG);
			mTubeHeight = tubelbg.getHeight();
			mTubeLeft = new TubeSprite(mContext, 0, mTop, tubelbg.getWidth(), tubelbg.getHeight());
			mTubeLeft.setFrame(tubelbg);
			break;
		case STYLE_RIGHT_LONG:
			tuberbg = BmpManage.getInstance(mContext).getTubeBitmap(BmpManage.STYLE_TUBE_RIGHT_LONG);
			mTubeRight = new TubeSprite(mContext, GameAct.sWidth - tuberbg.getWidth(), mTop, tuberbg.getWidth(), tuberbg.getHeight());
			mTubeHeight = tuberbg.getHeight();
			mTubeRight.setFrame(tuberbg);
			break;
		case STYLE_BOTH_SHORT:
			tubelbg = BmpManage.getInstance(mContext).getTubeBitmap(BmpManage.STYLE_TUBE_LEFT_SHORT);
			mTubeLeft = new TubeSprite(mContext, 0, mTop, tubelbg.getWidth(), tubelbg.getHeight());
			mTubeLeft.setFrame(tubelbg);
			tuberbg = BmpManage.getInstance(mContext).getTubeBitmap(BmpManage.STYLE_TUBE_RIGHT_SHORT);
			mTubeRight = new TubeSprite(mContext, GameAct.sWidth - tuberbg.getWidth(), mTop, tuberbg.getWidth(), tuberbg.getHeight());
			mTubeHeight = tubelbg.getHeight();
			mTubeRight.setFrame(tuberbg);
			break;
		default:
			break;
		}
	}
	

	public void draw(Canvas canvas) {
		
		if (mTubeLeft != null) {
			mTubeLeft.draw(canvas);
		}
		
		if (mTubeRight != null) {
			mTubeRight.draw(canvas);
		}
		
		mTop = mTop + mSpeed;
		mBottomLine = mTop + mTubeHeight + PADDING*2;
	}
	
	public boolean pass(FlySprite fly) {
		if (!mIsPass) {
			if (fly.mFlyLeft.top >= mTop) {
				if (mTubeLeft != null) {
					if (fly.mFlyLeft.left <= mTubeLeft.mPoint.left + mTubeLeft.mSize.width) {
						GameControler.getInstance(mContext).gameOver();
					}
				}
				
				if (mTubeRight != null) {
					if (fly.mFlyRight.left >= mTubeRight.mPoint.left) {
						GameControler.getInstance(mContext).gameOver();
					}
				}
				
				if (fly.mFlyLeft.top > mBottomLine) {
					GameControler.getInstance(mContext).scoreUp();
					MediaManage.getInstance(mContext).playPass();
					mIsPass = true;
				}
			}
		}
		return mIsPass;
	}
}
