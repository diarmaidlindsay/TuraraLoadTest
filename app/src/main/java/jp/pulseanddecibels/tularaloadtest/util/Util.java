package jp.pulseanddecibels.tularaloadtest.util;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import jp.pulseanddecibels.tularaloadtest.R;

/**
 * Created by Diarmaid Lindsay on 2016/05/20.
 * Copyright Pulse and Decibels 2016
 */
public class Util {
    public static final String STRING_EMPTY = "";
    public static final int INT_EMPTY = -9999;

    public static String getText(EditText editText) {
        try {
            return editText.getText().toString().trim();
        } catch (Exception ex) {
            return STRING_EMPTY;
        }
    }

    public static void initDialpad(View dialpadLayout) {
        final TextView numberField = (TextView) dialpadLayout.findViewById(R.id.field_number_entry);
        final Button deleteButton = (Button) dialpadLayout.findViewById(R.id.button_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = numberField.getText().toString();
                if(text.length() > 0) {
                    numberField.setText(text.substring(0, text.length()-1));
                }
            }
        });

        final int[] dialpadButtons = new int[] {
                R.id.button_0, R.id.button_1, R.id.button_2, R.id.button_3, R.id.button_4,
                R.id.button_5, R.id.button_6, R.id.button_7, R.id.button_8, R.id.button_9,
                R.id.button_hash, R.id.button_star
        };

        for(int buttonId : dialpadButtons) {
            final Button button = (Button) dialpadLayout.findViewById(buttonId);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numberField.setText(String.format("%s%s", numberField.getText().toString(), button.getText().toString()));
                }
            });
        }
    }
}
