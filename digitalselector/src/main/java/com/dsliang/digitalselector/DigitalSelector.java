package com.dsliang.digitalselector;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by dsliang on 2016/10/26.
 */
public class DigitalSelector extends View {

    static final String TAG = DigitalSelector.class.getSimpleName();

    //默认显示的数字
    static final private int DEFAULT_DISPLAY_NUMBER = 99;

    //默认字体大小(48px)
    static final private int DEFAULT_TEXT_SIZE = 24;
    //默认选中时候背景色颜色(红色)
    static final private int DEFAULT_SELECTED_COLOR = 0xFFFF0000;
    //选中时候字体颜色(白色)
    static final private int DEFAULT_TEXT_COLOR_SELECTED = 0xFFFFFFFF;
    //未选中时候字体颜色((灰色)
    static final private int DEFAULT_TEXT_COLOR_NOT_SELECTED = 0xFF7D7D7D;
    //内部文字和背景色的间隔(默认20px)
    static final private int DEFAULT_INSIDE_MARGIN = 10;

    //数字支持的最大限度.过大也是没意义的(设置为99).当然是从0到MAXIMUM_VALUE.负数也是没多少意义的.
    static final private int MAXIMUM_VALUE = 99;
    static final private int MINIMUM_VALUE = 0;
    //溢出时候显示的东西(显示三个点 ...)
    static final private String OVERFLOW_WILL_DISPLAY = "...";

    private Context mContext;

    //显示的数字
    private int mNumber = DEFAULT_DISPLAY_NUMBER;
    //字体大小
    private int mTextSize = DEFAULT_TEXT_SIZE;
    //选中时候的背景
    private int mSelectedColor = DEFAULT_SELECTED_COLOR;
    //选中时候的字体颜色
    private int mTextColorSelected = DEFAULT_TEXT_COLOR_SELECTED;
    //未选中时候的字体颜色
    private int mTextColorNotSelected = DEFAULT_TEXT_COLOR_NOT_SELECTED;

    //文字画笔
    private Paint mPaintText;

    private int mInsideMargin = DEFAULT_INSIDE_MARGIN;

    //是否选择
    private boolean mIsSelected = false;

    private OnStateChangeListener mListener;

    private OnClickListener mClickListener;

    /**
     * 获取字体大小
     *
     * @return
     */
    public int getTextSize() {
        return mTextSize;
    }

    /**
     * 设置字体大小
     *
     * @param size
     */
    public void setTextSize(int size) {
        if (mTextSize == size)
            return;

        mTextSize = size;
        initPaintattributes();
        requestLayout();
    }

    /**
     * 获取数字
     *
     * @return
     */
    public int getNumber() {
        return mNumber;
    }

    /**
     * 设置数字
     *
     * @param number
     */
    public void setNumber(int number) {
        if (mNumber == number)
            return;

        mNumber = number;
        reDraw();
    }

    /**
     * 获取选中后的背景色
     *
     * @return
     */
    public int getSelectedColor() {
        return mSelectedColor;
    }

    /**
     * 设置选中时候的背景色
     *
     * @param color
     */
    public void setSelectedColor(int color) {
        if (mSelectedColor == color)
            return;

        mSelectedColor = color;
        reDraw();
    }

    /**
     * 获取选中时候的字体颜色
     */
    public int getTextColorSelected() {
        return mTextColorSelected;
    }

    /**
     * 设置选中时候的字体颜色
     *
     * @param color 颜色
     */
    public void setTextColorSelected(int color) {
        if (mTextColorSelected == color)
            return;

        mTextColorSelected = color;
        reDraw();
    }

    /**
     * 获取未选中时候的字体颜色
     */
    public int getTextColorNotSelected() {
        return mTextColorNotSelected;
    }

    /**
     * 设置未选中时候的字体颜色
     *
     * @param color 颜色
     */
    public void setTextColorNotSelected(int color) {
        if (mTextColorNotSelected == color)
            return;

        mTextColorNotSelected = color;
        reDraw();
    }

    /**
     * 设置是否给选中
     */
    public void setIsSelected(boolean isSelected) {
        if (mIsSelected == isSelected)
            return;

        mIsSelected = isSelected;

        //如果设置了监听函数就调用
        if (null != mListener)
            mListener.onStateChange(DigitalSelector.this, getNumber(), getIsSelected());

        reDraw();
    }

    /**
     * 获取是否给选中
     *
     * @return
     */
    public boolean getIsSelected() {
        return mIsSelected;
    }

    /**
     * 获取内部间距大小
     *
     * @return
     */
    public int getInsideMargin() {
        return mInsideMargin;
    }

    /**
     * 设置内部间距大小
     *
     * @param margin
     */
    public void settInsideMargin(int margin) {
        if (mInsideMargin == margin)
            return;

        mInsideMargin = margin;
        //需要重新布局
        requestLayout();
    }

