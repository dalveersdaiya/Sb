package in.ajm.sb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import in.ajm.sb.R;
import in.ajm.sb.data.ClassOptions;
import in.ajm.sb.interfaces.OnClassItemClick;

/**
 * Created by DSD on 26/03/18.
 */

public class ClassOptionAdapter extends RecyclerView.Adapter<ClassOptionAdapter.Holder> {
    private Context context;
    private List<ClassOptions> arraylist;
    private OnClassItemClick onClassItemClick;

    public ClassOptionAdapter(Context context, List<ClassOptions> arraylist, OnClassItemClick onClassItemClick) {
        this.context = context;
        this.arraylist = arraylist;
        this.onClassItemClick = onClassItemClick;
    }

    @Override
    public ClassOptionAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.option_list_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(ClassOptionAdapter.Holder holder, int position) {
        Holder viewHolder = (Holder) holder;
        viewHolder.setItem(arraylist.get(position));
    }

    @Override
    public int getItemCount() {
        return (null != arraylist ? arraylist.size() : 0);
    }

    public ClassOptions getItem(int position) {
        return arraylist.get(position);
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tvOptionTitle;
        LinearLayout root;

        public Holder(View itemView) {
            super(itemView);
            tvOptionTitle = (TextView) itemView.findViewById(R.id.tv_option_title);
            root = (LinearLayout) itemView;
        }

        public void setItem(ClassOptions classOptions) {
            tvOptionTitle.setText(context.getResources().getString(R.string.select_class));
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClassItemClick.onClassItemClicked();
                }
            });
        }

    }
}
