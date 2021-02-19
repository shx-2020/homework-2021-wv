package xyz.imaginarycrisis.wanandroidapp;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AlwaysMarqueeTextView extends androidx.appcompat.widget.AppCompatTextView{
    public AlwaysMarqueeTextView(@NonNull Context context) {
        super(context);
    }

    public AlwaysMarqueeTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AlwaysMarqueeTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused(){
        return true;
    }
}