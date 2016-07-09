package com.sunix.game.paperplane;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;

public class GameControler {

	public int mLevel = 1;

	private static final String SP_NAME = "paperplanedata";
	private static final String KEY_SCORE = "keyscore";
	private static final String KEY_PASS = "pass_";

	public static final int MSG_GAME_STOP = 1000;
	public static final int MSG_GAME_START = 1001;
	public static final int MSG_UPDATA_TASK = 1002;

	public static boolean sGameOver = false;

	public Context mContext;

	public static GameControler sInstance = null;

	public Handler mMessageHandler;

	private int mThisTask = 20;

	private int mNextTask = 20;

	private int mScore = 0;

	private HashMap<Integer, Long> mPassTime;
	
	private SimpleDateFormat formatter;
	
	private String mUserTimeString;
	
	private long mUserTime;

	public GameControler(Context context) {
		mContext = context;
		mPassTime = new HashMap<Integer, Long>();
		formatter = new SimpleDateFormat("mm:ss:SS"); // 初始化Formatter的转换格式。
	}

	public void setMessageHandler(Handler handler) {
		mMessageHandler = handler;
	}

	public static GameControler getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new GameControler(context);
		}
		return sInstance;
	}

	public int getLevel() {
		return mLevel;
	}

	public void setLevel(int level) {
		mLevel = level;
	}

	public void gameOver() {
		MediaManage.getInstance(mContext).playDeal();
		sGameOver = true;
		stopGame();
	}

	public void stopGame() {
		mMessageHandler.sendEmptyMessage(MSG_GAME_STOP);
	}

	public void startGame() {
		sGameOver = false;
		mMessageHandler.sendEmptyMessage(MSG_GAME_START);
	}

	public int getScore() {
		return mScore;
	}

	public void scoreUp() {
		mScore++;
		if (mScore == mNextTask) {
			setThisTask(mNextTask);
			if (mScore == 20) {
				setLevel(2);
			} else if (mScore == 40) {
				setLevel(3);
				SpeedControler.setLevel(2);
			} else if (mScore == 60) {
				setLevel(4);
				SpeedControler.setLevel(3);
			} else if (mScore == 80) {
				setLevel(5);
				SpeedControler.setLevel(4);
			}
			setNextTask(mNextTask + 20);
			
			new Thread() {
				public void run() {
					mUserTime = GameView.getUsedTime();
					GameControler.getInstance(mContext).savePassTime(mUserTime);
					mUserTimeString = GameControler.getInstance(mContext).format(mUserTime);
					mMessageHandler.sendEmptyMessage(MSG_UPDATA_TASK);
					MediaManage.getInstance(mContext).playLevelup();
				};
			}.start();
		}
	}

	public void setScore(int score) {
		mScore = score;
	}

	public void saveBest(int score) {
		SharedPreferences sp = mContext.getSharedPreferences(SP_NAME,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putInt(KEY_SCORE, score);
		editor.commit();
	}

	public int getBest() {
		SharedPreferences sp = mContext.getSharedPreferences(SP_NAME,
				Context.MODE_PRIVATE);
		return sp.getInt(KEY_SCORE, 0);
	}

	public int getThisTask() {
		return mThisTask;
	}

	public int getNextTask() {
		return mNextTask;
	}

	public void setThisTask(int thisTask) {
		mThisTask = thisTask;
	}

	public void setNextTask(int nextTask) {
		mNextTask = nextTask;
	}

	public void savePassTime(long passTime) {
		mPassTime.put(mThisTask, passTime);
	}

	public HashMap<Integer, Long> getPassTime() {
		return mPassTime;
	}
	
	public void clearPassTime() {
		mPassTime.clear();;
	}
	
	public String getUserTimeString() {
		return mUserTimeString;
	}
	
	public long getUserTime() {
		return mUserTime;
	}

	public void saveBestPassTime(int pass, long passtime) {
		SharedPreferences sp = mContext.getSharedPreferences(SP_NAME,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putLong(KEY_PASS + pass, passtime);
		editor.commit();
	}

	public long getBestPassTime(int pass) {
		SharedPreferences sp = mContext.getSharedPreferences(SP_NAME,
				Context.MODE_PRIVATE);
		return sp.getLong(KEY_PASS + pass, 0);
	}
	
	public String format(long time) {
		return formatter.format(time);
	}
}
