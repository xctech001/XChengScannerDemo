package com.xcheng.scanner.demo.widgets;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

public class PreviewInterfaceLine extends View {
    private float mDensity;
    private float mWidth;
    private float mHeight;

    private final Paint mPaint = new Paint();

    public PreviewInterfaceLine(Context context) {
        super(context);
        this.init();
    }

    public PreviewInterfaceLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public PreviewInterfaceLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    private void init() {
        this.mDensity = this.getResources().getDisplayMetrics().density;
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(Color.parseColor("#53D555"));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY)
            this.mWidth = widthSize;
        else
            this.mWidth = 250 * this.mDensity;

        if (heightMode == MeasureSpec.EXACTLY)
            this.mHeight = heightSize;
        else
            this.mHeight = 250 * this.mDensity;

        this.setMeasuredDimension((int) this.mWidth, (int) this.mHeight);
        Shader shader = new LinearGradient(
                this.mWidth / 2.0f,
                this.mHeight,
                this.mWidth / 2.0f,
                0,
                new int[]{Color.GREEN, Color.TRANSPARENT},
                null,
                Shader.TileMode.CLAMP
        );

        this.mPaint.setShader(shader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0, 0, this.mWidth, this.mHeight, this.mPaint);
    }
}