    public DigitalSelector(Context context) {
        this(context, null);
    }

    public DigitalSelector(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DigitalSelector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        mContext = context;

        TypedArray a;
        int indexCount;

        a = context.obtainStyledAttributes(attrs, R.styleable.DigitalSelector);
        indexCount = a.getIndexCount();

        //获取属性
        for (int i = 0; i < indexCount; i++) {
            int attr;

            attr = a.getIndex(i);
            if (R.styleable.DigitalSelector_number == attr) {
                //数字
                mNumber = a.getInt(attr, DEFAULT_DISPLAY_NUMBER);
            } else if (R.styleable.DigitalSelector_selectedColor == attr) {
                //选中时候的背景色
                mSelectedColor =
                        a.getColor(attr, DEFAULT_SELECTED_COLOR);
            } else if (R.styleable.DigitalSelector_textSize == attr) {
                //字体大小
                mTextSize =
                        (int) a.getDimension(attr, DEFAULT_TEXT_SIZE);
            } else if (R.styleable.DigitalSelector_textColorSelected == attr) {
                //选中时候的字体颜色
                mTextColorSelected =
                        a.getColor(R.styleable.DigitalSelector_selectedColor, DEFAULT_TEXT_COLOR_SELECTED);
            } else if (R.styleable.DigitalSelector_textColorNotSelected == attr) {
                //未选中时候的字体颜色
                mTextColorNotSelected =
                        a.getColor(R.styleable.DigitalSelector_textColorNotSelected, DEFAULT_TEXT_COLOR_NOT_SELECTED);
            } else if (R.styleable.DigitalSelector_insideMargin == attr) {
                mInsideMargin =
                        (int) a.getDimension(attr, DEFAULT_INSIDE_MARGIN);
            }
        }

        a.recycle();

        initPaintattributes();
        setupListerner();
    }

    /**
     * 初始化画笔属性
     */
    private void initPaintattributes() {

        if (null == mPaintText) {
            //初始化画笔
            mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        }

        //设置画笔文字大小
        mPaintText.setTextSize(mTextSize);

        //设置画笔绘制锚点
        mPaintText.setTextAlign(Paint.Align.CENTER);
    }

    /**
     * 获取当前的字体颜色
     *
     * @return 颜色
     */
    private int getCurrentTextColor() {
        if (mIsSelected) {
            return mTextColorSelected;
        } else {
            return mTextColorNotSelected;
        }
    }

    /**
     * 分别计算显示时候所需的宽和高,返回宽,高中的最大值.(因为需要显示成为一个方向,所以返回最大值)
     *
     * @return 实制需要的尺寸
     */
    private int getRequiredLength() {

        int width;
        int height;
        int result;

        //使用MAXIMUM_VALUE和OVERFLOW_WILL_DISPLAY计算宽度.取最大值
        width = Math.max(measureNumberWidth(MAXIMUM_VALUE, getTextSize()), measureStringWidth(OVERFLOW_WILL_DISPLAY, getTextSize()));

        //使用MAXIMUM_VALUE和OVERFLOW_WILL_DISPLAY计算高度.取最大值
        height = Math.max(measureNumberHeight(MAXIMUM_VALUE, getTextSize()), measureStringHeight(OVERFLOW_WILL_DISPLAY, getTextSize()));

        //使用其中最大值计算控件宽高
        result = Math.max(width, height);

        //设置内部文字和背景色的间距
        result += mInsideMargin * 2;

        return result;
    }

    /**
     * 强制重新绘制视图
     */
    public void reDraw() {
        invalidate();
    }

    /**
     * 计算数字长度(指定某个字体大小时候的长度)
     *
     * @param number   数字
     * @param textSize 字体大小
     * @return 宽度
     */
    private int measureNumberWidth(int number, int textSize) {
        return measureStringWidth(String.valueOf(number), textSize);
    }

    /**
     * 计算字符串长度(指定某个字体大小时候的长度)
     *
     * @param str      字符串
     * @param textSize 字体大小
     * @return 长度
     */
    private int measureStringWidth(String str, int textSize) {

        Paint paint;
        int result;
        Rect rect;

        result = 0;

        //str为空或者textSize为0,返回0
        if (TextUtils.isEmpty(str) || 0 == textSize)
            return result;

        //计算宽度
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);

        rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        result = rect.width();

