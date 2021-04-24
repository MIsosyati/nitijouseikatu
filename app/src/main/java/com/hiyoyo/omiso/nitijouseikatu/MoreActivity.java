package com.hiyoyo.omiso.nitijouseikatu;

import android.content.Context;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class MoreActivity extends AppCompatActivity {

    Realm realm;
    RealmQuery<Plan> query;
    TextView textView;
    ListView listView;
    ListAdapter listAdapter;
    String date;
    String name;
    String starttime;
    String endtime;
    String tag;
    String flec;
    String notif;
    public List<String> morelist = new ArrayList();
    Plan plan;
    List<String> mPlans;

    private final List<String>labelList = Arrays.asList("日付","タグ","通知","周期");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete(name);
            }
        });

        Realm.init(this);
        realm = Realm.getDefaultInstance();
        query = realm.where(Plan.class);

        textView = (TextView) findViewById(R.id.textView);

        date = intent.getStringExtra("date");
        starttime = intent.getStringExtra("starttime");
        endtime = intent.getStringExtra("endtime");
        tag = intent.getStringExtra("tag");
        flec = intent.getStringExtra("flec");
        notif = intent.getStringExtra("notif");
        listView = (ListView)findViewById(R.id.listview);
        mPlans = new ArrayList<String>();
        textView.setText(name);
        morelist.add(date);
        morelist.add(tag);
        morelist.add(notif);
        morelist.add(flec);
        listAdapter = new ListAdapter(this,R.layout.onelist,morelist);
        listView.setAdapter(listAdapter);
    }


    class ListAdapter extends ArrayAdapter<String> {

        public ListAdapter(Context context, int layoutResourceId, List<String> objects) {
            super(context, layoutResourceId, objects);
            mPlans = objects;
        }

        @Override
        public int getCount() {
            return mPlans.size();
        }

        @Override
        public String getItem(int position) {
            return mPlans.get(position);
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ItemHolder itemHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.onelist, null);
                itemHolder = new ItemHolder(convertView);
                convertView.setTag(itemHolder);
            } else {
                itemHolder = (ItemHolder) convertView.getTag();
            }
            final String item = getItem(position);
            //if (item == null) {
                itemHolder.textVIew1.setText(labelList.get(position));
                itemHolder.textView2.setText(morelist.get(position));

            //}
            return convertView;

        }
    }


    public static class ItemHolder {
        TextView textVIew1;
        TextView textView2;


        public ItemHolder(View V) {
            textVIew1 = (TextView) V.findViewById(R.id.name);
            textView2 = (TextView) V.findViewById(R.id.more);


        }
    }
    public void delete(String name){
        final RealmResults<Plan> plans = realm.where(Plan.class).equalTo("name",name).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                plans.deleteAllFromRealm();
              finish();
                Toast.makeText(MoreActivity.this, "削除しました" , Toast.LENGTH_SHORT).show();

            }
        });
    }

}


