package com.shicimingju.switchbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by chenguoming on 15/7/24.
 */
public class SwitchButton extends View {
    public final static String TAG = "MySwitchView";
    private Paint paint;
    private float raduis,corner,offX,onX,CurX,Y;
    private float lastX,lastY,downX;
    private boolean isOn = false;
    private int onColor,offColor,btnColor;
    private OnCheckedChangeListener mOnCheckedChangeListener;
    public SwitchButton(Context context) {
        super(context);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.SwitchButton);
        onColor = a.getColor(R.styleable.SwitchButton_oncolor, Color.GREEN);
        offColor = a.getColor(R.styleable.SwitchButton_offcolor,Color.parseColor("#eeeeee"));
        btnColor = a.getColor(R.styleable.SwitchButton_btncolor,Color.WHITE);
        isOn = a.getBoolean(R.styleable.SwitchButton_is_on,true);
    }
    public void setOnCheckedChangeListener(OnCheckedChangeListener mOnCheckedChangeListener) {
        this.mOnCheckedChangeListener = mOnCheckedChangeListener;
    }

    /**
     * get switch status
     * @return
     */
    public boolean isOn() {
        return isOn;
    }

    /**
     * set switch status
     * @param isOn
     */
    public void setIsOn(boolean isOn) {
        this.isOn = isOn;
    }

    /**
     * init
     */
    private void init(){
        corner = raduis = getHeight()/2;
        offX = raduis;
        onX = getWidth() - raduis;
        Y = raduis;
        if(CurX == 0){//第一次加载..
            if(isOn){
                CurX = onX;
            }else{
                CurX = offX;
            }
        }
        int b = ((int)((CurX-raduis)/(getWidth()-2*raduis) * 0xf));
        Log.d(TAG, "b:" + b + "--->CurX:" + CurX);
        int alpha = ((b|(b<<4)))<<24;
        if(CurX == offX)
            paint.setColor(Color.parseColor("#eeeeee"));
        else
            paint.setColor(getSwitchAlphaColor());
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
    }

    /**
     * get a alpha color by switch position
     *
     * @return
     */

    public int getSwitchAlphaColor(){
        float cx = CurX - raduis;
        float len = getWidth()-2*raduis;
        int b ;
        boolean on = false;
        int c = onColor;
        if(cx>len/2){
            b = (int)((cx - len/2)/(len/2)*0xf);
            on = true;
        }else{
            b = 0xf - (int)(cx/(len/2)*0xf);
        }
        if(!on){
            c = Color.parseColor("#eeeeee");
        }
        b = b<4?4:b;
        b =  ((b|(b<<4)))<<24;
        return getAlphaColor(c,b);
    }

    /**
     * get a color by params
     * @param color base color
     * @param alpha
     * @return
     */
    private int getAlphaColor(int color,int alpha){
        Log.d(TAG, "color:" + color);
        Log.d(TAG, "alpha:" + alpha);
        color = color & 0x00ffffff;
        Log.d(TAG,"alpha:"+(alpha|color));
        return  color | alpha;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        init();
        canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), corner, corner, paint);
        canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), corner, corner, paint);
        paint.setColor(btnColor);
        canvas.drawCircle(CurX == 0 ? offX : CurX, Y, corner - 6, paint);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = lastX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if(event.getX()>onX){
                    if(CurX != onX){
                        CurX = onX;
                        invalidate();
                    }
                }else if(event.getX()<offX){
                    if(CurX != offX){
                        CurX = offX;
                        invalidate();
                    }
                }else{
                    CurX = event.getX();
                    invalidate();
                }
                lastX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                if(CurX<=getWidth()/2){
                    CurX = offX;
                }else{
                    CurX = onX;
                }
                boolean bf = isOn;
                if(CurX == offX)
                    isOn = false;
                else
                    isOn = true;
                if(bf!=isOn){
                    if(mOnCheckedChangeListener!=null){
                        mOnCheckedChangeListener.onCheckedChanged(this,isOn);
                    }
                }
                invalidate();
                break;
        }
        return true;
    }
    public  interface OnCheckedChangeListener {
        void onCheckedChanged(SwitchButton v, boolean isOn);
    }
}
