package utils;

import java.util.HashMap;
import java.util.Random;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RadioButton;

public class SoftRadioButton extends RadioButton {

    private static HashMap<String, SoftRadioGroup> GROUP_MAPPINGS = new HashMap<String, SoftRadioGroup>();
    private String mGroupName;

    public SoftRadioButton(Context context, String groupName) {
        super(context);
        addToGroup(groupName);
    }

    public SoftRadioGroup getRadioGroup() {
        return GROUP_MAPPINGS.get(mGroupName);
    }

    private void addToGroup(String groupName) {


                SoftRadioGroup group;
                if ((group = GROUP_MAPPINGS.get(groupName)) != null) {
                    // RadioGroup already exists
                    group.addView(this);
                    setOnClickListener(group);
                    mGroupName = groupName;

                } else {
                    // this is the first RadioButton in the RadioGroup
                    group = new SoftRadioGroup();
                    group.addView(this);
                    mGroupName = groupName;
                    setOnClickListener(group);

                    GROUP_MAPPINGS.put(groupName, group);
                }
                return;


    }

}