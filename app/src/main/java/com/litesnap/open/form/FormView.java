package com.litesnap.open.form;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ZTMIDGO on 2018/7/8.
 */

public class FormView extends View {
    public static final String TAG = "FromView3";
    private Row[] mRows;
    private float mTitleWidthGravity = 0.2f;
    private float mContentWidthGravity = 0.15f;
    private float mProgressWidthGravity = 1.0f - (mTitleWidthGravity + mContentWidthGravity);
    private float mTitleWidth;
    private float mContentWidth;
    private float mProgressWidth;
    private float mRowHeight;
    private float mProgressHeightGravity = 0.45f;
    private float mProgressHeight;
    private float mProgressRadius;
    private float mTitleTextSize;
    private float mContentTextSize;
    private float mTitleMarginRight;
    private float mContentMarginLeft;
    private float mTotal;
    private float mMaxWidth;
    private float mMaxHeight;

    private Paint mTitleTextPaint;
    private Paint mContentTextPaint;
    private Paint mProgressNormalPaint;
    private Paint mProgressPaint;

    public FormView(Context context) {
        this(context, null);
    }

    public FormView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mProgressRadius = AndroidSystem.dip2px(context, 100);
        mTitleMarginRight = AndroidSystem.dip2px(context, 10);
        mContentMarginLeft = AndroidSystem.dip2px(context, 10);
        mTitleTextSize = AndroidSystem.dip2px(context, 8);
        mContentTextSize = AndroidSystem.dip2px(context, 10);

        mTitleTextPaint = new TextPaint();
        mTitleTextPaint.setColor(Color.parseColor("#FF949494"));
        mTitleTextPaint.setTypeface(Typeface.SANS_SERIF);
        mTitleTextPaint.setAntiAlias(true);
        mTitleTextPaint.setTextSize(mTitleTextSize);
        mTitleTextPaint.setTextAlign(Paint.Align.RIGHT);

        mContentTextPaint = new TextPaint();
        mContentTextPaint.setColor(Color.BLACK);
        mContentTextPaint.setTypeface(Typeface.SANS_SERIF);
        mContentTextPaint.setAntiAlias(true);
        mContentTextPaint.setTextSize(mContentTextSize);

        mProgressNormalPaint = new Paint();
        mProgressNormalPaint.setAntiAlias(true);
        mProgressNormalPaint.setColor(Color.parseColor("#E8E8E7"));

        mProgressPaint = new Paint();
        mProgressPaint.setAntiAlias(true);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (mRows == null){
            return;
        }

        if (mRows.length == 0){
            return;
        }

        mTotal = 0;

        for (Row row : mRows){
            mTotal += row.data;
        }

        Rect rect = canvas.getClipBounds();
        calcuRow(rect);

        for (int i = 0; i < mRows.length; i++){
            drawRow(canvas, mRows[i], i);
        }
    }

    private void drawRow(Canvas canvas, Row row, int rowIndex){
        RectF rowRect = getNextRowRect(rowIndex);

        drawTitle(canvas, rowRect, row);
        RectF progressRect = drawProgress(canvas, rowRect, row);
        drawContent(canvas, rowRect, progressRect, row);
    }

    private RectF getNextRowRect(int rowIndex){
        RectF rectF = new RectF();
        rectF.left = 0;
        rectF.top = mRowHeight * (rowIndex * 1.0f);
        rectF.right = mMaxWidth;
        rectF.bottom = rectF.top + mRowHeight;
        return rectF;
    }

    private void drawTitle(Canvas canvas, RectF rowRect, Row row){
        String text = row.priText;

        float textWidth = mTitleTextPaint.measureText(text);
        float textTop = mTitleTextPaint.getFontMetrics().ascent;
        float textBottom = mTitleTextPaint.getFontMetrics().descent;

        RectF adornRect = new RectF();
        adornRect.left = rowRect.left;
        adornRect.top = rowRect.centerY() - Math.abs(textTop + textBottom) / 2.0f;
        adornRect.right = rowRect.left + mTitleWidth;
        adornRect.bottom = rowRect.centerY() + Math.abs(textTop + textBottom) / 2.0f;
        canvas.drawText(text, adornRect.right, adornRect.bottom, mTitleTextPaint);
    }

    private RectF drawProgress(Canvas canvas, RectF rowRect, Row row){
        mProgressPaint.setColor(row.color);

        float gravity = row.data == 0 ? 0 : (row.data * 1.0f) / mTotal;
        RectF progressNormalRect = new RectF();
        progressNormalRect.left = mTitleWidth + mTitleMarginRight;
        progressNormalRect.top = rowRect.centerY() - mProgressHeight / 2.0f;
        progressNormalRect.right = progressNormalRect.left + mProgressWidth;
        progressNormalRect.bottom = rowRect.centerY() + mProgressHeight / 2.0f;
        canvas.drawRoundRect(progressNormalRect, mProgressRadius, mProgressRadius, mProgressNormalPaint);

        RectF progressRect = new RectF();
        progressRect.left = progressNormalRect.left;
        progressRect.top = progressNormalRect.top;
        progressRect.right = progressRect.left + progressNormalRect.width() * gravity;
        progressRect.bottom = progressNormalRect.bottom;
        canvas.drawRoundRect(progressRect, mProgressRadius, mProgressRadius, mProgressPaint);
        return progressNormalRect;
    }

    private void drawContent(Canvas canvas, RectF rowRect, RectF progressRect, Row row){
        String text = (row.secText == null || row.secText.equals("0") ? "" : row.secText);

        float textWidth = mContentTextPaint.measureText(text);
        float textTop = mContentTextPaint.getFontMetrics().ascent;
        float textBottom = mContentTextPaint.getFontMetrics().descent;

        RectF adornRect = new RectF();
        adornRect.left = progressRect.right + mContentMarginLeft;
        adornRect.top = rowRect.centerY() - Math.abs(textTop + textBottom) / 2.0f;
        adornRect.right = adornRect.left + mContentWidth;
        adornRect.bottom = rowRect.centerY() + Math.abs(textTop + textBottom) / 2.0f;
        canvas.drawText(text, adornRect.left, adornRect.bottom, mContentTextPaint);
    }

    private void calcuRow(Rect rect){
        mMaxWidth = rect.width() * 1.0f;
        mMaxHeight = rect.height() * 1.0f;

        mTitleWidth = rect.width() * 1.0f * mTitleWidthGravity;
        mContentWidth = rect.width() * 1.0f * mContentWidthGravity;
        mProgressWidth = rect.width() * 1.0f - (mTitleWidth + mContentWidth + mTitleMarginRight + mContentMarginLeft);
        mRowHeight = (rect.height() * 1.0f) / (mRows.length * 1.0f);
        mProgressHeight = mRowHeight * mProgressHeightGravity;

    }

    public void setRows(Row[] rows){
        mRows = rows;
        invalidate();
    }

    public static class Row {
        private int data;
        private String priText;
        private String secText;
        private int color;

        public Row(int data, String priText, String secText, int color){
            this.data = data;
            this.priText = priText;
            this.secText = secText;
            this.color = color;
        }

        public int getData() {
            return data;
        }

        public String getPriText() {
            return priText;
        }

        public String getSecText() {
            return secText;
        }

        public int getColor() {
            return color;
        }
    }
}