        return result;
    }


    /**
     * 计算数字高度(指定某个字体大小时候的高度)
     *
     * @param number   数字
     * @param textSize 字体大小
     * @return 高度
     */
    private int measureNumberHeight(int number, int textSize) {
        return measureStringHeight(String.valueOf(number), textSize);
    }

    /**
     * 计算字符串高度(指定某个字体大小时候的高度)
     *
     * @param str      文字
     * @param textSize 字体大小
     * @return 高度
     */
    private int measureStringHeight(String str, int textSize) {

        Paint paint;
        int result;
        Rect rect;

        result = 0;

        //str为空或者textSize为0,返回0
        if (TextUtils.isEmpty(str) || 0 == textSize)
            return result;

        //计算宽度
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);

        rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        result = rect.height();

        return result;
    }

    /**
     * 测量数字的宽度
     *
     * @param measureSpec 限制和模式
     * @return 宽度
     */
    private int getMeasureWidth(int measureSpec) {

        int size;
        int mode;
        int width;
        int result;

        result = 0;

        size = MeasureSpec.getSize(measureSpec);
        mode = MeasureSpec.getMode(measureSpec);

        //计算所需的边长
        width = getRequiredLength();

        //分别根据三种模式返回相应的长度
        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                //UNSPECIFIED模式随意玩.准确需要多少就返回多少
                result = width;
                break;
            case MeasureSpec.AT_MOST:
                //AT_MOST模式就要节省一点了.父视图只有size这么大了.返回多少呢?不要超出size大小就对了.
                result = Math.min(width, size);
                break;
            //如果是精确值,那么我们就从了吧.放弃无为的反抗.
            case MeasureSpec.EXACTLY:
                result = size;
                break;
        }

        return result;
    }

    /**
     * 测量数字的高度,使用和测量宽度一样的方式
     *
     * @param measureSpec 限制和模式
     * @return 高度
     */
    private int getMeasureHeight(int measureSpec) {

        return getMeasureWidth(measureSpec);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width, height;

        //计算宽高
        width = getMeasureWidth(widthMeasureSpec);
        height = getMeasureHeight(heightMeasureSpec);

        Log.i(TAG, "measureWidth: " + width + " measureHeight: " + height);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawSelectedColor(canvas);
        drawNumber(canvas);
    }

    /**
     * 绘制数字
     *
     * @param canvas 画布
     */
    private void drawNumber(Canvas canvas) {

        String s;
        int x;
        int y;

        if (null == canvas)
            return;

        mPaintText.setColor(getCurrentTextColor());
        s = getNumber() > MAXIMUM_VALUE ? OVERFLOW_WILL_DISPLAY : String.valueOf(getNumber());
        x = canvas.getWidth() / 2;
        y = canvas.getHeight() / 2;
        drawTextInCenter(s, x, y, mPaintText, canvas);
    }

    /**
     * 以给出的坐标为中心点绘制文字
     *
     * @param text   文字
     * @param x      x坐标
     * @param y      y坐标
     * @param paint  画笔
     * @param canvas 画布
     */
    private void drawTextInCenter(@NonNull String text, int x, int y, @NonNull Paint paint, @NonNull Canvas canvas) {
        int exactX;
        int exactY;
        int offset;
        Paint patinCenter;
        Paint.FontMetrics metrics;

        metrics = paint.getFontMetrics();

        /*
        top             _______________________
        ascent          _______________________



        baseline        _______________________             基准线,baselin以下为负数,以上为正数
        descent         _______________________
        bottom          _______________________
         */

        exactX = x;

        offset = (int) ((Math.abs(metrics.top) - Math.abs(metrics.bottom)) / 2);
        exactY = y;
        exactY += offset;

        //新建一个画笔,确保Align是Paint.Align.CENTER
        patinCenter = new Paint(paint);
        patinCenter.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, exactX, exactY, patinCenter);
    }

    /**
     * 绘制背景色
     *
     * @param canvas 画布
     */
    private void drawSelectedColor(Canvas canvas) {

        int width;
        int height;
        Paint paint;

        //如果没有选中直接返回,无需绘制背景色
        if (!mIsSelected)
            return;

        width = canvas.getWidth();
        height = getHeight();

        //长,宽其一为0直接返回
        if (0 == width || 0 == height)
            return;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //实心方式填充
        paint.setStyle(Paint.Style.FILL);
        //背景色
        paint.setColor(mSelectedColor);
        canvas.drawCircle(width / 2, height / 2, Math.max(width, height) / 2, paint);
    }

    private OnClickListener patchClickListener;

    /**
     * 设置监听函数
     */
    private void setupListerner() {
        setOnClickListener(null);
    }

    /**
     * 重写监听函数
     *
     * @param l
     */
    @Override
    public void setOnClickListener(OnClickListener l) {
        mClickListener = l;

        if (null == patchClickListener) {
            patchClickListener =
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (null != mClickListener)
                                mClickListener.onClick(v);

                            //改变状态
                            setIsSelected(!getIsSelected());
                        }
                    };
            super.setOnClickListener(patchClickListener);
        }
    }

    /**
     * 设置监听函数
     *
     * @param listener
     */
    public void setSelectedListener(OnStateChangeListener listener) {
        mListener = listener;

    }

    interface OnStateChangeListener {
        public void onStateChange(View view, int number, boolean isSelected);
    }
}
