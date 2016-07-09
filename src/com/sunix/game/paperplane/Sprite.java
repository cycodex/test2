package com.sunix.game.paperplane;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

public class Sprite {
	
	public SSize mSize;
	public SPoint mPoint;
	public SSpeed mSpeed;
	private Matrix mMatrix;
	protected Bitmap mFrame;
	protected Paint mPaint;
	
	public Sprite(Context context, float left, float top, float width, float height) {
		mSize = new SSize(width, height);
		mPoint = new SPoint(left, top);
		mSpeed = new SSpeed(0, 0);
		
		this.mMatrix = new Matrix();
		this.mMatrix.preTranslate(left, top);
		
		this.mPaint = new Paint();
		this.mPaint.setTextSize(22);
		this.mPaint.setStrokeWidth(5);
		this.mPaint.setColor(Color.WHITE);
	}
	
	public void setFrame(Bitmap frame) {
		mFrame = frame;
	}
	
	class SSize {
		public float width;
		public float height;
		
		public SSize(float width, float height) {
			this.width = width;
			this.height = height;
		}
		
		public void set(float width, float height) {
			this.width = width;
			this.height = height;
		}
	}
	
	class SPoint {
		public float left;
		public float top;
		
		public SPoint(float left, float top) {
			this.left = left;
			this.top = top;
		}
		
		public void set(float left, float top) {
			this.left = left;
			this.top = top;
		}
	}
	
	class SSpeed {
		public float verticalSpeed;
		public float horizontalSpeed;
		
		public SSpeed(float vs, float hs) {
			verticalSpeed = vs;
			horizontalSpeed = hs;
		}

		public void set(float vs, float hs) {
			verticalSpeed = vs;
			horizontalSpeed = hs;
		}
	}
	
	public void draw(Canvas canvas) {
		if (canvas == null) {
			return;
		}
		
		if (mFrame != null) {	
			float scalex = (float)this.mSize.width / (float)mFrame.getWidth();
			float scaley = (float)this.mSize.height / (float)mFrame.getHeight();
			mMatrix.reset();
			mMatrix.preTranslate(mPoint.left, mPoint.top);
			mMatrix.preScale(scalex, scaley);
			canvas.drawBitmap(mFrame, mMatrix, mPaint);
		} else {
			canvas.drawRect(mPoint.left, mPoint.top, mPoint.left + mSize.width, mPoint.top + mSize.height, mPaint);
		}
		
		mPoint.left = mPoint.left + mSpeed.horizontalSpeed;
		mPoint.top = mPoint.top + mSpeed.verticalSpeed;
	}

}
