package in.ajm.sb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private List<UserTypeData> arraylist;
    private OnUserTypeSelected onUserTypeSelected;

    public UserTypeAdapter(Context context, List<UserTypeData> arraylist,  OnUserTypeSelected onUserTypeSelected) {
        this.context = context;
        this.arraylist = arraylist;
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
        viewHolder.setItem(arraylist.get(position));
    }

    @Override
    public int getItemCount() {
        return (null != arraylist ? arraylist.size() : 0);
    }

    public UserTypeData getItem(int position) {
        return arraylist.get(position);
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tv_option_title;
        ImageView iv_user_image;

        public Holder(View itemView) {
            super(itemView);
            tv_option_title = (TextView) itemView.findViewById(R.id.tv_user_title);
            iv_user_image = itemView.findViewById(R.id.image_user);
        }

        public void setItem(final UserTypeData userTypeData) {
            tv_option_title.setText(userTypeData.getUserName());
            tv_option_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onUserTypeSelected.userTypeSelected(getAdapterPosition(), userTypeData.getUserName());
                }
            });
        }

    }
}
