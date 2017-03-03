package com.it.keithxiaoy.qqkeithxiaoy.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyphenate.util.DensityUtil;
import com.it.keithxiaoy.qqkeithxiaoy.R;

/**
 * Created by xiaoY on 2017/3/3.
 */

public class SlideBar extends View {

    private int mMeasuredWidth;
    private float mMeasuredHeight;
    private float mAvgHeight;
    private int mAvgWidth;
    private TextView mTvFloat;
    private static final String[] SECTIONS = {"搜", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private Paint mPaint;

    public SlideBar(Context context) {
        this(context,null);
    }

    public SlideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    public SlideBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //抗锯齿
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#000000"));
        mPaint.setTextAlign(Paint.Align.CENTER);
        //需要把10sp转换为px
        mPaint.setTextSize(DensityUtil.sp2px(context,10));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMeasuredWidth = getMeasuredWidth();
        mMeasuredHeight = getMeasuredHeight();
        mAvgHeight = (mMeasuredHeight+0f)/(SECTIONS.length+1);
        mAvgWidth = mMeasuredWidth/2;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //给自己设置灰色的背景
                setBackgroundResource(R.drawable.slide_bar_bk);
                //将点中的文字显示到TextView
                //如果RecyclerView中正好有以当前选中的文本开头的条目，定位条目
                setFloatTextAndScrollRecyclerView(event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                //将点中的文字显示到TextView
                //如果RecyclerView中正好有以当前选中的文本开头的条目，定位条目
                setFloatTextAndScrollRecyclerView(event.getY());
                break;
            case MotionEvent.ACTION_UP:
                //取消背景色
                setBackgroundColor(Color.TRANSPARENT);
                //隐藏TextView
                mTvFloat.setVisibility(GONE);
                break;
        }
        return true;
    }

    private void setFloatTextAndScrollRecyclerView(float y) {
        //将点中的文字显示到TextView
        int index = (int) ((y-mAvgHeight/2)/mAvgHeight);
        if (index<0){
            index = 0;
        }else if (index>SECTIONS.length-1){
            index = SECTIONS.length -1;
        }
        String section = SECTIONS[index];

        if (mTvFloat==null){
            //获取SlideBar的父控件
            ViewGroup parent = (ViewGroup) getParent();
            mTvFloat = (TextView) parent.findViewById(R.id.tv_float);
 //           mRecyclerView = (RecyclerView) parent.findViewById(R.id.recyclerView);
        }
        mTvFloat.setVisibility(VISIBLE);
        mTvFloat.setText(section);

        //获取RecyclerView中总共有多少个条目
//        SlideBarAdapter adapter = (SlideBarAdapter) mRecyclerView.getAdapter();
//        List<String> dataList = adapter.getData();
//        for (int i = 0; i < dataList.size(); i++) {
//            String contact = dataList.get(i);
//            if (StringUtils.getInitial(contact).equals(section)){
//                mRecyclerView.smoothScrollToPosition(i);
//                return;
//            }
//        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < SECTIONS.length; i++) {
            canvas.drawText(SECTIONS[i],mAvgWidth,mAvgHeight*(i+1f),mPaint);
        }
    }


}
