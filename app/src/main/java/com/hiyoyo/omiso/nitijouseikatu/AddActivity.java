package com.hiyoyo.omiso.nitijouseikatu;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class AddActivity extends AppCompatActivity {
    public Realm realm;
    //部品の変数
    EditText eventnameedit;
    Spinner tagsp;
    Spinner notisp;
    Spinner perisp;
    LinearLayout linearLayout;
    ImageView imageView;
    ImageView imageView2;
    Button button4;
    EditText editText2;
    String formateDate = "";
    String formateDate2 = "";
    String formateTime = "";


    Date starttime;
    Date endtime;
    RealmQuery<Plan> query;
    List<Float> dataSource = new ArrayList<Float>();
    List<String> newlist = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //部品の取得
        setContentView(R.layout.activity_add);
        tagsp = (Spinner) findViewById(R.id.tag_spinner);
        notisp = (Spinner) findViewById(R.id.noti_spinner);
        perisp = (Spinner) findViewById(R.id.peri_spinner);
        eventnameedit = (EditText) findViewById(R.id.editText);
        linearLayout = (LinearLayout) findViewById(R.id.asu);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        editText2 = (EditText) findViewById(R.id.editText2);

//        リルムの初期化
        Realm.init(this);
        realm = Realm.getDefaultInstance();

//        EditTextにリスナーをつける
        editText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Calendarインスタンスを取得
                final Calendar date = Calendar.getInstance();

                //DatePickerDialogインスタンスを取得
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                //setした日付を取得して表示
                                formateDate = String.format("%d / %02d / %02d", year, month + 1, dayOfMonth);
                                ftime();
                            }
                        },
                        date.get(Calendar.YEAR),
                        date.get(Calendar.MONTH),
                        date.get(Calendar.DATE)
                );
                //dialogを表示
                datePickerDialog.show();
            }

        });
        // ArrayAdapter
        SharedPreferences data = getSharedPreferences("DataSave", Context.MODE_PRIVATE);
        Set<String> spinnerItems = data.getStringSet("TAG", new HashSet<String>());
        spinnerItems.add("選択なし");
        List<String> itemlist = new ArrayList<>(spinnerItems);

        ArrayAdapter<String> adapter
                = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, itemlist);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // spinner に adapter をセット
        tagsp.setAdapter(adapter);
        // リスナーを登録
        tagsp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //　アイテムが選択された時
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                Spinner spinner = (Spinner) parent;
                String item = (String) spinner.getSelectedItem();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

    }


    //ストリング型からdate型へ変化
    public static Date stringToDate(String dateStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.JAPAN);
        Log.d("dateStr", dateStr);
        Log.d("format", format);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    //時間習得＋日付表示？
    //開始時間
    private void ftime() {
        Calendar calendars = Calendar.getInstance();
        TimePickerDialog timePickerDialogs = new TimePickerDialog(
                AddActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                formateTime = String.format("%02d:%02d", hour, minute);
                selectTime();
            }
        }, calendars.get(Calendar.HOUR_OF_DAY), calendars.get(Calendar.MINUTE), true);
        timePickerDialogs.show();
    }

    //終了時間
    private void selectTime() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(AddActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePickers, int hours, int minutes) {
                starttime = stringToDate(formateDate + "　" + formateTime, "yyyy / MM / dd　HH:mm");
                endtime = stringToDate(formateDate + "　" + String.format("%02d:%02d", hours, minutes), "yyyy / MM / dd　HH:mm");
                formateDate2 = formateDate + "　" + formateTime + "〜" + String.format("%02d:%02d", hours, minutes);
                editText2.setText(formateDate2);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

        timePickerDialog.show();

    }

    //完了ボタン操作
    public void add(View v) {
        String name = eventnameedit.getText().toString();
        String date = editText2.getText().toString();

        String tag = (String) tagsp.getSelectedItem();
        String notif = (String) notisp.getSelectedItem();
        String period = (String) perisp.getSelectedItem();
//もしどっちかが空だったら
        if (name.equals("") || date.equals("")) {
            // 空なのでダメ

            if (name.equals("")) {
                linearLayout.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.VISIBLE);

            }

            if (date.equals("")) {
                linearLayout.setVisibility(View.VISIBLE);
                imageView2.setVisibility(View.VISIBLE);
            }

            Snackbar.make(v, "値を入力してください", Snackbar.LENGTH_SHORT).show();
            return;
        }


        //日付取り出し、その日付の予定取り出し
        query = realm.where(Plan.class);
        String datestr = new SimpleDateFormat("yyyy / MM / dd").format(new Date());
        query = realm.where(Plan.class);
        RealmResults<Plan> plans = query.contains("date", datestr).sort("starttime").findAll();

        Log.d("plans", plans.toString());

        //重複処理
        if(plans.size() > 2) {
            for (int i = 0, j = 1; j < plans.size(); i++, j++) {
                Plan plan0 = plans.get(i);
                Plan plan1 = plans.get(j);
                Log.d("plan1", plan1.toString());
                Log.d("Date", String.valueOf(plan0.starttime));

                //名前が重複している場合
                if (name.equals(plan0.name)) {
                    // 重複している
                    linearLayout.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                    Snackbar.make(v, "名前が重複しています", Snackbar.LENGTH_SHORT).show();
                    return;
                }


                // 時間の重複をチェック
                String datesub = date.substring(0, 15);
                String Plandatesub = plan0.date.substring(0, 15);

                if (datesub.equals(Plandatesub)) {

                    if (i == 0) {
                        // 最初の時
                        if ((endtime.before(plan0.starttime) || endtime.equals(plan0.starttime))) {
                            break;
                        }
                    }

                    if ((starttime.after(plan0.endtime) || starttime.equals(plan0.endtime)) &&
                            (endtime.before(plan1.starttime)) || endtime.equals(plan1.starttime)) {
                        Log.d("Add", "plan0 start " + plan0.starttime +
                                ", end " + plan0.endtime);
                        Log.d("Add", "plan1 start " + plan1.starttime +
                                ", end " + plan1.endtime);
                        break;
                    }

                    if (j == plans.size() - 1) {
                        // 最後の時
                        if ((starttime.after(plan1.endtime)) || starttime.equals(plan1.endtime)) {
                            break;
                        } else {
                            linearLayout.setVisibility(View.VISIBLE);
                            imageView2.setVisibility(View.VISIBLE);
                            Snackbar.make(v, "時間が重複しています", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                    }


                }


            }
        }else if(plans.size() == 1) {

                Plan plan0 = plans.get(0);
                Log.d("Date", String.valueOf(plan0.starttime));

                //名前が重複している場合
                if (name.equals(plan0.name)) {
                    // 重複している
                    linearLayout.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                    Snackbar.make(v, "重複しています", Snackbar.LENGTH_SHORT).show();
                    return;
                }


                // 時間の重複をチェック
                String datesub = date.substring(0, 15);
                String Plandatesub = plan0.date.substring(0, 15);

                if (datesub.equals(Plandatesub)) {

                   if (starttime.after(plan0.endtime) ||(starttime.equals(plan0.endtime))){

                   } else{
                       if (starttime.before(plan0.starttime)&&((endtime.before(plan0.starttime))||endtime.equals(plan0.starttime))){

                       }else {
                           Snackbar.make(v, "時間が重複しています", Snackbar.LENGTH_SHORT).show();
                           return;
                       }
                   }

                }
        }

        // セーブする
        save(name, date, tag, notif, period);
    }


    //リルム閉じ、元の画面に戻る　またその画面の動作を終了する
    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    public void save(final String name, final String date, final String tag, final String notif, final String period) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Plan plan = realm.createObject(Plan.class);
                plan.name = name;
                plan.date = date;
                plan.tag = tag;
                plan.notif = notif;
                plan.flec = period;
                plan.starttime = starttime;
                plan.endtime = endtime;

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(AddActivity.this, "保存しました", Toast.LENGTH_SHORT).show();
                finish();

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(AddActivity.this, "保存できませんでした", Toast.LENGTH_SHORT).show();
//                Log.w("realm", "保存失敗", error);

            }
        });
    }
}
