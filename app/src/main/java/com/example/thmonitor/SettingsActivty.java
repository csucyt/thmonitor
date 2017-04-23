package com.example.thmonitor;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivty extends AppCompatActivity {

    private CheckBox dialogCheckBox;
    private EditText minEidtText;
    private EditText normEditText;
    private Button tempButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_activty);

        final SharedPreferences.Editor editor = getSharedPreferences("Suggestion", Context.MODE_PRIVATE)
                .edit();
        SharedPreferences pref = getSharedPreferences("Suggestion", Context.MODE_PRIVATE);
        final boolean mFlag = pref.getBoolean("flag", false);

        dialogCheckBox = (CheckBox)findViewById(R.id.dialog_check_box);
        minEidtText = (EditText)findViewById(R.id.min_edit_text);
        normEditText = (EditText)findViewById(R.id.norm_edit_text);
        tempButton = (Button)findViewById(R.id.temp_button);

        dialogCheckBox.setChecked(mFlag);
        dialogCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                editor.putBoolean("flag", !mFlag);
                editor.apply();
            }
        });

        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float minRange = Float.parseFloat(minEidtText.getText().toString());
                float normRange = Float.parseFloat(normEditText.getText().toString());
                if(minRange <= 0 | normRange <= 0 | minRange >= 79 | normRange >= 79 | minRange + normRange >= 80) {
                    Toast.makeText(SettingsActivty.this, "输入不合法", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences.Editor editor1 = getSharedPreferences("Temp", MODE_PRIVATE).edit();
                    editor1.putFloat("minRange", minRange);
                    editor1.putFloat("normRange", normRange);
                    editor1.apply();
                    Toast.makeText(SettingsActivty.this, "修改成功", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
