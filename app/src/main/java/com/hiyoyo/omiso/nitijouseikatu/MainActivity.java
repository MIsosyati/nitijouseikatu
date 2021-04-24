package com.hiyoyo.omiso.nitijouseikatu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static android.graphics.Color.BLACK;

public class MainActivity extends AppCompatActivity {
    PieChart pieChart;
    Realm realm;
    RealmQuery<Plan> query;
    List<Float> dataSource = new ArrayList<Float>();
    Boolean isAM = true;
    TextView textView;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView5;
    Button button;
    List<Integer> colors;


    //スプラッシュ画面、サブバー設定
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {

        }
        // スプラッシュthemeを通常themeに変更する
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);
//リルムの初期化
        Realm.init(this);
        realm = Realm.getDefaultInstance();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menuNav = navigationView.getMenu();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_add:
//                        Toast.makeText(MainActivity.this, "nav_camera", Toast.LENGTH_SHORT).show();
//                       //ここに画面推移を書けば良い
                        toAdd();

                        break;
                    case R.id.nav_galley:
                        Toast.makeText(MainActivity.this, "nav_gallery", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_sideshow:
                        Toast.makeText(MainActivity.this, "タゲ一覧を表示します", Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.nav_manage:
                        Toast.makeText(MainActivity.this, "nav_manage", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        break;

                }
                return false;

            }
        });
        colors = createColorSet();
        TextView dateText = (TextView) findViewById(R.id.textView_today);
// 現在の時刻を取得
        Date date = new Date();
// 表示形式を設定
        SimpleDateFormat sdf = new SimpleDateFormat("MM 月 dd 日");
        dateText.setText(sdf.format(date));
}



    //パイチャート表示
    @Override
    protected void onResume() {
        super.onResume();

        setPieChartView(isAM);
    }

    private void toAdd() {
        Intent intent = new Intent(this, TagActivity.class);
        startActivity(intent);
    }

    //画面推移新規作成
    public void button(View V) {
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }

    //画面推移カレンダー
    public void button2(View V) {
        Intent intent = new Intent(this, CalendarActivity.class);
        startActivity(intent);
    }


    private void setPieChartView(Boolean isAM) {
        pieChart = findViewById(R.id.pie);
        pieChart.setUsePercentValues(true);
        //本日の予定実装
        Description desc = new Description();
        pieChart.setCenterText("本日の予定");
        pieChart.setCenterTextSize(20);

        //日付取り出し、その日付の予定取り出し
        query = realm.where(Plan.class);
        String datestr = new SimpleDateFormat("yyyy / MM / dd").format(new Date());
        query = realm.where(Plan.class);
        RealmResults<Plan> plans = query.contains("date", datestr).sort("starttime").findAll();
        dataSource = new ArrayList<Float>();
        List<String> newlist = new ArrayList<String>();


        float baseTime = 0.0f;
        for (Plan plan : plans) {
            Log.d("Date", String.valueOf(plan.starttime));

            float starttime = (float) (plan.starttime.getHours() * 60 + plan.starttime.getMinutes()) - baseTime;
            float endtime = (float) plan.endtime.getHours() * 60 + plan.endtime.getMinutes() - baseTime;

            //if (isAM && endtime < 720) {

            dataSource.add(starttime);
            newlist.add("");
            dataSource.add(endtime - starttime);
            newlist.add(plan.name);
            baseTime = plan.endtime.getHours() * 60 + plan.endtime.getMinutes();

            plan.starttime.getHours();
            plan.starttime.getMinutes();
            plan.endtime.getHours();
            plan.endtime.getMinutes();

            Log.d("starttime", String.valueOf(plan.starttime.getHours() * 60 + plan.starttime.getMinutes()));
            Log.d("endtime", String.valueOf(plan.endtime.getHours() * 60 + plan.endtime.getMinutes()));
            Log.d("starttime1", String.valueOf(plan.starttime.getMinutes()));
            Log.d("endtime1", String.valueOf(plan.endtime.getMinutes()));

        }


        dataSource.add((float) (24 * 60 - baseTime));
        newlist.add("");
        pieChart.setDescription(desc);

        //piechartの設定
        Legend legend = pieChart.getLegend();
        legend.setTextColor(BLACK);
        pieChart.animateXY(2000, 2000); // 表示アニメーション
        pieChart.setRotationEnabled(false);//回転可能かどうか

        ArrayList<PieEntry> entry = new ArrayList<>();


        //デザイン設定
        PieDataSet dataSet = new PieDataSet(entry, "予定なし");
        dataSet.setDrawValues(true);
//        dataSet.setSliceSpace(4.4f);//間に白い枠を入れる
        dataSet.setSelectionShift(12.35f);//円グラフの大きさ


        final int[] MY_COLORS = {
                Color.rgb(192, 255, 140), Color.rgb(255, 247, 140), Color.rgb(255, 208, 140),
                Color.rgb(140, 234, 255), Color.rgb(255, 140, 157)
        };
        dataSet.setColors(MY_COLORS);
        //％数字表記設定
        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter((IValueFormatter) (new PercentFormatter()));
        pieData.setDrawValues(false);

        LineDataSet lineDataSet;

        // 画面の PiChart の表示に使うカラーセット
        List<Integer> colorList = new ArrayList<>();
        //割合の設定（ループ）
        // ((Collection) dataSource).size() の大きさまで for 文を回す
        // i は 0 から (dataSource の大きさ - 1) まで回る
        // → dataSource 全てのデータを entry に入れる
        for (int var7 = ((Collection) dataSource).size(), i = 0; i < var7; ++i) {
            Object var1003 = dataSource.get(i);
            entry.add(new PieEntry(((Number) var1003).floatValue(), newlist.get(i)));
            // entry に追加される要素を表示
            Log.d("entry", var7 + "label:" + newlist.get(i));

            if ("".equals(newlist.get(i))) {
                colorList.add(Color.rgb(195, 195, 195));
            } else {
//                colorList.add(Color.rgb(192, 255, 140));
                colorList.add(colors.get(0));
                Collections.rotate(colors, 1);
            }
        }



        dataSet.setColors(colorList);

        pieChart.setEntryLabelColor(BLACK);
        pieChart.setEntryLabelTextSize(15f);
        pieChart.setData(pieData);
        pieChart.invalidate(); // refresh

    }

    private List<Integer> createColorSet() {
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(192, 255, 140));
        colors.add(Color.rgb(255, 247, 140));
        colors.add(Color.rgb(255, 208, 140));
        colors.add(Color.rgb(140, 234, 255));
        colors.add(Color.rgb(255, 140, 157));
        return colors;
    }



   @Override
   protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}