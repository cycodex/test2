package com.sunix.game.paperplane;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements android.view.SurfaceHolder.Callback{
	
	SurfaceHolder holder;
	
	Paint mPaint;
	
	Paint mPaintB;
	
	private Context mContext;
	
	Handler handler = new Handler();
	
	private boolean mVisible = true;
	
	private FlySprite mFlySprite;
	private Bitmap mFlyBitmap;
	
	private int draw_interval = 17;
	
	private ArrayList<TubeGap> mTubeGapList;
	
	private ArrayList<WallGap> mWallGaps;
	
	public static final float sGapHeight = GameAct.sHeight/3;
	
	private boolean mFlyLock = false; // 飞机垂直速度是否被锁定
	
	private int mTouchState = 0;
	
	private long mTouchInterval = 80;
	
	private long mTocuhTime = 0;
	
	private boolean mIsGameStart = false;
	
	private Sprite mScoreSprite;
	
	private static long mStartTime;
	
	public long mUsedTime;
	
	public GameView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
		holder = getHolder();
		holder.addCallback(this);
		
		mContext = context;
		this.mPaint = new Paint();
		this.mPaint.setTextSize(22);
		this.mPaint.setStrokeWidth(5);
		this.mPaint.setColor(Color.WHITE);
		
		this.mPaintB = new Paint();
		this.mPaintB.setColor(Color.BLACK);
		
		startGame();
		
		mStartTime = System.currentTimeMillis();
		drawFrame();
	}
	
	public void startGame() {
		mStartTime = System.currentTimeMillis();
		GameControler.getInstance(mContext).clearPassTime();
		mFlyBitmap = BmpManage.getInstance(mContext).getBitmap("fly7.png");
		mFlySprite = new FlySprite(mContext, 100, 150,  mFlyBitmap.getWidth(),  mFlyBitmap.getHeight());
		mFlySprite.setFrame(mFlyBitmap);
		mFlySprite.mSpeed.set(SpeedControler.FALL_SPEED3, SpeedControler.FLY_SPEED_RIGHT3);
		
		mTubeGapList = new ArrayList<TubeGap>();
		TubeGap gap = new TubeGap(mContext, 300, sGapHeight, TubeGap.STYLE_LEFT_LONG);
		mTubeGapList.add(gap);
		
		TubeGap gap1 = new TubeGap(mContext, 300 + sGapHeight, sGapHeight, TubeGap.STYLE_RIGHT_LONG);
		mTubeGapList.add(gap1);
		
		TubeGap gap2 = new TubeGap(mContext, 300 + (sGapHeight * 2), sGapHeight, 0);
		mTubeGapList.add(gap2);
		
		TubeGap gap3 = new TubeGap(mContext, 300 + (sGapHeight * 3), sGapHeight, 0);
		mTubeGapList.add(gap3);
		
		TubeGap gap4 = new TubeGap(mContext, 300 + (sGapHeight * 4), sGapHeight, 0);
		mTubeGapList.add(gap4);
		
		mWallGaps = new ArrayList<WallGap>();
		float wallTop = 0;
		float wallHeight = 0;
		while ((wallTop + wallHeight) < GameAct.sHeight) {
			WallGap wallgap = new WallGap(mContext, wallTop + wallHeight);
			wallTop = wallgap.getTop();
			wallHeight = wallgap.getHeight();
			mWallGaps.add(wallgap);
		}
		
		mFlySprite.setWallWidth(mWallGaps.get(0).getWidth());
		
		mFlyLock = false;
		
		SpeedControler.sAngle = 7;
		
		mTouchState = 0;
		
		Bitmap sroreBmp = BmpManage.getInstance(mContext).getScoreBitmap(GameControler.getInstance(mContext).getScore());
		mScoreSprite = new Sprite(mContext,GameAct.sWidth/2 - sroreBmp.getWidth(), 20, sroreBmp.getWidth()*2, sroreBmp.getHeight()*2);
		mScoreSprite.setFrame(sroreBmp);
		mIsGameStart = true;
	}
	
	public void stopGame() {
		
		SpeedControler.resetSpeed();
		
		mTubeGapList.clear();
		
		mTubeGapList = null;
		
		mFlySprite = null;
		
		mWallGaps.clear();
		
		mWallGaps = null;
		
		mIsGameStart = false;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
//		super.onDraw(canvas);
	}
	
	private final Runnable mDrawRunnable = new Runnable() {
		public void run() {	
			if (mVisible) {
				drawFrame();
			}
		}
	};
	
	public void drawFrame() {
		if (GameControler.sGameOver || !mIsGameStart) {
			return;
		}
		Canvas canvas = holder.lockCanvas();
		long t = System.currentTimeMillis();
		if(canvas != null)
		{			
			int save = canvas.save();
			canvas.scale(GameAct.sScaleX, GameAct.sScaleY);
			
			if (mTouchState != 0) {
				long time = System.currentTimeMillis();
				if (time - mTocuhTime > mTouchInterval) {
					mTocuhTime = time;
					MediaManage.getInstance(mContext).playClick();
					if (mTouchState == 1) {
						SpeedControler.sAngle--;
						if (SpeedControler.sAngle <= 1) {
							SpeedControler.sAngle = 1;
						}
					} else if (mTouchState == 2) {
						SpeedControler.sAngle++;
						if (SpeedControler.sAngle >= 7) {
							SpeedControler.sAngle = 7;
						}
					}
					updateSpeed();
				}
			}
			
			canvas.drawRect(0, 0, GameAct.sWidth, GameAct.sHeight, mPaintB);
			mFlySprite.draw(canvas);
			
			if (!mFlyLock && mFlySprite.mPoint.top >= GameAct.sHeight/3) {
				mFlySprite.mSpeed.verticalSpeed = 0;
				mFlySprite.setTop(mFlySprite.mPoint.top);
				mFlyLock = true;
			}
			
			drawTube(canvas);
			drawWall(canvas);
			updateScore(canvas);
			canvas.restoreToCount(save);
		}
		
		if(canvas != null)
		{
			holder.unlockCanvasAndPost(canvas);	
		}		
		
		t = System.currentTimeMillis() - t;
		handler.removeCallbacks(mDrawRunnable);
		if (mVisible) {
			if (t >= draw_interval)
				handler.post(mDrawRunnable);
			else
				handler.postDelayed(mDrawRunnable, draw_interval - t);
		}
	}
	
	public static long getUsedTime() {
		return System.currentTimeMillis() - mStartTime;
	}
	
	private void updateScore(Canvas canvas) {
		Bitmap sroreBmp = BmpManage.getInstance(mContext).getScoreBitmap(GameControler.getInstance(mContext).getScore());
		mScoreSprite.mPoint.left = GameAct.sWidth/2 - sroreBmp.getWidth();
		mScoreSprite.mSize.width = sroreBmp.getWidth()*2;
		mScoreSprite.setFrame(sroreBmp);
		mScoreSprite.draw(canvas);
	}
	
	private void drawWall(Canvas canvas) {
		WallGap gap0 = mWallGaps.get(0);
		if (gap0.getTop() + gap0.getHeight() <= 0) {
			mWallGaps.remove(0);
		}
		int pos = 0;
		int size = mWallGaps.size();
		WallGap newGap = null;
		for(WallGap wallgap : mWallGaps) {
			wallgap.draw(canvas);		
			if (mFlyLock) {
				wallgap.setSpeed(-SpeedControler.sFallSpeed);
			}
			if (pos == size - 1) {
				float top = wallgap.getTop();
				if (top <= GameAct.sHeight) {
					newGap = new WallGap(mContext, top +wallgap.getHeight());
					if (mFlyLock) {
						newGap.setSpeed(-SpeedControler.sFallSpeed);
					}
				}
			}
			pos++;
		}
		
		if (newGap != null) {
			mWallGaps.add(newGap);
		}
	}
	
	private void drawTube(Canvas canvas) {
		TubeGap gap0 = mTubeGapList.get(0);
		if (gap0.getTop() + sGapHeight <= 0) {
			mTubeGapList.remove(0);
		}
		int pos = 0;
		int size = mTubeGapList.size();
		TubeGap newGap = null;
		for (TubeGap gap:mTubeGapList) {
			gap.draw(canvas);
			gap.pass(mFlySprite);
			if (mFlyLock) {
				gap.setSpeed(-SpeedControler.sFallSpeed);
			}
			if (pos == size - 1) {
				float top = gap.getTop();
				if (top <= GameAct.sHeight) {
					newGap = new TubeGap(mContext, top + sGapHeight, sGapHeight, 0);
					if (mFlyLock) {
						newGap.setSpeed(-SpeedControler.sFallSpeed);
					}
				}
			}
			pos++;
		}
		
		if (newGap != null) {
			mTubeGapList.add(newGap);
		}
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		mVisible = true;
		drawFrame();	
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		mVisible = false;
		handler.removeCallbacks(mDrawRunnable);
	}
	
	public void onTocuh(MotionEvent event) {
		// TODO Auto-generated method stub
		int action = event.getAction();
		if (action == MotionEvent.ACTION_DOWN) {
			float touchx = event.getX();
			if (touchx < GameAct.sScreenWidth/2) {
				mTouchState = 1;
			} else {
				mTouchState = 2;
			}
		} else if (action == MotionEvent.ACTION_UP){
			mTouchState = 0;
		}
	}

	public void updateSpeed() {
		mFlyBitmap = BmpManage.getInstance(mContext).getBitmap("fly" + SpeedControler.sAngle + ".png");
		mFlySprite.mSpeed.horizontalSpeed = SpeedControler.getFlySpeed(SpeedControler.sAngle);
		mFlySprite.setFrame(mFlyBitmap);
		if (!mFlyLock) {
			mFlySprite.mSpeed.verticalSpeed = SpeedControler.sFallSpeed;
		}
	}
}
