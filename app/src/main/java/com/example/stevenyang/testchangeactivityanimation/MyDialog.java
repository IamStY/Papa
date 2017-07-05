package com.example.stevenyang.testchangeactivityanimation;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by User on 2017/4/27.
 */

public class MyDialog extends Dialog {
    Button submit;
    EditText editText;
    Switch switch1;
    PapaInterface papaInterface;
    public MyDialog(Context context, final CallBackFromDialog callBackFromDialog , final PapaInterface papaInterface) {
        super(context, android.R.style.Theme_DeviceDefault_Dialog);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_layout);
        this.papaInterface = papaInterface;
        submit = (Button)this.findViewById(R.id.button);
        switch1 = (Switch)this.findViewById(R.id.switch1);
        if ((boolean) SharedPreferencesUtils.getParam("myPapa", false))
            switch1.setChecked(true);
        else
            switch1.setChecked(false);

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferencesUtils.setParam("myPapa", b);
                papaInterface.papaCallBack(b);
            }
        });
        editText = (EditText)this.findViewById(R.id.editText);
        editText.setText("https://www.youtube.com/watch?v=FG9M0aEpJGE&list=PLm_3vnTS-pvl0nwpX1TiwlogaV_OlBkoJ&index=1");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = (String)SharedPreferencesUtils.getParam("myVideoStrings","");
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<String>>() {}.getType();
                ArrayList<String> arrayList = gson.fromJson(str, type);
                if(arrayList==null)
                    arrayList = new ArrayList<String>();
                arrayList.add(editText.getText().toString());
                String json = gson.toJson(arrayList);
                SharedPreferencesUtils.setParam("myVideoStrings",json);
                callBackFromDialog.dialogCallBack();
                dismiss();
            }
        });
    }

}