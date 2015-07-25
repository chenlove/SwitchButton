# SwitchButton
一个超级简单的可滑动的Switch ，直接继承自View。无需图片也是可以做出漂亮的按钮。

#使用
1、直接写入XML布局文件.

2、记住四个属性：oncolor(开关打开时背景颜色)、offcolor(开关关闭时背景颜色)、
btncolor(按钮的颜色)、is_on(boolean值，默认是否打开).

3、在activity 实现OnCheckedChangeListener用于监听按钮的状态改变.

3、在actitity 可使用setIsOn初始化按钮的状态.



使用自定义属性先定义命名空间

AndroidStudio:

```xml
xmlns:switch = "http://schemas.android.com/apk/res-auto"
```
```xml
<com.shicimingju.switchbutton.SwitchButton
            android:layout_width="60dp"
            android:layout_height="30dp" 
            switch:oncolor="#3344ff"
            switch:offcolor="#eeeeee"
            switch:btncolor="#ffffff"
            />
```


![image](https://github.com/chenlove/SwitchButton/blob/master/demo.png)
