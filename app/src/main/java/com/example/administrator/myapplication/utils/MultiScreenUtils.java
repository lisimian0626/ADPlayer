package com.example.administrator.myapplication.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.widget.TextView;

public class MultiScreenUtils {
	private static final String TAG = "MultiScreenUtils";

	/**
	 * calculate screenRatio by width factor
	 */
	private static final int RATIO_FACTOR_WIDTH = 1;
	
	/**
	 * calculate screenRatio by height factor
	 */
	private static final int RATIO_FACTOR_HEIGHT = 2;
	
	/**
	 * calculate screenRatio by both width and height factors, the size
	 * of the view may be morphed
	 */
	private static final int RATIO_FACTOR_ALL = 3;
	
	private static final int STANDARD_SCREEN_WIDTH = 1080;
	private static final int STANDARD_SCREEN_HEIGHT = 1920;
	
	// calculate screen ratio using STANDARD_WIDTH
	private static int STANDARD_SIZE = STANDARD_SCREEN_HEIGHT;
	private static float screenRatio = 1;  // default ratio is 1, rightly like a resolution with 1920*1080
	private static int targetSize;
	private static int ratioFactor = RATIO_FACTOR_HEIGHT;
	
	private static boolean initialized = false;
	
	
	public static void init(Context context) {
		Point displaySize = getDisplaySize(context);
		if (ratioFactor == RATIO_FACTOR_HEIGHT) {
			STANDARD_SIZE = STANDARD_SCREEN_HEIGHT;
			targetSize = displaySize.y;
		} else if (ratioFactor == STANDARD_SCREEN_WIDTH) {
			STANDARD_SIZE = STANDARD_SCREEN_WIDTH;
			targetSize = displaySize.x;
		}
		screenRatio = (float) targetSize / (float) STANDARD_SIZE;
		
		DisplayMetrics dm = context.getResources().getDisplayMetrics();

		Log.i(TAG, "width:"+dm.widthPixels+"heigh:"+dm.heightPixels+"init, targetSize: " + targetSize + ", ratio: " + screenRatio + ", density: " + dm.density + ", scaledDensity: " + dm.scaledDensity);
		initialized = true;
	}

	/**
	 * input a real size, output a scaled size, both sizes are without density
	 * @param context
	 * @param size
	 * @return
	 */
	public static int getScaledSize(Context context, int size) {
		if (!initialized) {
			init(context);
		}
		return getScaledSize(size);
	}
	
	public static float getScaled(Context context) {
		if (!initialized) {
			init(context);
		}
		return screenRatio;
	}
	
	/**
	 * input a real size, output a scaled size, both sizes are without density
	 * @param size
	 * @return
	 */
	private static int getScaledSize(int size) {
		if (size < 0) {
			return size;
		}
		int scaledSize = Math.round((float) size * screenRatio);
		return scaledSize;
	}
	
	public static void resizeView(View view) {
		if (view == null || view.getLayoutParams() == null) {
			return;
		}
		if (!initialized) {
			init(view.getContext());
		}
		ViewGroup.LayoutParams params = view.getLayoutParams();
		
		params.width = getScaledSize(params.width);
		params.height = getScaledSize(params.height);
		if (params instanceof MarginLayoutParams) {
			MarginLayoutParams mParams = (MarginLayoutParams) params;
			mParams.leftMargin = getScaledSize(mParams.leftMargin);
			mParams.topMargin = getScaledSize(mParams.topMargin);
			mParams.rightMargin = getScaledSize(mParams.rightMargin);
			mParams.bottomMargin = getScaledSize(mParams.bottomMargin);
		}
		int left = getScaledSize(view.getPaddingLeft());
		int top = getScaledSize(view.getPaddingTop());
		int right = getScaledSize(view.getPaddingRight());
		int bottom = getScaledSize(view.getPaddingBottom());
		view.setPadding(left, top, right, bottom);
		if (view instanceof TextView){
			((TextView) view).setTextSize(getScaledSize((int) ((TextView) view).getTextSize()));
		}
	}
	
	public static void resizeViews(View view) {
		if (view == null) {
			return;
		}
		resizeView(view);
		ViewGroup container;
		if (view instanceof ViewGroup) {
			container = (ViewGroup) view;
			int count = container.getChildCount();
			for (int i = 0; i < count; i++) {
				View child = container.getChildAt(i);
				resizeViews(child);
			}
		}
	}

	public static void resizeDrawable(Context context, Drawable drawable) {
		Log.i(TAG, "resizeDrawable, rect: " + drawable.getBounds());
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		int sw = MultiScreenUtils.getScaledSize(context, width);
		int sh = MultiScreenUtils.getScaledSize(context, height);
		drawable.setBounds(0, 0, sw, sh);
	}
	/**
     * Returns the screen/display size
     */
    @SuppressLint("NewApi")
    public static Point getDisplaySize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static int convertDpToPixel(Context ctx, int dp) {
        float density = ctx.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

}
