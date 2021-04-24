package com.hiyoyo.omiso.nitijouseikatu;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import com.afollestad.materialdialogs.MaterialDialog;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class CalendarAdapter extends BaseAdapter {
    private List<Date> dateArray = new ArrayList();
    private Context mContext;
    private DateManager mDateManager;
    private LayoutInflater mLayoutInflater;
    ArrayList objects = new ArrayList<>();
    Realm realm;
    Resources resources;
    Plan plan = new Plan();
    static ArrayAdapter<String> adapter;
    RealmQuery<Plan> query;

    //カスタムセルを拡張したらここでWigetを定義
    private static class ViewHolder {
        public TextView dateText;
    }

    public CalendarAdapter(Context context, Realm realm) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mDateManager = new DateManager();
        dateArray = mDateManager.getDays();
        this.realm = realm;
        //リルムのデータを取得
         query = realm.where(Plan.class);
        //RealmResults<Plan> result = query.findAll();
    }

    @Override
    public int getCount() {
        return dateArray.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.calendar_cell, null);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //日付の取り出しまた絞り込み
                    String datestr = new SimpleDateFormat("yyyy / MM / dd").format(dateArray.get(position));
                    query = realm.where(Plan.class);
                    final RealmResults<Plan> plans  = query.contains("date",datestr).findAll();
                   final List<Plan> editableplans = new ArrayList<>();
                    List<String> planDetail = new ArrayList<String>();
                    for (Plan plan: plans){
                        planDetail.add(plan.name);
                        editableplans.add(plan);
                    }

                    Log.d("date", datestr);
                    //String.format("%d / %02d / %02d", year, month + 1, dayOfMonth);

                    new MaterialDialog.Builder(mContext)
                            .title("当日の予定")
                            .items(planDetail)
                            .contentColor(Color.BLACK)
                            .positiveText("閉じる")
                            .positiveColor(Color.BLACK)
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                //予定が押された時の処理
                                public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                                    Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(view.getContext(), MoreActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("name", text);
                                    intent.putExtra("date",editableplans.get(which).date);
                                    intent.putExtra("starttime",editableplans.get(which).starttime);
                                    intent.putExtra("endtime",editableplans.get(which).endtime);
                                    intent.putExtra("tag",editableplans.get(which).tag);
                                    intent.putExtra("notif",editableplans.get(which).notif);
                                    intent.putExtra("flec",editableplans.get(which).flec);
                                    view.getContext().startActivity(intent);
                                }
                            })
                            //長押ししたらコマンド
                            .itemsLongCallback(new MaterialDialog.ListLongCallback(){

                                @Override
                                public boolean onLongSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {

                                    return false;
                                }
                            })
                            .show();
                }
            });
            holder = new ViewHolder();
            holder.dateText = convertView.findViewById(R.id.dateText);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //セルのサイズを指定
        float dp = mContext.getResources().getDisplayMetrics().density;
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(parent.getWidth() / 7 - (int) dp, (parent.getHeight() - (int) dp * mDateManager.getWeeks()) / mDateManager.getWeeks());
        convertView.setLayoutParams(params);

        //日付のみ表示させる
        SimpleDateFormat dateFormat = new SimpleDateFormat("d", Locale.US);
        holder.dateText.setText(dateFormat.format(dateArray.get(position)));

        //当月以外のセルをグレーアウト
        if (mDateManager.isCurrentMonth(dateArray.get(position))) {
            convertView.setBackgroundColor(ContextCompat.getColor(mContext,R.color.whiteColor));
        } else {
            convertView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.lightgraycolor));
        }

        //日曜日を赤、土曜日を青に
        int colorId;
        switch (mDateManager.getDayOfWeek(dateArray.get(position))) {
            case 1:
                colorId = Color.RED;
                break;
            case 7:
                colorId = Color.BLUE;
                break;

            default:
                colorId = Color.BLACK;
                break;
        }
        holder.dateText.setTextColor(colorId);

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    //表示月を取得
    public String getTitle() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM", Locale.US);
        return format.format(mDateManager.mCalendar.getTime());
    }

    //翌月表示
    public void nextMonth() {
        mDateManager.nextMonth();
        dateArray = mDateManager.getDays();
        this.notifyDataSetChanged();
    }

    //前月表示
    public void prevMonth() {
        mDateManager.prevMonth();
        dateArray = mDateManager.getDays();
        this.notifyDataSetChanged();
    }
}

