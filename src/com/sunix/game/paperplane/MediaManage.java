package com.sunix.game.paperplane;

import android.content.Context;
import android.media.MediaPlayer;

public class MediaManage {

	private Context mContext;
	private static MediaManage sInstance;
	
	private MediaPlayer mBgMedia;
	
	private SoundPoolManage mSoundPool;
	
	private final static int SOUND_ID_CLICK = 1;
	private final static int SOUND_ID_DEAL = 2;
	private final static int SOUND_ID_PASS = 3;
	private final static int SOUND_ID_LEVELUP = 4;
	
	public MediaManage(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
//		mClickMedia = MediaPlayer.create(mContext,R.raw.click);
//		mClearMedia = MediaPlayer.create(mContext, R.raw.clear);
		mSoundPool = new SoundPoolManage(mContext);
		mSoundPool.loadSfx(R.raw.click, SOUND_ID_CLICK);
		mSoundPool.loadSfx(R.raw.deal, SOUND_ID_DEAL);
		mSoundPool.loadSfx(R.raw.pass, SOUND_ID_PASS);
		mSoundPool.loadSfx(R.raw.levelup, SOUND_ID_LEVELUP);
		mBgMedia = MediaPlayer.create(mContext, R.raw.bgm);
	}
	
	public static MediaManage getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new MediaManage(context);
		}
		return sInstance;
	}
	
	public static MediaManage getInstance() {
		return sInstance;
	}
	
	public void playClick() {
//		mClickMedia.start();
		mSoundPool.play(SOUND_ID_CLICK, 0);
	}
	
	public void playDeal() {
//		mClearMedia.start();
		mSoundPool.play(SOUND_ID_DEAL, 0);
	}
	
	public void playPass() {
//		mClearMedia.start();
		mSoundPool.play(SOUND_ID_PASS, 0);
	}
	
	public void playLevelup() {
//		mClearMedia.start();
		mSoundPool.play(SOUND_ID_LEVELUP, 0);
	}
	
	public void playBgm() {
		mBgMedia.setLooping(true);
		mBgMedia.start();
	}
	
	public void stopBgm() {
		mBgMedia.pause();
	}
}
