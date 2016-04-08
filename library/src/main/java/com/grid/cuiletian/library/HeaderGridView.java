package com.grid.cuiletian.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.RelativeLayout;

import java.util.List;

/**
 * Created by cuiletian on 15/11/11.
 */

public class HeaderGridView extends RelativeLayout {

    private int mColumnWidth;
    private int mHeaderRowHeight;
    private int mHeaderColumnWidth;
    private int mHeaderColumnHeight;
    private final float mHorizontalDivider = 1;
    private final float mVerticalDivider = 1;
    private int mDateMargin = 8;

    private final PointF mCurrentOrigin = new PointF(0f, 0f);
    private Paint mHeaderBackGroundPainter;
    private Paint mStrokePainter;
    private TextPaint mHolidayTextPaint;
    private TextPaint mNamePaint;
    private TextPaint mWeekDayTextPaint;
    private int mDateTextSize;
    private int mDirtyTextSize;
    private int mRoomNumTextSize;

    private int mNumberOfVisibleDays;
    private float mTextWidth = 0;
    private float mTextHeight = 0;
    private float mDayOfWeekWidth;
    private final Context mContext;

    private GridView grid;
    private List<String> tittles;
    private List<Day> days;
    private NoInterceptHorizontalScrollView myscrollView;
    private FrameLayout stateFrameLayout;

    private TittleClickListener mTittleClickListener;
    private DrawOverListener mDrawOverListener;
    private boolean isDataLoadOver = false;


    public HeaderGridView(Context context) {
        this(context, null);
    }

