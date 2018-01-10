package com.jackandphantom.androidlikebutton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.IOException;


public class AndroidLikeButton extends FrameLayout implements View.OnClickListener {

   /* These three instance variable from @like_layout  */
   private ImageView star;
   private AndroidCircleView androidCircleView;
   private AndroidDotView androidDotView;

   /* isChecked is used for making decision that which imageview is set on imageview
    * animator set is used to run all animation as a set
     * */
   private boolean isChecked;
   private AnimatorSet animatorSet;
   private int dotColor1, dotColor2;
   private int circleStartColor, circleEndColor;

   /* starIcon and endIcon is reflection of like and unlike icon  */

   private int startIcon, endIcon;
   private boolean isLiked;
   private Context context;

   /* These variable is used as thre name suggest  */

   private Bitmap likeBitmap, unlikeBitmap;
   private boolean dotActive, circleActive;

   /* This is instance of  interface for like  or unlike icon */
   private OnLikeEventListener onLikeEventListener;

    public AndroidLikeButton(@NonNull Context context) {
        super(context);
        this.context = context;

    }

    public AndroidLikeButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;

    }

    /* In this constructor i am getting all values from custom xml attributes */

    public AndroidLikeButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AndroidLikeButton);
        dotColor1 = array.getColor(R.styleable.AndroidLikeButton_dot_color_1, 0);
        dotColor2 = array.getColor(R.styleable.AndroidLikeButton_dot_color_2, 0);
        circleEndColor = array.getColor(R.styleable.AndroidLikeButton_circle_endColor, 0);
        circleStartColor = array.getColor(R.styleable.AndroidLikeButton_circle_startColor, 0);
        startIcon = array.getResourceId(R.styleable.AndroidLikeButton_like_icon, 0);
        endIcon = array.getResourceId(R.styleable.AndroidLikeButton_unlike_icon, 0);
        isLiked = array.getBoolean(R.styleable.AndroidLikeButton_liked, false);
        dotActive = array.getBoolean(R.styleable.AndroidLikeButton_dot_active, true);
        circleActive = array.getBoolean(R.styleable.AndroidLikeButton_circle_active, true);

        array.recycle();
        init();
    }

    /* This medhod used to make dimesion and select whether the circel and dot view should be active or not
     * and also set there color and set image and gettng there bitmap for further use.
      * */
    private void init() {
      View view =   LayoutInflater.from(getContext()).inflate(R.layout.like_layout, this, true);
      androidCircleView = (AndroidCircleView) view.findViewById(R.id.circleView);
      star = view.findViewById(R.id.starImg);
      androidDotView = view.findViewById(R.id.dotView);

      androidCircleView.setEND_COLOR(circleEndColor);
      androidCircleView.setStartColor(circleStartColor);
      androidDotView.setDotColor1(dotColor1);
      androidDotView.setDotColor2(dotColor2);
      androidCircleView.setIsActive(circleActive);
      androidDotView.setDotActive(dotActive);

      if (startIcon != 0 && endIcon != 0) {
          likeBitmap = getBitmapFromResId(startIcon);
          unlikeBitmap = getBitmapFromResId(endIcon);
          setLikeButtonImage();
      }


      setOnClickListener(this);
    }

    /* in onSizeChanged we set all view dimension dimension  */

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int actualWidth = w/2;
        int actualHeight = h/2;
        setImageView(actualWidth, actualHeight);

    }



    public interface OnLikeEventListener{
        void onLikeClicked(AndroidLikeButton androidLikeButton);
        void onUnlikeClicked(AndroidLikeButton androidLikeButton);
    }

    public void setOnLikeEventListener(OnLikeEventListener onLikeEventListener) {
        this.onLikeEventListener = onLikeEventListener;
    }

    /* This view extends the framelayout and i set click listener in the init() method so when some one click on the
     * view then using this method we perform the animation in appropiate manner
      * */

    @Override
    public void onClick(View view) {
        isChecked = !isChecked;

        star.setImageBitmap(isChecked ? likeBitmap : unlikeBitmap);

        if (animatorSet != null) {
            animatorSet.cancel();
        }

        if (isChecked) {
            star.animate().cancel();
            star.setScaleX(0);
            star.setScaleY(0);
            androidCircleView.setInnerCircleRadiusProgress(0);
            androidCircleView.setProgress(0);
            androidDotView.setCurrentProgress(0);

            animatorSet = new AnimatorSet();

            ObjectAnimator outerCircleAnimator = ObjectAnimator.ofFloat(androidCircleView, "Progress", 0.1f, 1f);
            outerCircleAnimator.setDuration(250);
            outerCircleAnimator.setInterpolator(new DecelerateInterpolator());


            ObjectAnimator innerCircleAnimator = ObjectAnimator.ofFloat(androidCircleView, "InnerCircleRadiusProgress", 0.1f, 1f);
            innerCircleAnimator.setDuration(200);
            innerCircleAnimator.setStartDelay(200);
            innerCircleAnimator.setInterpolator(new DecelerateInterpolator());

            ObjectAnimator starScaleYAnimator = ObjectAnimator.ofFloat(star, ImageView.SCALE_Y, 0.2f, 1f);
            starScaleYAnimator.setDuration(350);
            starScaleYAnimator.setStartDelay(250);
            starScaleYAnimator.setInterpolator(new OvershootInterpolator());

            ObjectAnimator starScaleXAnimator = ObjectAnimator.ofFloat(star, ImageView.SCALE_X, 0.2f, 1f);
            starScaleXAnimator.setDuration(350);
            starScaleXAnimator.setStartDelay(250);
            starScaleXAnimator.setInterpolator(new OvershootInterpolator());


            ObjectAnimator dotsAnimator = ObjectAnimator.ofFloat(androidDotView, "CurrentProgress" , 0.1f, 1f);

            dotsAnimator.setDuration(900);
            dotsAnimator.setStartDelay(50);
            dotsAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

            animatorSet.playTogether(
                    outerCircleAnimator,
                    innerCircleAnimator,
                    starScaleYAnimator,
                    starScaleXAnimator,
                    dotsAnimator
            );


            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationCancel(Animator animation) {
                    androidCircleView.setInnerCircleRadiusProgress(0);
                    androidCircleView.setProgress(0);
                    androidDotView.setCurrentProgress(0);
                    star.setScaleX(1);
                    star.setScaleY(1);
                }
            });

            animatorSet.start();

            if (onLikeEventListener != null) {
                onLikeEventListener.onLikeClicked(AndroidLikeButton.this);
            }
        } else {
            if (onLikeEventListener != null) {
                onLikeEventListener.onUnlikeClicked(AndroidLikeButton.this);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                star.animate().scaleX(0.7f).scaleY(0.7f).setDuration(150).setInterpolator(new DecelerateInterpolator());
                setPressed(true);
                break;

            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                boolean isInside = (x > 0 && x < getWidth() && y > 0 && y < getHeight());
                if (isPressed() != isInside) {
                    setPressed(isInside);
                }
                break;

            case MotionEvent.ACTION_UP:
                star.animate().scaleX(1).scaleY(1).setInterpolator(new DecelerateInterpolator());
                if (isPressed()) {
                    performClick();
                    setPressed(false);
                }
                break;
        }
        return true;
    }

    /* These are some setter method  */

    public void setDotColor(int startColor, int endColor) {
        this.dotColor1 = startColor;
        this.dotColor2 = endColor;
        androidDotView.setAllDotColor(startColor, endColor);
    }

    public void setCircleColor(int startColor, int endColor) {
        this.circleStartColor = startColor;
        this.circleEndColor = endColor;
        androidCircleView.setAllColor(startColor, endColor);
    }

    public void setDotActive(boolean isActive) {
        this.dotActive = isActive;
        androidDotView.setDotActive(dotActive);
    }

    public void setCircleActive(boolean isActive) {
        this.circleActive = isActive;
        androidCircleView.setIsActive(circleActive);
    }

    private void setImageView(int width, int height) {
        star.getLayoutParams().width = width;
        star.getLayoutParams().height = height;
        androidCircleView.setWidthAndHeight(width, height);
        androidDotView.setWidthAndHeight(width, height);

    }

    public void setLikeIcon(int resId) {
        likeBitmap = getBitmapFromResId(resId);
        setLikeButtonImage();
    }

    public void setUnlikeIcon(int resId) {

        unlikeBitmap = getBitmapFromResId(resId);
        setLikeButtonImage();
    }

    public void setLikeIcon(Bitmap bitmap) {

        likeBitmap = bitmap;
        setLikeButtonImage();
    }

    public void setUnlikeIcon(Bitmap bitmap) {

        unlikeBitmap = bitmap;
        setLikeButtonImage();
    }

    public void setLikeIcon(Uri uri) {
        likeBitmap = getImageFromUri(uri);
        setLikeButtonImage();
    }

    public void setUnlikeIcon(Uri uri) {
        unlikeBitmap = getImageFromUri(uri);
        setLikeButtonImage();
    }

    private Bitmap getBitmapFromResId(int resId) {
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }

    private void setLikeButtonImage() {
        isChecked = isLiked;
        if (isLiked) {
            star.setImageBitmap(likeBitmap);
        }else {
            star.setImageBitmap(unlikeBitmap);
        }
    }

    public void setCurrentlyLiked(boolean isLiked) {
        this.isLiked = isLiked;
        this.isChecked = isLiked;
        setLikeButtonImage();
    }



    private Bitmap getDrawableToBitmap(Drawable drawable) {

        //return if drawable is null that means it doen't have a bitmap
        if (drawable == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private Bitmap getImageFromUri(Uri uri) {
        Bitmap temp = null;
        try {
            temp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (IOException exception){
            exception.printStackTrace();
        }

        return temp;

    }

    public Bitmap getLikeBitmap() {
        return this.likeBitmap;
    }

    public Bitmap getUnlikeBitmap() {
        return this.unlikeBitmap;
    }

}
