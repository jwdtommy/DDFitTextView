package com.dd.views;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.ddfittextviewtest.R;

/**
 * 带右下角标的TextView，实现能塞在最后一行文字末尾就塞进去，否则就自动换行. 可在此控件中添加图标和图标描述.
 * @version V1.0
 * @author J.Tommy
 * @Date 2015.4.16
 * @QQ:659579390
 */
public class DDFitTextView extends TextView {
	private Context mContext;
	private String text;
	private int mLineY;// 当前的line

	private Paint iconPaint;// 角标画笔
	private TextPaint iconTextPaint;// 角标文字画笔

	private float lastLineWidth;// 最后一行文字的宽度

	private int iconsAndTextsWidth;// 所有的角标+文字的宽度(包括之间的间距)

	private int lastLineY;// 最后一行文字的Y坐标

	private int textHeight;// 文字的高度

	private int iconY;// 角标的Y坐标

	private int iconX;// 角标的X坐标

	private float paddingSameGroup;// 同组图标与文字之间的间距
	private float paddingDifferentGroup;// 不同组图标与文字之间的间距
	private float iconPaddingTop;// 图标距离顶端的距离

	private float iconTextSize;// 图标文字的大小
	private int iconTextColor;// 图标文字的颜色

	private int iconTextGravity;// 图标与文字的位置关系（上下居中，上对齐，下对齐）
	private int iconLastLineGravity;// 最后一行文字与图标的位置关系(上下居中，上对齐，下对齐)

	private ArrayList<Bitmap> icons;// 角标集合
	private ArrayList<String> iconTexts;// 角标搭配的文字

	/**
	 * V2.0版本再加上
	 */
	private final int GRAVITY_TOP = 0;
	private final int GRAVITY_BOTTOM = 1;
	private final int GRAVITY_CENTER_VERTICAL = 2;

	private float minIconTextWidth;// 最小宽度

	public DDFitTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;

