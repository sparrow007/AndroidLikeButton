package com.jackandphantom.androidlikebutton;




public class Utils {

    public static double mapValueFromRangeToRange(double value,double fromLow,  double fromHigh, double toLow, double toHigh) {
        double progress = ((value - fromLow) / (fromHigh - fromLow)*(toHigh - toLow));

        return toLow + progress;
    }

    public static double clamp(double value, double low, double high) {
        return  Math.min(Math.max(value , low), high);
    }

    public static int getDotSize (int maximumuValue) {
        int a = 100;
        return ((maximumuValue /a)+1)*5;
    }
}
