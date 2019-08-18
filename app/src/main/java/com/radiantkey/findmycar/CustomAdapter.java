package com.radiantkey.findmycar;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CustomAdapter extends BaseAdapter {
    private Context mContext;
    private List<ItemContainer> mItemList;

    DatabaseHelper mdb;

    public CustomAdapter(Context mContext, List<ItemContainer> mItemList) {
        this.mContext = mContext;
        this.mItemList = mItemList;
    }

    @Override
    public int getCount() {
        return mItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return mItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext, R.layout.listview_item,null);
        TextView itemName = (TextView) v.findViewById(R.id.name);
        TextView date = (TextView) v.findViewById(R.id.date);
        TextView time = (TextView) v.findViewById(R.id.time);

        itemName.setText(mItemList.get(i).getName());

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mItemList.get(i).getTime());
        date.setText(formatter.format(calendar.getTime()));
        formatter = new SimpleDateFormat("HH:mm:ss", Locale.US);
        time.setText(formatter.format(calendar.getTime()));

        v.setTag(mItemList.get(i).getId());
        return v;
    }
}
