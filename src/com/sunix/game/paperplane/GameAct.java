package com.sunix.game.paperplane;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GameAct extends Activity {

	GameView gameView;

	public static float sHeight = 800;
	public static float sWidth = 480;

	public static float sScreenHeight;
	public static float sScreenWidth;

	public static float sScaleX;
	public static float sScaleY;

	private RelativeLayout mMainView;

	private RelativeLayout mMeunView;

	private TextView mScoreView;
	private TextView mBestView;

	private Context mContext;

	private TextView mNextTask;

	private TextView mPassTime;

	private ListView mPassTimeList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		mContext = this;

		setContentView(R.layout.activity_main);

		DrawUtils.resetDensity(this);

		DisplayMetrics dm = new DisplayMetrics();
		dm = getResources().getDisplayMetrics();

		sScreenWidth = dm.widthPixels;
		sScreenHeight = dm.heightPixels;

		sScaleX = ((float) sScreenWidth) / sWidth;
		sScaleY = ((float) sScreenHeight) / sHeight;

		GameControler.getInstance(this).setMessageHandler(mHandler);

		initGameView();

		initTitle();

		initMeun();

	}

	private void initTitle() {
		mNextTask = (TextView) findViewById(R.id.next_task);
		mPassTime = (TextView) findViewById(R.id.pass_time);
	}

	private void initGameView() {
		mMainView = (RelativeLayout) findViewById(R.id.main_view);

		gameView = new GameView(this);

		mMainView.addView(gameView);
	}

	private void initMeun() {
		mMeunView = (RelativeLayout) findViewById(R.id.meun_view);

		mScoreView = (TextView) findViewById(R.id.score_text);
		mBestView = (TextView) findViewById(R.id.best_text);

		mMeunView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (mMeunView.getVisibility() == View.VISIBLE) {
					hideMeun();
				}
			}
		});
	}

	private void showMeun() {
		mMeunView.setVisibility(View.VISIBLE);
		int score = GameControler.getInstance(mContext).getScore();
		int best = GameControler.getInstance(mContext).getBest();
		if (score > best) {
			GameControler.getInstance(mContext).saveBest(score);
			best = score;
		}
		mScoreView.setText("" + score);
		mBestView.setText("" + best);

		HashMap<Integer, Long> passTimes = GameControler.getInstance(mContext)
				.getPassTime();

		mPassTimeList = (ListView) findViewById(R.id.pass_time_list);
		ArrayList<PassTimeBean> datas = new ArrayList<PassTimeBean>();
		for (int level = 20; level <= 100; level += 20) {
			PassTimeBean bean = new PassTimeBean();
			bean.passLevel = level;
			long yourtime = 0;
			if (passTimes.containsKey(level)) {
				yourtime = passTimes.get(level);
			}
			long besttime = GameControler.getInstance(mContext)
					.getBestPassTime(level);
			bean.yourTime = yourtime;
			if (besttime == 0 || (yourtime != 0 && yourtime < besttime)) {
				bean.bestTime = yourtime;
				GameControler.getInstance(mContext).saveBestPassTime(level,
						yourtime);
			} else {
				bean.bestTime = besttime;
			}
			datas.add(bean);
		}
		mPassTimeList.setAdapter(new PassTimeAdapter(mContext, datas));

		TranslateAnimation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT,
				0, Animation.RELATIVE_TO_PARENT, 1,
				Animation.RELATIVE_TO_PARENT, 0);
		animation.setDuration(300);
		mMeunView.startAnimation(animation);
	}

	private void hideMeun() {
		mMeunView.setVisibility(View.GONE);
		TranslateAnimation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT,
				0, Animation.RELATIVE_TO_PARENT, 0,
				Animation.RELATIVE_TO_PARENT, 1);
		animation.setDuration(300);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				GameControler.getInstance(GameAct.this).startGame();
			}
		});
		mMeunView.startAnimation(animation);
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg == null) {
				return;
			}
			switch (msg.what) {
			case GameControler.MSG_GAME_START:
				mNextTask.setText("NEXT TASK: " + 20);
				mPassTime.setText("PASS TIME: ");
				gameView.startGame();
				gameView.drawFrame();
				break;
			case GameControler.MSG_GAME_STOP:
				gameView.stopGame();
				showMeun();
				SpeedControler.sAngle = 4;
				SpeedControler.setLevel(1);
				GameControler.getInstance(mContext).setNextTask(20);
				GameControler.getInstance(mContext).setLevel(1);
				GameControler.getInstance(GameAct.this).setScore(0);
				break;
			case GameControler.MSG_UPDATA_TASK:
				// String ss = String.valueOf(usedtime%100);
				mNextTask.setText("NEXT TASK: "
						+ GameControler.getInstance(mContext).getNextTask());
				mPassTime.setText("PASS "
						+ GameControler.getInstance(mContext).getThisTask()
						+ " TIME: " + GameControler.getInstance(mContext).getUserTimeString());
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MediaManage.getInstance(this).stopBgm();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		MediaManage.getInstance(this).playBgm();
		GameControler.sGameOver = false;
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		MediaManage.getInstance(this).playBgm();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// Log.d("zxw","act ontouch:"+event.getAction());
		gameView.onTocuh(event);
		// TODO Auto-generated method stub
		return super.onTouchEvent(event);
	}
}
