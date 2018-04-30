package in.ajm.sb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import in.ajm.sb.R;
import in.ajm.sb.data.UserTypeData;
import in.ajm.sb.interfaces.OnUserTypeSelected;

/**
 * Created by DSD on 26/03/18.
 */

public class UserTypeAdapter extends RecyclerView.Adapter<UserTypeAdapter.Holder> {
    private Context context;
    private List<UserTypeData> userTypeDataList;
    private OnUserTypeSelected onUserTypeSelected;

    public UserTypeAdapter(Context context, List<UserTypeData> userTypeDataList, OnUserTypeSelected onUserTypeSelected) {
        this.context = context;
        this.userTypeDataList = userTypeDataList;
        this.onUserTypeSelected = onUserTypeSelected;
    }

    @Override
    public UserTypeAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_type_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(UserTypeAdapter.Holder holder, int position) {
        Holder viewHolder = (Holder) holder;
        viewHolder.setItem(userTypeDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return (null != userTypeDataList ? userTypeDataList.size() : 0);
    }

    public UserTypeData getItem(int position) {
        return userTypeDataList.get(position);
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tvOptionTitle;
        ImageView ivUserImage;
        LinearLayout root;

        public Holder(View itemView) {
            super(itemView);
            tvOptionTitle = (TextView) itemView.findViewById(R.id.tv_user_title);
            ivUserImage = itemView.findViewById(R.id.image_user);
            root = (LinearLayout) itemView;
        }

        public void setItem(final UserTypeData userTypeData) {
            tvOptionTitle.setText(userTypeData.getUserName());
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onUserTypeSelected.userTypeSelected(getAdapterPosition(), userTypeData.getUserName());
                }
            });
        }

    }
}
