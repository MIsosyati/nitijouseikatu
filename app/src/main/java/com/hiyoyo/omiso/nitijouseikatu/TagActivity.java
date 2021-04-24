package com.hiyoyo.omiso.nitijouseikatu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

public class TagActivity extends AppCompatActivity {
    EditText editText;
    SharedPreferences data;
    Set<String> tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);

        editText = (EditText) findViewById(R.id.editText3);
        data = getSharedPreferences("DataSave", Context.MODE_PRIVATE);
        tags = data.getStringSet("TAG", new HashSet<String>());
    }

    void saveTag(View V) {
        String text = editText.getText().toString();

        tags.add(text);
        SharedPreferences.Editor editor = data.edit();
        editor.putStringSet("TAG", tags);
        editor.apply();
        Toast.makeText(TagActivity.this, "保存しました", Toast.LENGTH_SHORT).show();
        finish();
    }

}




