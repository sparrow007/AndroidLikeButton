package com.jackandphantom.androidlikebutton;


import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


public class AndroidCircleView extends View {

    /* These are color used to make circle  */
   private int StartColor = 0xFFFF5722;
   private int END_COLOR = 0xFFFFC107;

   /*These variables are used as name implies */
   private ArgbEvaluator argbEvaluator = new ArgbEvaluator();
   private Paint circlePaint = new Paint();
   private Paint maskPaint = new Paint();

   /* These are temp bitmap and canvas for making circleView*/
   private Bitmap tempBitmap;
   private Canvas tempCanvas;
   private float outerCircleRadiusProgress = 0f;

   private float innerCircleRadiusProgress=0f;
   private int maxCircleSize;
   private int width ,height;

   /* IsActive is used to make sure that circleview should appear in animation and changeDimension for used
    * When there is changing in dimension which is done by @link AndroidLikeButton
     * */
   private boolean isActive = true;
   private boolean changeDimensiion = false;



   public AndroidCircleView(Context context) {
       super(context);
       init();
   }

   public AndroidCircleView(Context context, @Nullable AttributeSet attrs) {
       super(context, attrs);
       init();
   }

   public AndroidCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
       super(context, attrs, defStyleAttr);
       init();
   }

   /*
   *  Circle is fill and maskPaint using PorterDuff Mode CLEAR which make empty
   *   */
   private void init() {
      circlePaint.setStyle(Paint.Style.FILL);
      maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
   }

   // when this callback method call by android system the it initialize some values which is using in order the draw view
   @Override
   protected void onSizeChanged(int w, int h, int oldw, int oldh) {
       super.onSizeChanged(w, h, oldw, oldh);
       valueInit(w);
   }

   /* This is used fo initialize the value for drawing the componenet on surface */

   private void valueInit(int width) {
       maxCircleSize = width /2;
       tempBitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
       tempCanvas = new Canvas(tempBitmap);

   }

   @Override
   protected void onDraw(Canvas canvas) {
       super.onDraw(canvas);

       tempCanvas.drawCircle(getWidth() / 2, getHeight() / 2, outerCircleRadiusProgress * maxCircleSize, circlePaint);
       tempCanvas.drawCircle(getWidth() / 2, getHeight() / 2, innerCircleRadiusProgress * maxCircleSize, maskPaint);
       if (isActive)
       canvas.drawBitmap(tempBitmap, 0, 0, null);
   }

   /* Here we are using changedimension for taking decision that until @link AndroidLikeButton is not change the
    * dimension of view then if make it's default value and if AndroidLikeButton change the dimension then by using
    * requestLayout we again call onMeasure for redesign the size of the view
     * */

   @Override
   protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (changeDimensiion) {
            setMeasuredDimension(width, height);
        } else {
            width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
            height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
            setMeasuredDimension(width, height);
        }

   }


   /* This methdo is call when animation is executed and update some value of circle */

    public void setProgress(float value) {

       this.outerCircleRadiusProgress = value;

       circlePaint.setColor((Integer) argbEvaluator.evaluate(value, StartColor, END_COLOR));
       invalidate();
   }

   public float getProgress() {
       return this.outerCircleRadiusProgress;
   }

   /* There are two circle if you see in the animation so we are performing two animation this second circle animation method */

   public void setInnerCircleRadiusProgress(float value) {
       this.innerCircleRadiusProgress = value;
       invalidate();
   }

    float getInnerCircleRadiusProgress() {
       return this.innerCircleRadiusProgress;
   }

   /* These two method for make color in circle */

    void setStartColor(int color) {
       if (color != 0)
       this.StartColor = color;
   }

    void setEND_COLOR(int color) {
       if (color != 0)
       this.END_COLOR = color;
   }

   /* This method is call by the @link AndroidLikeButton */
    void setWidthAndHeight(int width, int height) {
           this.width = width*2;
           this.height = height*2;
           valueInit(width*2);
           changeDimensiion = true;
           requestLayout();
   }

   /* This method is used to enable and disable the circle  */

   void setIsActive(boolean isActive) {
       this.isActive = isActive;
   }

   void setAllColor(int startColor , int endColor) {
       this.StartColor = startColor;
       this.END_COLOR = endColor;
   }

}