		icons = new ArrayList<Bitmap>();
		iconTexts = new ArrayList<String>();
		TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FitTextView, 0, 0);
		initByAttributes(attributes);
		initPaint();
	}

	public DDFitTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.mContext = context;
		icons = new ArrayList<Bitmap>();
		iconTexts = new ArrayList<String>();
		TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FitTextView, defStyleAttr, 0);
		initByAttributes(attributes);
		initPaint();

	}

	protected void initByAttributes(TypedArray attributes) {
		paddingSameGroup = attributes.getDimension(R.styleable.FitTextView_iconTextPaddingSameGroup, 3);
		paddingDifferentGroup = attributes.getDimension(R.styleable.FitTextView_iconpTextPaddingDifferentGroup, 3);
		iconPaddingTop = attributes.getDimension(R.styleable.FitTextView_iconPaddingTop, 3);

		iconLastLineGravity = attributes.getInt(R.styleable.FitTextView_iconLastLineGravity, GRAVITY_BOTTOM);
		iconTextGravity = attributes.getInt(R.styleable.FitTextView_iconTextGravity, GRAVITY_BOTTOM);

		minIconTextWidth = attributes.getDimension(R.styleable.FitTextView_minIconTextWidth, 0);
		iconTextSize = attributes.getDimension(R.styleable.FitTextView_iconTextSize, 12);
		iconTextColor = attributes.getColor(R.styleable.FitTextView_iconTextColor, android.R.color.white);
	}

	// 设定多图标和多文字
	public void setIconAndText(int[] res, String... text) {
		if (res == null || text == null) {
			return;
		}

		if (res.length != text.length) {
			return;
		}
		icons.clear();
		iconTexts.clear();
		for (int i = 0; i < res.length; i++) {
			if (res[i] == 0) {
				icons.add(null);
			} else {
				Bitmap temp = BitmapFactory.decodeResource(mContext.getResources(), res[i]);// 可自行替换为更高效适用的获取图片方法
				icons.add(temp);
			}
			iconTexts.add(TextUtils.isEmpty(text[i]) ? "" : text[i]);
		}
		invalidate();
		requestLayout();
	}

	public void initPaint() {
		iconPaint = new Paint();
		iconTextPaint = new TextPaint();
		iconTextPaint.setTextSize(iconTextSize);
		iconTextPaint.setColor(iconTextColor);
		iconTextPaint.setAntiAlias(true);
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		// TODO Auto-generated method stub
		super.setText(text, type);
		this.text = (String) text;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		textHeight = getTextHeight();
		lastLineWidth = getLastLineWidth();
		lastLineY = getLastLineY();
		iconsAndTextsWidth = getIconsAndTextsWidth();
		iconY = getIconY();
		iconX = getIconX();
		if (isNeedChangeLine()) {
			setMeasuredDimension(widthMeasureSpec, getLayout().getHeight() + textHeight);
		}
	}

	private int getIconX() {
		return (int) (getLayout().getWidth() - iconsAndTextsWidth - paddingDifferentGroup);
	}

	private int getTextHeight() {
		TextPaint paint = getPaint();
		paint.setColor(getCurrentTextColor());
		paint.drawableState = getDrawableState();
		Paint.FontMetrics fm = paint.getFontMetrics();
		mLineY = 0;
		mLineY += getTextSize();
		int textHeight = (int) (Math.ceil(fm.descent - fm.ascent));
		return textHeight;
	}

	private float getLastLineWidth() {
		float lastLineWidth = 0;
		int lastLine = getLayout().getLineCount() - 1;
		int lastLineStart = getLayout().getLineStart(lastLine);
		int lastLineEnd = getLayout().getLineEnd(lastLine);
		lastLineWidth = StaticLayout.getDesiredWidth(text, lastLineStart, lastLineEnd, getPaint());
		return lastLineWidth;
	}

	private int getLastLineY() {
		int lastLineY = 0;
		for (int i = 0; i < getLayout().getLineCount(); i++) {
			if (i == getLayout().getLineCount() - 1) {
				lastLineY = mLineY - textHeight / 2;
			}
			mLineY += textHeight;
		}
		return lastLineY;
	}

	private boolean isNeedChangeLine() {
		if (lastLineWidth + paddingDifferentGroup + iconsAndTextsWidth > getLayout().getWidth()) {
			return true;
		}
		return false;
	}

	private int getIconY() {
		if (isNeedChangeLine()) {
			// return lastLineY + textHeight;
			return (int) (lastLineY + iconPaddingTop + textHeight);
		}
		return (int) (lastLineY + iconPaddingTop);
	}

	private void drawIconsAndTexts(Canvas canvas, TextPaint iconTextPaint) {
		int x = getWidth();
		int y = iconY;
		for (int i = 0; i < icons.size(); i++) {
			drawIconAndText(canvas, icons.get(i), iconTexts.get(i), x, y);
			x -= getIconAndTextWidth(icons.get(i), iconTexts.get(i), iconTextPaint);
		}
	}

	/**
	 * 格式 -icon-text-icon-text-icon-text- 以icon-text-为一个单元
	 * 
	 * @return
	 */
	private int getIconsAndTextsWidth() {
		int width = 0;
		for (int i = 0; i < icons.size(); i++) {
			width += getIconAndTextWidth(icons.get(i), iconTexts.get(i), iconTextPaint);
		}
		return width;
	}

	private int getIconAndTextWidth(Bitmap icon, String text, TextPaint iconTextPaint) {
		int iconWidth;
		if (icon == null) {
			iconWidth = 0;
		} else {
			iconWidth = icon.getWidth();
		}

		int iconTextWidth = (int) StaticLayout.getDesiredWidth(text, 0, text.length(), iconTextPaint);
		if (iconTextWidth < minIconTextWidth) {
			iconTextWidth = (int) minIconTextWidth;
		}

		return (int) (iconWidth + paddingSameGroup + iconTextWidth + paddingDifferentGroup);
	}

	private void drawIconAndText(Canvas canvas, Bitmap icon, String text, int x, int y) {
		int iconWidth = (icon == null ? 0 : icon.getWidth());
		int iconHeight = (icon == null ? 0 : icon.getHeight());
		int iconTextWidth = (int) StaticLayout.getDesiredWidth(text, 0, text.length(), iconTextPaint);
		if (iconTextWidth < minIconTextWidth) {
			iconTextWidth = (int) minIconTextWidth;
		}
		// if (icon != null) {
		// canvas.drawBitmap(icon, paddingDifferentGroup + x, y, iconPaint);
		// }
		// canvas.drawText(text, x + iconWidth + paddingDifferentGroup +
		// paddingSameGroup, y + iconHeight, iconTextPaint);
		canvas.drawText(text, x - iconTextWidth - paddingDifferentGroup, y + iconHeight, iconTextPaint);
		if (icon != null) {
			canvas.drawBitmap(icon, x - paddingDifferentGroup - iconTextWidth - paddingSameGroup - iconWidth, y, iconPaint);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawIconsAndTexts(canvas, iconTextPaint);
	}

}
