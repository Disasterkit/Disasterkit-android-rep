package com.android.rivchat.ui.DownloadMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.rivchat.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adapter extends SimpleAdapter {

    // private Context context;
    private LayoutInflater inflater;
    private List<? extends Map<String, ?>> listData;

    public class ViewHolder {
        TextView line1;
        TextView line2;
        Button downloadButton;
    }

    public Adapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        // TODO 自動生成されたコンストラクター・スタブ
        // this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listData = data;
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder;

        // ビューを受け取る
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.row, parent, false);
            // LayoutInflater inflater = (LayoutInflater)
            // context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // view = inflater.inflate(R.layout.raw, null);

            holder = new ViewHolder();
            holder.line1 = (TextView) view.findViewById(android.R.id.text1);
            holder.line2 = (TextView) view.findViewById(android.R.id.text2);
            holder.downloadButton = (Button) view.findViewById(R.id.buttondownload);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        String text1 = ((HashMap<?, ?>) listData.get(position)).get("title").toString();
        String text2 = ((HashMap<?, ?>) listData.get(position)).get("comment").toString();
        holder.line1.setText(text1);
        holder.line2.setText(text2);

        //Button btn = (Button) view.findViewById(R.id.button1);
        //btn.setTag(position);
        holder.downloadButton.setTag(position);

        /*
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO 自動生成されたメソッド・スタブ
                Log.v("buttonクリック", "ポジション：　" + position);
                if(holder.line2.getVisibility() == android.view.View.GONE){
                    holder.line2.setVisibility(View.VISIBLE);
                }else
                    holder.line2.setVisibility(View.GONE);
            }
        });
        */

        holder.downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ListView) parent).performItemClick(view, position, R.id.buttondownload);
            }
        });


        return view;
    }

    /*
    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    */
}

