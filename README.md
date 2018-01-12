## RangeTimeSeekBar
github:https://github.com/lvzhihao100/RangeTimeSeekBar  
简书:https://www.jianshu.com/p/4971610e5ba6 

##### 效果图
![rangetime.gif](http://upload-images.jianshu.io/upload_images/4179767-21e63d5ba9826c63.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/512)
#### 使用说明
+ 布局中使用
```java
 <com.eqdd.common.widget.RangeTimeSeekBar
            android:layout_width="match_parent"
            android:layout_height="300pt"
            android:padding="30pt" />
```
+ 设置最小时间段
```java
    /**
     * 设置时间段最短间隔,
     * @param minute 例如设置1小时的间隔,minute设置6,
     * @return
     */
    public RangeTimeSeekBar setMinSpace(int minute){
        this.minSpace=minute;
        invalidate();
        return this;
    }
```
+ 设置末端时间点
```java
    /**
     * 设置末端时间点
     * @param endHour 小时数
     * @param endMin 分钟数 例如30分钟 endMin设置为3
     * @return
     */
    public RangeTimeSeekBar setEndTime(int endHour, int endMin) {
              ......
    }
```
+ 设置起点时间点
```java
    /**设置起点时间点
     * @param startHour 0~24
     * @param startMin  0~6
     */
    public RangeTimeSeekBar setStartTime(int startHour, int startMin) {
        ......
    }
```
+ 获取当前起点时间与尾点时间
```java
    public String getStartTime() {
        return this.startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }
```
源代码:  
https://github.com/lvzhihao100/RangeTimeSeekBar/blob/master/RangeTimeSeekBar.java
