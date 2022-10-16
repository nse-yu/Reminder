package com.example.reminder.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.example.reminder.R;

public class LocationContextButton extends LinearLayout {

    protected final TextView label;
    protected final TextView location;

    public String getLocation() {
        return location.getText().toString();
    }

    public void setLocation(String loc){
        this.location.setText(loc);
    }


    public LocationContextButton(Context context) {
        this(context, null);
    }

    public LocationContextButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LocationContextButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View.inflate(getContext(), R.layout.row2, this);

        label       = findViewById(R.id.label);
        location    = findViewById(R.id.location);
    }
}
