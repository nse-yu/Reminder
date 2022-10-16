package com.example.reminder.database.room;

import android.content.Context;
import com.example.reminder.R;

public enum LocationCodes {

    All(0),
    REMINDER(1),
    TODO(2);

    public final int code;

    LocationCodes(int code){ this.code = code; }

    private static boolean validate(final int code){
        return code == 0 || code == 1 || code == 2;
    }

    public static LocationCodes toLocationCodes(final int code){

        if(!validate(code))
            return null;

        switch(code){
            case 0:
                return All;
            case 1:
                return REMINDER;
            case 2:
                return TODO;
            default:
                return null;
        }
    }

    public static String toString(Context context , final int code){

        if(!validate(code))
            return "";

        return new String[]{
                context.getString(R.string.tab_all), context.getString(R.string.tab_reminder), context.getString(R.string.tab_todo)
        }[code];
    }
}
