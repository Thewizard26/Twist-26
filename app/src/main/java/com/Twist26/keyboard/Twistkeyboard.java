
package com.twist26.keyboard;

import android.inputmethodservice.InputMethodService;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.LinearLayout;

public class TwistKeyboard extends InputMethodService {

    private boolean shifted = false;
    private LinearLayout keyboard;

    private final String[] rows = {
        "QYGWDCLUM",
        "OTAENIRSH",
        "KJXZ FPBV"
    };

    @Override
    public View onCreateInputView() {
        keyboard = new LinearLayout(this);
        keyboard.setOrientation(LinearLayout.VERTICAL);
        keyboard.setBackgroundColor(Color.rgb(215, 218, 225));
        keyboard.setPadding(3, 6, 3, 6);

        for (String row : rows) {
            LinearLayout line = new LinearLayout(this);
            line.setOrientation(LinearLayout.HORIZONTAL);

            for (char letter : row.toCharArray()) {
                if (letter == ' ') {
                    View blank = new View(this);
                    line.addView(blank,
                        new LinearLayout.LayoutParams(0, 56, 1));
                    continue;
                }

                Button key = makeKey(String.valueOf(letter));
                key.setOnClickListener(v -> {
                    InputConnection ic = getCurrentInputConnection();
                    if (ic != null) {
                        String text = ((Button)v).getText().toString();
                        ic.commitText(text, 1);
                    }
                });
                line.addView(key,
                    new LinearLayout.LayoutParams(0, 56, 1));
            }
            keyboard.addView(line);
        }

        LinearLayout controls = new LinearLayout(this);

        Button shift = makeKey("SHIFT");
        shift.setOnClickListener(v -> {
            shifted = !shifted;
            refreshKeys();
        });

        Button space = makeKey("SPACE");
        space.setOnClickListener(v -> sendText(" "));

        Button backspace = makeKey("⌫");
        backspace.setOnClickListener(v -> {
            InputConnection ic = getCurrentInputConnection();
            if (ic != null) {
                ic.deleteSurroundingText(1, 0);
            }
        });

        Button enter = makeKey("↵");
        enter.setOnClickListener(v -> {
            InputConnection ic = getCurrentInputConnection();
            if (ic != null) {
                ic.commitText("\n", 1);
            }
        });

        controls.addView(shift,
            new LinearLayout.LayoutParams(0, 56, 1));
        controls.addView(space,
            new LinearLayout.LayoutParams(0, 56, 3));
        controls.addView(backspace,
            new LinearLayout.LayoutParams(0, 56, 1));
        controls.addView(enter,
            new LinearLayout.LayoutParams(0, 56, 1));

        keyboard.addView(controls);
        return keyboard;
    }

    private Button makeKey(String label) {
        Button button = new Button(this);
        button.setText(label);
        button.setTextSize(14);
        button.setTextColor(Color.BLACK);
        button.setAllCaps(false);
        button.setGravity(Gravity.CENTER);
        button.setPadding(0, 0, 0, 0);
        return button;
    }

    private void sendText(String text) {
        InputConnection ic = getCurrentInputConnection();
        if (ic != null) {
            ic.commitText(text, 1);
        }
    }

    private void refreshKeys() {
        for (int i = 0; i < 3; i++) {
            LinearLayout line =
                (LinearLayout) keyboard.getChildAt(i);

            for (int j = 0; j < 9; j++) {
                View view = line.getChildAt(j);
                if (view instanceof Button) {
                    Button key = (Button) view;
                    String text = key.getText().toString();
                    key.setText(shifted
                        ? text.toUpperCase()
                        : text.toLowerCase());
                }
            }
        }
    }
}
