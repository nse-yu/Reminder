package com.example.reminder.contracts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.activity.result.contract.ActivityResultContract;
import com.example.reminder.NewMemoActivity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static android.app.Activity.RESULT_CANCELED;

public class NewMemoContract extends ActivityResultContract<Integer, String[]> {
    @NotNull
    @Override
    public Intent createIntent(@NotNull Context context, Integer integer) {

        Intent append_intent = new Intent(context, NewMemoActivity.class);
        append_intent.putExtra("REQUEST",integer);

        return append_intent;
    }

    @Override
    public String[] parseResult(int i, @Nullable Intent intent) {

        if(i == RESULT_CANCELED)
            return new String[]{""};

        if(i != Activity.RESULT_OK || intent == null)
            return null;

        return intent.getStringArrayExtra(NewMemoActivity.EDIT_REPLY);
    }
}
