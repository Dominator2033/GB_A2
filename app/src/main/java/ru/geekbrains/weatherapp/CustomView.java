package ru.geekbrains.weatherapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CustomView extends View {

    RectF rectf = new RectF(getLeft(), getTop(), getRight(), getBottom());
    private Paint paint;
    private int radius;
    private int color;
    private boolean pressed = false;    // Признак нажатия
    View.OnClickListener listener;      // Слушатель

    public CustomView(Context context) {
        super(context);
        init();
    }

    // Вызывается при вставке элемента в макет
    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        init();
    }

    // Вызывается при вставке элемента в макет, если был добавлен стиль
    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        init();
    }

    // Обработка параметров в xml
    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomView, 0, 0);
        setRadius(typedArray.getResourceId(R.styleable.CustomView_cv_Radius, 100));
        setColor(typedArray.getResourceId(R.styleable.CustomView_cv_Color, Color.TRANSPARENT));
        typedArray.recycle();
    }

    // Подготовка элемента
    private void init() {
        paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rectf.set(0, getHeight() / 5, getWidth(), 0);
        canvas.drawCircle(radius, radius, radius, paint);
        canvas.drawRoundRect(rectf, 40, 40, paint);
        if (pressed) {
            canvas.drawCircle(radius, radius, radius / 10, paint);
        } else {
            canvas.drawCircle(radius, radius, radius, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int Action = event.getAction();
        if (Action == MotionEvent.ACTION_DOWN) { // Нажали
            pressed = true;
            invalidate();           // Перерисовка элемента
            if (listener != null) listener.onClick(this);
        } else if (Action == MotionEvent.ACTION_UP) { // Отпустили
            pressed = false;
            invalidate();           // Перерисовка элемента
        }
        return true;
    }
}
