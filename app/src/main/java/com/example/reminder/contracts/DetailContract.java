package com.example.reminder.contracts;

import android.content.Context;
import android.content.Intent;
import androidx.activity.result.contract.ActivityResultContract;
import com.example.reminder.NewMemoActivity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.example.reminder.DetailActivity.EDIT_UPDATE;

public class DetailContract extends ActivityResultContract<String[], String[]> {
    @NotNull
    @Override
    public Intent createIntent(@NotNull Context context, String[] strings) {

        Intent toNewMemo = new Intent(context, NewMemoActivity.class);
        toNewMemo.putExtra(EDIT_UPDATE, strings);

        return toNewMemo;
    }

    @Override
    public String[] parseResult(int i, @Nullable Intent intent) {

        if(i == RESULT_CANCELED)
            return new String[]{""};

        if(i != RESULT_OK || intent == null)
            return null;

        return intent.getStringArrayExtra(NewMemoActivity.EDIT_REPLY);
    }
}
