package kang.contentapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by kangjonghyuk on 2016. 7. 14..
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    Context mContext;
    List<Recycler_item> items;
    int item_layout;

    public RecyclerAdapter(Context context, List<Recycler_item> items, int item_layout){
        this.mContext = context;
        this.items = items;
        this.item_layout = item_layout;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_info, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvNaesun, tvNumber, tvEmail, tvDepart;

        public ViewHolder(View itemView){
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.name);
            tvNaesun = (TextView) itemView.findViewById(R.id.naesun);
            tvNumber = (TextView) itemView.findViewById(R.id.number);
            tvEmail = (TextView) itemView.findViewById(R.id.email);
            tvDepart = (TextView) itemView.findViewById(R.id.depart);
        }
    }
}
