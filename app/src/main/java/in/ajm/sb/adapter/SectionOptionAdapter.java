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
import in.ajm.sb.data.SectionOptions;
import in.ajm.sb.interfaces.OnSectionItemClick;

/**
 * Created by DSD on 26/03/18.
 */

public class SectionOptionAdapter extends RecyclerView.Adapter<SectionOptionAdapter.Holder> {
    private Context context;
    private List<SectionOptions> arraylist;
    private OnSectionItemClick onSectionItemClick;

    public SectionOptionAdapter(Context context, List<SectionOptions> arraylist, OnSectionItemClick onSectionItemClick) {
        this.context = context;
        this.arraylist = arraylist;
        this.onSectionItemClick = onSectionItemClick;
    }

    @Override
    public SectionOptionAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.option_list_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(SectionOptionAdapter.Holder holder, int position) {
        Holder viewHolder = (Holder) holder;
        viewHolder.setItem(arraylist.get(position));
    }

    @Override
    public int getItemCount() {
        return (null != arraylist ? arraylist.size() : 0);
    }

    public SectionOptions getItem(int position) {
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

        public void setItem(SectionOptions sectionOptions) {
            tvOptionTitle.setText(context.getResources().getString(R.string.select_section));
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSectionItemClick.onSectionItemClicked();
                }
            });
        }

    }
}
