# AndroidLikeButton
![github gif](https://user-images.githubusercontent.com/22986571/34904388-dbbfc69c-f86a-11e7-8478-9cef559ca63b.gif)

[![Platform](https://img.shields.io/badge/platform-android-blue.svg)](http://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-14%2B-blue.svg?style=flat)](https://android-arsenal.com/api?level=14)

This library let you create a button with animation like twitter' heart , facebook like button in a simplest way

Gradle
------

#### Add the dependency

```
dependencies {
    ...
    compile 'com.jackandphantom.android:androidlikebutton:1.2.0'
}
```
# Basic Usage

#### AndroidLikeButton XML

In order to use AndroidLikeButton copy following xml code in your xml file.

```xml

 <com.jackandphantom.androidlikebutton.AndroidLikeButton
                android:layout_width="150dp"
                android:layout_height="150dp"
                android:layout_below="@+id/circular"
                android:layout_centerHorizontal="true"
                android:layout_marginTop="20dp"
                app:like_icon="@drawable/like_star"
                app:unlike_icon="@drawable/unlike_star"
                app:circle_startColor="#fcd229"
                app:circle_endColor="#f4ac1b"
                app:dot_color_1="#f5ce31"
                app:dot_color_2="#ed8128"
                >

```

# Attributes

There are several attribute to configure AndroidLikeButton view, they are following

#### Like and Unlike image

To set like button and unlike button in your likebutton use :-

```xml
 <com.jackandphantom.androidlikebutton.AndroidLikeButton
       app:like="@drawable/like_star"  
        app:unlike_icon="@drawable/unlike_star"                                                  
  />

```

In Java you have several way to do it
```java
   //Like Button
   androidLikeButton.setLikeIcon(Uri);
   androidLikeButton.setLikeIcon(Bitmap);
   androidLikeButton.setLikeIcon(ResId);
   
   //Unlike button
   
   androidLikeButton.setUnlikeIcon(Uri);
   androidLikeButton.setUnlikeIcon(Bitmap);
   androidLikeButton.setUnlikeIcon(ResId);

```
#### Circle 
you can see the circle in animation so for changing the color of circle add the below code and you can also decide that circle should
be appear or not in your animation.

Default of circle active is true.

```xml
 <com.jackandphantom.androidlikebutton.AndroidLikeButton
       app:circle_startColor="#fcd229"
       app:circle_endColor="#f4ac1b"
       app:circle_active="false"                                                  
  />

```
In Java 

```java
   
 androidLikeButton.setCircleColor(int color , int color);
 androidLikeButton.setCircleActive(boolean isActive);

```

#### Dot 
you can see the Dot in animation so for changing the color of Dot add the below code and you can also decide that Dot should
be appear or not in your animation.

Default of circle active is true.

```xml
 <com.jackandphantom.androidlikebutton.AndroidLikeButton
      app:dot_color_1="#f5ce31"
      app:dot_color_2="#ed8128"
      app:dot_active="false"                                                 
  />

```
In Java 

```java
   
 androidLikeButton.setDotColor(int color , int color);
 androidLikeButton.setDotActive(boolean isActive);

```

#### Like Button

In order to make AndroidLikeButton already liked because default implementation is make unlike image first then animate the view and convert it into like image so if you want to make already like in your implementation so just add below code.

Default value of liked = false.

```xml
 <com.jackandphantom.androidlikebutton.AndroidLikeButton
      app:liked="false"                                                 
  />

```
In Java 

```java
androidLikeButton.setCurrentlyLiked(boolean liked);

```

#### Like and Unlike Bitmap 

If you need bitmap of like and unlike image then you can retrieve it by calling the below code java.

In Java 

```java
Bitmap likeBitmap = androidLikeButton.getLikeBitmap();
Bitmap unlikeBitmap = androidLikeButton.getUnlikeBitmap();

```

 LICENCE
-----

 Copyright 2017 Ankit kumar

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 Special Thanks to frogermcs


