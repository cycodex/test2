package com.sunix.game.paperplane;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class BmpManage {

	public static final int STYLE_TUBE_LEFT_LONG = 1;
	public static final int STYLE_TUBE_RIGHT_LONG = 2;
	public static final int STYLE_TUBE_LEFT_SHORT = 3;
	public static final int STYLE_TUBE_RIGHT_SHORT = 4;

	private Context mContext;

	private static BmpManage sSelf = null;

	private HashMap<String, Bitmap> mBmpMap;

	private ArrayList<Bitmap> mNumFrames;

	private Bitmap mScoreBitmap;

	private int mScore;

	public BmpManage(Context context) {
		mContext = context;
		mBmpMap = new HashMap<String, Bitmap>();
	}

	public static BmpManage getInstance(Context context) {
		if (sSelf == null) {
			sSelf = new BmpManage(context);
		}
		return sSelf;
	}

	public Bitmap getBitmap(String name) {
		Bitmap bitmap = null;
		if (mBmpMap.containsKey(name)) {
			bitmap = mBmpMap.get(name);
		} else {
			bitmap = createBmp(name);
			mBmpMap.put(name, bitmap);
		}
		return bitmap;
	}

	public Bitmap getTubeBitmap(int style) {
		Bitmap bitmap = null;
		int level = GameControler.getInstance(mContext).getLevel();
		String filename = "";
		switch (style) {
		case STYLE_TUBE_LEFT_LONG:
			filename = "tubel_long";
			break;
		case STYLE_TUBE_RIGHT_LONG:
			filename = "tuber_long";
			break;
		case STYLE_TUBE_LEFT_SHORT:
			filename = "tubel_short";
			break;
		case STYLE_TUBE_RIGHT_SHORT:
			filename = "tuber_short";
			break;
		default:
			break;
		}

		if (level > 1) {
			filename = filename + "_big.png";
		} else {
			filename = filename + ".png";
		}
		
		bitmap = getBitmap(filename);
		
		return bitmap;
	}

	public Bitmap createBmp(String name) {
		InputStream assetFile;
		Bitmap bitmap = null;
		try {
			assetFile = mContext.getAssets().open(name);
			bitmap = BitmapFactory.decodeStream(assetFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Matrix matrix = new Matrix();
		// float[] scale = getScale(bitmap, name);
		// matrix.postScale(scale[0], scale[1]);
		// �õ��µ�ͼƬ
		Bitmap newbm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);

		return newbm;
	}

	public Bitmap getScoreBitmap(int score) {
		if (score == mScore && mScoreBitmap != null) {
			return mScoreBitmap;
		}
		mScore = score;
		if (mNumFrames == null) {
			mNumFrames = GenerateScoreBitmaps();
		}
		mScoreBitmap = nestBitmap(mNumFrames, mScore);
		return mScoreBitmap;
	}

	private ArrayList<Bitmap> GenerateScoreBitmaps() {
		Bitmap resImage = getBitmap("scorenum.png");
		if (resImage == null) {
			return null;
		}

		ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>(10);

		for (int index = 0; index < 10; ++index) {
			try {
				Bitmap frame = Bitmap.createBitmap(resImage, index * 22, 0, 22,
						26);
				bitmaps.add(frame);
			} catch (IllegalArgumentException e) {
				// TODO: handle exception
			}
		}
		return bitmaps;
	}

	private Bitmap nestBitmap(ArrayList<Bitmap> frames, int num) {
		if (frames == null || frames.size() <= 0) {
			return null;
		}
		int listSize = 1;
		ArrayList<Integer> numList = new ArrayList<Integer>();
		if (num == 0) {
			numList.add(0);
		} else {
			while (num > 0) {
				numList.add(num % 10);
				num /= 10;
			}
			listSize = numList.size();
		}
		Bitmap bitmapEx = frames.get(0);
		int bmpWidth = bitmapEx.getWidth();
		int width = bmpWidth * listSize;
		int height = bitmapEx.getHeight();
		Bitmap result = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(result);
		int framesSize = frames.size();
		for (int i = 0; i < listSize; i++) {
			int n = numList.get(i);
			if (n >= framesSize) {
				n = 0;
			}
			canvas.drawBitmap(frames.get(n), width - (bmpWidth * (i + 1)), 0,
					null);
		}
		return result;
	}
}
