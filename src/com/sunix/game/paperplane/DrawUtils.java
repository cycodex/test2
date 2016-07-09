package com.sunix.game.paperplane;

import java.lang.reflect.Method;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.ViewConfiguration;
/**
 * 绘制工具�?
 * 功能：相对屏幕百分比转化为实际像素�?，罩子层的实际显示区域， 不能分辨率下的等比转�?
 * @author jiangxuwen
 * 
 */
public class DrawUtils {
	public static float sDensity = 1.0f;
	public static int sDensityDpi;
	public static int sWidthPixels;
	public static int sHeightPixels;
	public static float sFontDensity;
	public static int sTouchSlop = 15; // 点击的最大识别距离，超过即认为是移动

	public static int sStatusHeight; // 平板中底边的状�?栏高�?
	private static Class sClass = null;
	private static Method sMethodForWidth = null;
	private static Method sMethodForHeight = null;

	private static int sScreenViewWidth = -1; // 罩子层可见区域的宽度
	private static int sScreenViewHeight = -1; // 罩子层可见区域的高度

	private static int sMiddleViewWidth = -1; // 罩子层可见区域的宽度
	private static int sMiddleViewHeight = -1; // 罩子层可见区域的高度

	public static int sMiddleViewOffsetX;

	private static float sScaleX = 1.0f; // 非默认屏幕宽度分辨率与默认分辨率的比例（默认480�?
	private static float sScaleY = 1.0f; // 非默认屏幕高度度分辨率与默认分辨率的比例（默�?00�?
	/**
	 * dip/dp转像�?
	 * 
	 * @param dipValue
	 *            dip�?dp大小
	 * @return 像素�?
	 */
	public static int dip2px(float dipVlue) {
		return (int) (dipVlue * sDensity + 0.5f);
	}

	/**
	 * 像素转dip/dp
	 * 
	 * @param pxValue
	 *            像素大小
	 * @return dip�?
	 */
	public static int px2dip(float pxValue) {
		final float scale = sDensity;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * sp �?px
	 * 
	 * @param spValue
	 *            sp大小
	 * @return 像素�?
	 */
	public static int sp2px(float spValue) {
		final float scale = sDensity;
		return (int) (scale * spValue);
	}

	/**
	 * 设置罩子层的
	 * @param width
	 * @param height
	 */
	public static void setScreenViewSize(int width, int height) {
		sScreenViewWidth = width;
		sScreenViewHeight = height;
	}

	/**
	 * <br>功能�?��:设置中间层的高与�?
	 * @param width
	 * @param height
	 */
	public static void setMiddleViewSize(int width, int height) {
		sMiddleViewHeight = height;
		sMiddleViewWidth = width;
	}

	public static int getMiddleViewWidth() {
		return sMiddleViewWidth;
	}

	public static int getMiddleViewHeight() {
		return sMiddleViewHeight;
	}

	/**
	 * px转sp
	 * 
	 * @param pxValue
	 *            像素大小
	 * @return sp�?
	 */
	public static int px2sp(float pxValue) {
		final float scale = sDensity;
		return (int) (pxValue / scale);
	}

	/**
	 * 占屏幕的横向百分比转化为实际像素�?
	 * @param percent
	 * @return
	 */
	public static int percentX2px(float percent) {
		return (int) (sScreenViewWidth * percent);
	}

	/**
	 * 占屏幕的竖向百分比转化为实际像素�?
	 * @param percent
	 * @return
	 */
	public static int percentY2px(float percent) {
		return (int) (sScreenViewHeight * percent);
	}

	/**
	 * 以X轴为基准，像素�?转为相对于当前设备的像素�?
	 * @param srcValue
	 * @return
	 */
	public static int switchByX(int srcValue) {
		if (sScaleX != 0 && sScaleX != 1) {
			return (int) (srcValue * sScaleX);
		}
		return srcValue;
	}

	/**
	 * 以Y轴为基准，像素�?转为相对于当前设备的像素�?
	 * @param srcValue
	 * @return
	 */
	public static int switchByY(int srcValue) {
		if (sScaleY != 0 && sScaleY != 1) {
			return (int) (srcValue * sScaleY);
		}
		return srcValue;
	}

	public static void resetDensity(Context context) {
		if (context != null && null != context.getResources()) {
			DisplayMetrics metrics = context.getResources().getDisplayMetrics();
			sDensity = metrics.density;
			sFontDensity = metrics.scaledDensity;
			sWidthPixels = metrics.widthPixels;
			sHeightPixels = metrics.heightPixels;
			sDensityDpi = metrics.densityDpi;
			final boolean isLand = sWidthPixels > sHeightPixels;
			final int defaultWidth = isLand ? 800 : 480;
			final int defaultHeight = isLand ? 480 : 800;
			sScaleX = sWidthPixels / defaultWidth;
			sScaleY = sHeightPixels / defaultHeight;
			try {
				final ViewConfiguration configuration = ViewConfiguration.get(context);
				if (null != configuration) {
					sTouchSlop = configuration.getScaledTouchSlop();
				}
			} catch (Error e) {
//				Log.i("DrawUtils", "resetDensity has error" + e.getMessage());
			}
		}
	}

	/*public static int getTabletScreenWidth(Context context) {
		int width = 0;
		if (context != null) {
			try {
				WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
				Display display = wm.getDefaultDisplay();
				if (sClass == null) {
					sClass = Class.forName("android.view.Display");
				}
				if (sMethodForWidth == null) {
					sMethodForWidth = sClass.getMethod("getRealWidth");
				}
				width = (Integer) sMethodForWidth.invoke(display);
			} catch (Exception e) {
			}
		}

		// Rect rect= new Rect();
		// ((Activity)
		// context).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
		// int statusbarHeight = height - rect.bottom;
		if (width == 0) {
			width = sWidthPixels;
		}

		return width;
	}*/

	/*public static int getTabletScreenHeight(Context context) {
		int height = 0;
		if (context != null) {
			WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			try {
				if (sClass == null) {
					sClass = Class.forName("android.view.Display");
				}
				if (sMethodForHeight == null) {
					sMethodForHeight = sClass.getMethod("getRealHeight");
				}
				height = (Integer) sMethodForHeight.invoke(display);
			} catch (Exception e) {
			}
		}

		// Rect rect= new Rect();
		// ((Activity)
		// context).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
		// int statusbarHeight = height - rect.bottom;
		if (height == 0) {
			height = sHeightPixels;
		}

		return height;
	}*/

	public static int getScreenViewWidth() {
		return sScreenViewWidth;
	}

	public static int getScreenViewHeight() {
		return sScreenViewHeight;
	}

	public static boolean isPort() {
		return sScreenViewWidth < sScreenViewHeight;
	}
	/**
	 * 减�?的三次曲线插�?
	 * 
	 * @param begin
	 * @param end
	 * @param t
	 *            应该位于[0, 1]
	 * @return
	 */
	public static float easeOut(float begin, float end, float t) {
		t = 1 - t;
		return begin + (end - begin) * (1 - t * t * t);
	}

}