    public HeaderGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderGridView(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        TypedArray a = mContext.getTheme().obtainStyledAttributes(attrs, R.styleable.HeaderGridView, 0, 0);
        try {
            mColumnWidth = a.getDimensionPixelSize(R.styleable.HeaderGridView_columnWidth, mColumnWidth);
            mHeaderColumnWidth = a.getDimensionPixelOffset(R.styleable.HeaderGridView_columnWidth, mHeaderColumnWidth);
            mHeaderRowHeight = a.getDimensionPixelOffset(R.styleable.HeaderGridView_header_row_height, mHeaderRowHeight);
            mHeaderColumnHeight = a.getDimensionPixelOffset(R.styleable.HeaderGridView_header_column_height, mHeaderRowHeight) + 1;
            mDateMargin = a.getDimensionPixelOffset(R.styleable.HeaderGridView_date_margin, mDateMargin);
            mDateTextSize = a.getDimensionPixelSize(R.styleable.HeaderGridView_date_txt_size, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mDateTextSize, context.getResources().getDisplayMetrics()));
            mDirtyTextSize = a.getDimensionPixelSize(R.styleable.HeaderGridView_dirty_txt_size, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mDirtyTextSize, context.getResources().getDisplayMetrics()));
            mRoomNumTextSize = a.getDimensionPixelSize(R.styleable.HeaderGridView_room_num_txt_size, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mRoomNumTextSize, context.getResources().getDisplayMetrics()));
        } finally {
            a.recycle();
        }
        setWillNotDraw(false);
        LayoutInflater.from(mContext).inflate(R.layout.header_grid, this, true);
        init();
    }

    private void init() {

        mHeaderBackGroundPainter = new Paint();
        mHeaderBackGroundPainter.setColor(getResources().getColor(R.color.header_background));
        mHeaderBackGroundPainter.setAntiAlias(true);
        mStrokePainter = new Paint();
        mStrokePainter.setColor(getResources().getColor(R.color.grey_9e));
        mStrokePainter.setAntiAlias(true);
        mNamePaint = new TextPaint();
        mNamePaint.setColor(getResources().getColor(R.color.grey_3));
        mNamePaint.setAntiAlias(true);
        mWeekDayTextPaint = new TextPaint();
        mWeekDayTextPaint.setColor(getResources().getColor(R.color.grey_a));
        mWeekDayTextPaint.setAntiAlias(true);
        mDateTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 13, getResources().getDisplayMetrics());
        float textSize_12 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics());
        float textSize_10 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        mNamePaint.setTextSize(mDateTextSize);
        mWeekDayTextPaint.setTextSize(textSize_12);
        mHolidayTextPaint = new TextPaint();
        mHolidayTextPaint.setTextSize(mDateTextSize);
        mHolidayTextPaint.setAntiAlias(true);
        mHolidayTextPaint.setColor(getResources().getColor(R.color.red_hotel_remark));

        mTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
        grid = (GridView) findViewById(R.id.content_grid);

        grid.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                View v = grid.getChildAt(0);
                if (v != null) {
                    mCurrentOrigin.y = -(firstVisibleItem / grid.getNumColumns() * mHeaderColumnHeight - v.getTop());//上负下正
                    invalidate();
                }
            }
        });

        stateFrameLayout = (FrameLayout) findViewById(R.id.state_frame);
        myscrollView = (NoInterceptHorizontalScrollView) findViewById(R.id.myscrollView);
        myscrollView.setOnScrollChangedListener(new NoInterceptHorizontalScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                mCurrentOrigin.x = -l;
                invalidate();
            }
        });

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins((int) (mColumnWidth + mHorizontalDivider), (int) (mHeaderRowHeight + mVerticalDivider), 0, 0);
        stateFrameLayout.setLayoutParams(params);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw the header row.上方行
        drawHeaderRow(canvas);
        // Draw the time column and all the axes/separators.画左侧的列和所有的分割线
        drawDateColumnAndEvents(canvas);
        // Hide everything in the first cell (top left corner).左上角的空白方块
        canvas.drawRect(0, 0, mHeaderColumnWidth, mHeaderRowHeight, mHeaderBackGroundPainter);
        //横竖边界线
        canvas.drawRect(0, mHeaderRowHeight, getWidth(), mHeaderRowHeight + mVerticalDivider, mStrokePainter);
        canvas.drawRect(mHeaderColumnWidth, 0, mHeaderColumnWidth + mHorizontalDivider, getHeight(), mStrokePainter);

        if (mDrawOverListener != null && isDataLoadOver) {
            mDrawOverListener.onDrawOver();
            isDataLoadOver = false;
        }
    }

    private void drawHeaderRow(Canvas canvas) {

        int mFirstVisibleTittle = (int) Math.floor((-mCurrentOrigin.x) / mColumnWidth);
        int mVisibleTittleNum = 0;
        if (tittles != null && tittles.size() != 0) {
            mVisibleTittleNum = (int) Math.ceil((getHeight() - mHeaderColumnWidth) / mColumnWidth);
            mVisibleTittleNum = mVisibleTittleNum < tittles.size() ? mVisibleTittleNum : tittles.size() + 1;
        }
        // 最上方一行的背景
        canvas.drawRect(0, 0, getWidth(), mHeaderRowHeight, mHeaderBackGroundPainter);

        // 第一行的房间名
        for (int tittleNum = mFirstVisibleTittle; tittleNum < mFirstVisibleTittle + mVisibleTittleNum; tittleNum++) {
            int left = (int) (mHeaderColumnWidth + (int) mCurrentOrigin.x + mColumnWidth * tittleNum + mHorizontalDivider * tittleNum);//mCurrentOrigin.x < 0

            //分割线
            canvas.drawRect(left, 0, left + mHorizontalDivider, mHeaderRowHeight, mStrokePainter);

            String tittleStr = "";
            if (tittles != null && tittleNum < tittles.size()) {
                tittleStr = tittles.get(tittleNum);
            }

            if (left < getWidth() && tittleNum < tittles.size()) {
                Rect rect = new Rect();
                mNamePaint.getTextBounds(tittleStr, 0, tittleStr.length(), rect);
                mTextWidth = mNamePaint.measureText(tittleStr);
                mTextHeight = rect.height();
                canvas.drawText(tittleStr, left + (mColumnWidth - mTextWidth) / 2, (mHeaderRowHeight - mTextHeight) / 2 + mTextHeight, mNamePaint);
            }
        }
    }

    private void drawDateColumnAndEvents(Canvas canvas) {

        int aboveDaysWithGaps = (int) (Math.floor(-mCurrentOrigin.y / (mHeaderColumnHeight)));//从第几个格子开始画，取较小值
        float startPixel = mCurrentOrigin.y + (mHeaderColumnHeight) * aboveDaysWithGaps + mHeaderRowHeight;

        // 日期背景
        canvas.drawRect(0, 0, mHeaderColumnWidth, getHeight(), mHeaderBackGroundPainter);
        mNumberOfVisibleDays = (getHeight() - mHeaderRowHeight) / mHeaderColumnHeight;

        // 左侧日期文字和分割线等
        for (int dayNumber = aboveDaysWithGaps; dayNumber <= aboveDaysWithGaps + mNumberOfVisibleDays + 1; dayNumber++) {

            boolean isToday = false;
            //分割线
            canvas.drawRect(0, startPixel + mHeaderColumnHeight, mHeaderColumnWidth, startPixel + mHeaderColumnHeight + mHorizontalDivider, mStrokePainter);

            // Draw the day labels.
            // 画日期
            String dateLabel = "";
            String dayOfWeek = "";
            boolean isHoliday = false;
            if (days != null && dayNumber >= 0 && dayNumber < days.size()) {
                dateLabel = days.get(dayNumber).getDateDisplay();
                isHoliday = days.get(dayNumber).isHoliday();
                dayOfWeek = days.get(dayNumber).getDayOfWeek();
            }

            Rect rect = new Rect();
            mNamePaint.getTextBounds(dateLabel, 0, dateLabel.length(), rect);
            mTextWidth = mNamePaint.measureText(dateLabel);
            mTextHeight = rect.height();

            mWeekDayTextPaint.getTextBounds(dayOfWeek, 0, dayOfWeek.length(), rect);
            mDayOfWeekWidth = mWeekDayTextPaint.measureText(dayOfWeek);

            canvas.drawText(dateLabel, ScreenUtil.dip2px(mContext, mDateMargin), startPixel + ScreenUtil.dip2px(mContext, mDateMargin) + mTextHeight, mNamePaint);
            canvas.drawText(dayOfWeek, ScreenUtil.dip2px(mContext, mDateMargin), startPixel + ScreenUtil.dip2px(mContext, mDateMargin) + mTextHeight + ScreenUtil.dip2px(mContext, mDateMargin) + mTextHeight, isHoliday ? mHolidayTextPaint : mWeekDayTextPaint);
            startPixel += mHeaderColumnHeight;
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }

    private int mTouchSlop;
    private float offsetX;
    private float offsetY;
    private float startX;
    private float startY;
    private float lastX;
    private float lastY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (ev.getX() < mHeaderColumnWidth) {//按下的时候在ScrollView的左边
                    offsetX = mHeaderColumnWidth - ev.getX(); //>0
                }
                if (ev.getY() < mHeaderRowHeight) {//按下的时候在ScrollView的上边
                    offsetY = mHeaderRowHeight - ev.getY();//>0
                    startX = lastX = ev.getX();
                    startY = lastY = ev.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                lastX = ev.getX();
                lastY = ev.getY();
                break;
        }
        //左侧和上方也可以滑动
        boolean s;
        if (offsetX > 0) {
            ev.offsetLocation(-100000, -100000);
            s = grid.dispatchTouchEvent(ev);
        } else if (offsetY > 0) {
            ev.offsetLocation(-100000, -100000);
            s = myscrollView.dispatchTouchEvent(ev);
        } else {
            s = super.dispatchTouchEvent(ev);
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                if (Math.abs(lastX - startX) < mTouchSlop && Math.abs(lastY - startY) < mTouchSlop) {
                    if (startY > 0 && startY < mHeaderColumnHeight && startX > mHeaderColumnWidth) {
                        int position = (int) ((lastX - mCurrentOrigin.x) / mHeaderColumnWidth - 1);
                        mTittleClickListener.onTittleClick(position);
                    }
                    s = super.dispatchTouchEvent(ev);
                }
                offsetY = 0;
                offsetX = 0;
                startX = 0;
                startY = 0;
                break;
        }
        return s;
    }

    public interface TittleClickListener {
        void onTittleClick(int position);
    }

    public void setOnTittleClickListener(TittleClickListener listener) {
        this.mTittleClickListener = listener;
    }

    public interface DrawOverListener {
        void onDrawOver();
    }

    public void setDrawOverListener(DrawOverListener listener) {
        this.mDrawOverListener = listener;
    }

    public void setDays(List<Day> days) {
        this.days = days;
    }

    public void setTittles(List<String> tittles){
        this.tittles = tittles;
    }
}
