package com.jackandphantom.androidlikebutton;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class AndroidDotView extends View {

   /*DOTS_COUNT is used for number of dots will generate in circle so for that we choose 7 dots
   * In order to create 7 Dots we need make an angle for each dot so after calculation 51 is angle for each dot
   * */
   private static final int DOTS_COUNT = 7;
   private static final int OUTER_DOTS_POSITION_ANGLE = 51;

   /* These two colors which appears in dots */
   private   int COLOR_1 = 0xffffc107;
   private   int COLOR_2 = 0xffff9800;

   /* These two variable are for dot radius and dot size
   * */
   private float maxOuterDotsRadius;
   private float maxDotSize;

   /* All these variable is used as there name implies */
   private int centerX;
   private int centerY;
   private Paint[] circlePaint = new Paint[4];
   private float currentProgress = 0;
   private float currentRadius = 0;
   private float currentDotSize = 0;


   private int width , height;
   private boolean isActive = true;

   /* This boolean is used when we changed the dimension of view from another class @link AndroidLikeButton*/
   private boolean changeDimension = false;

   private ArgbEvaluator argbEvaluator = new ArgbEvaluator();


   public AndroidDotView(Context context) {
       super(context);
       init();
   }

   public AndroidDotView(Context context, @Nullable AttributeSet attrs) {
       super(context, attrs);
       init();
   }

   public AndroidDotView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
       super(context, attrs, defStyleAttr);
       init();
   }

   // Initiliaze the paint array;
   private void init() {
       for(int i = 0; i < circlePaint.length; i++) {
           circlePaint[i] = new Paint();
           circlePaint[i].setStyle(Paint.Style.FILL);
       }

   }

   /** This value is used when change dimension is not true */

   @Override
   protected void onSizeChanged(int w, int h, int oldw, int oldh) {
       super.onSizeChanged(w, h, oldw, oldh);

       if (!changeDimension) {
           this.centerX = width / 2;
           centerY = height / 2;
           maxOuterDotsRadius = width / 2;
           maxDotSize = getDotSize(centerX) + 5;
       }

   }

   /* This is math calculation used to find the dot size according for the width and height of the view*/

   private int getDotSize(int width) {
       float c = (float)width / 100;
       int value = (int)c;
       float result = (float)(c - value);

       if (result >= 0.5) {
           return value + 2;
       }
       else {
           return value;
       }
   }

   // I make a method used to disable the DotView so in order to do that i create a boolean variable isActive
   @Override
   protected void onDraw(Canvas canvas) {

       if (isActive)
       drawOuterDotsFrame(canvas);
       //drawInnerDotsFrame(canvas);
   }

   /* The dimension is set according to changeDimension boolean variable */

   @Override
   protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
       super.onMeasure(widthMeasureSpec, heightMeasureSpec);

       if (changeDimension) {
           setMeasuredDimension(width, height);
       } else {
           width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
           height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
           setMeasuredDimension(width, height);
       }
   }

   private void drawOuterDotsFrame(Canvas canvas) {
       for (int i = 0; i < DOTS_COUNT; i++) {
           int cX = (int) (centerX + currentRadius * Math.cos(i * OUTER_DOTS_POSITION_ANGLE * Math.PI / 180));
           int cY = (int) (centerY + currentRadius * Math.sin(i * OUTER_DOTS_POSITION_ANGLE * Math.PI / 180));
           canvas.drawCircle(cX, cY, currentDotSize, circlePaint[i % 4]);
       }
   }

/* This method is call when animation is executed then it's increase the radius of dot's **/

   public void setCurrentProgress(float value) {

       currentProgress = value;
       currentRadius = (float) (maxOuterDotsRadius * value);
       currentDotSize = (float) (maxDotSize*value);
       updateDotsPaints();
       updateDotsAlpha();
       invalidate();

   }

   /* Above two variable is decelare for color so when animation is started then it slightly change color */
   private void updateDotsPaints() {
       if (currentProgress < 0.5f) {
           float progress = currentProgress;
           //float progress = (float) Utils.mapValueFromRangeToRange(currentProgress, 0f, 0.5f, 0, 1f);
           circlePaint[0].setColor((Integer) argbEvaluator.evaluate(progress, COLOR_1, COLOR_2));
           circlePaint[1].setColor((Integer) argbEvaluator.evaluate(progress, COLOR_2, COLOR_1));
           circlePaint[2].setColor((Integer) argbEvaluator.evaluate(progress, COLOR_1, COLOR_2));
           circlePaint[3].setColor((Integer) argbEvaluator.evaluate(progress, COLOR_2, COLOR_1));
       } else {
           float progress = (float) Utils.mapValueFromRangeToRange(currentProgress, 0.5f, 1f, 0, 1f);
           circlePaint[0].setColor((Integer) argbEvaluator.evaluate(progress, COLOR_2, COLOR_1));
           circlePaint[1].setColor((Integer) argbEvaluator.evaluate(progress, COLOR_1, COLOR_2));
           circlePaint[2].setColor((Integer) argbEvaluator.evaluate(progress, COLOR_2, COLOR_1));
           circlePaint[3].setColor((Integer) argbEvaluator.evaluate(progress, COLOR_1, COLOR_2));
       }
   }

   /* There are two method for make fade effect in the end of animation */
   private void setViewAlpha() {
       float progress;
       int alpha;
       if (currentProgress < 0.8f) {
            progress = (float) Utils.clamp(currentProgress, 0.9, 1f);
            alpha = (int) (progress * 255);
       } else {
           alpha = (int) ((1 - currentProgress)*255);
       }

       for ( int i = 0; i < circlePaint.length; i++) {
           circlePaint[i].setAlpha(alpha);
       }
        //circlePaint[0].setAlpha((int) 0.4);
   }

/* This is second mehtod to fade out effect in the end of animation */

   private void updateDotsAlpha() {
       float progress = (float) Utils.clamp(currentProgress, 0.6f, 1f);
       int alpha = (int) Utils.mapValueFromRangeToRange(progress, 0.6f, 1f, 255, 0);
       circlePaint[0].setAlpha(alpha);
       circlePaint[1].setAlpha(alpha);
       circlePaint[2].setAlpha(alpha);
       circlePaint[3].setAlpha(alpha);
   }

/* This method is used in @link AndroidLikeButton for change the width and height of DotView*/
    void setWidthAndHeight(int width, int height) {
         if (width != 0 && height != 0) {
             this.centerX = width;
             this.width = width*2;
             this.height = height*2;
             centerY = height;
             maxOuterDotsRadius = width;
             maxDotSize = getDotSize(centerX)+5;
             changeDimension = true;
             requestLayout();
         }
   }



    void setDotColor1(int color) {
       if (color != 0)
       this.COLOR_1 = color;
   }
    void setDotColor2(int color) {
       if (color != 0)
       this.COLOR_2 = color;
   }

   //This method is used to make disable the AndroidDotView

   void setDotActive(boolean isActive) {
       this.isActive = isActive;
   }

    void setAllDotColor(int color1, int color2) {
       COLOR_1 = color1;
       COLOR_2 = color2;

   }
}
