package com.hiyoyo.omiso.nitijouseikatu;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.GridView;

import io.realm.Realm;


public class CalendarActivity extends AppCompatActivity {
    private TextView titleText;
    private Button prevButton, nextButton;
    private CalendarAdapter mCalendarAdapter;
    private GridView calendarGridView;
    public Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        //リルムの初期化
        Realm.init(this);
        realm = Realm.getDefaultInstance();


        titleText = findViewById(R.id.titleText);
        prevButton = findViewById(R.id.prevButton);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarAdapter.prevMonth();
                titleText.setText(mCalendarAdapter.getTitle());
            }
        });
        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarAdapter.nextMonth();
                titleText.setText(mCalendarAdapter.getTitle());
            }
        });
        calendarGridView = findViewById(R.id.calendarGridView);
        mCalendarAdapter = new CalendarAdapter(this,realm);
        calendarGridView.setAdapter(mCalendarAdapter);
        titleText.setText(mCalendarAdapter.getTitle());
//リルムの初期化
        //Realm.init(this);
        //realm = Realm.getDefaultInstance();


    }

    //public void read() {
        //リルムのデータを取得
       // RealmQuery<Plan> query = realm.where(Plan.class);
//データを設定
      //  RealmResults<Plan> result = query.findAll();
        //for (int i = 0; i < result.size(); i++) {
          //  Plan plan = new Plan();


        //}


       // }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}

