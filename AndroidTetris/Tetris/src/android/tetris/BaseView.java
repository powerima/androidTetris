package android.tetris;

import android.content.*;
import android.graphics.*;
import android.view.*;

public class BaseView extends View {
	
	public BaseView(Context context) {
		super(context);
	}

	// 채우지 않은 사각형 그리기
	public void drawLineRect(Canvas canvas, Rect rt, Paint pnt) {
		float[] f1 = { rt.left, rt.top, rt.right, rt.top };
		float[] f2 = { rt.left, rt.top, rt.left, rt.bottom };
		float[] f3 = { rt.left, rt.bottom, rt.right, rt.bottom };
		float[] f4 = { rt.right, rt.top, rt.right, rt.bottom };

		canvas.drawLines(f1, pnt);
		canvas.drawLines(f2, pnt);
		canvas.drawLines(f3, pnt);
		canvas.drawLines(f4, pnt);
	}
}
