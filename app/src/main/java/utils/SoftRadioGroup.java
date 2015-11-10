package utils;

import java.util.ArrayList;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

public class SoftRadioGroup implements OnClickListener {

    private ArrayList<RadioButton> buttons = new ArrayList<RadioButton>();

    public void addView(RadioButton button) {
        buttons.add(button);
    }

    @Override
    public void onClick(View v) {
        for (RadioButton button : buttons) {
            button.setChecked(false);
        }
        RadioButton button = (RadioButton) v;
        button.setChecked(true);

    }


    public RadioButton getCheckedRadioButton() {
        for (RadioButton button : buttons) {
            if (button.isSelected())
                return button;
        }
        return null;
    }

    public int getChildCount() {
        return buttons.size();
    }

    public RadioButton getChildAt(int i) {
        return buttons.get(i);
    }

    public void check(SoftRadioButton button) {
        if (buttons.contains(button)) {
            for (RadioButton b : buttons) {
                b.setChecked(false);
            }
        }
    }

}