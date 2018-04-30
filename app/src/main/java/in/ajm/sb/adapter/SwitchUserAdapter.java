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
import in.ajm.sb.activities.BaseActivity;
import in.ajm.sb.data.User;
import in.ajm.sb.interfaces.OnUserSwitched;

/**
 * Created by DSD on 26/03/18.
 */

public class SwitchUserAdapter extends RecyclerView.Adapter<SwitchUserAdapter.Holder> {
    private Context context;
    private List<User> arraylist;
    private OnUserSwitched onUserSwitched;

    public SwitchUserAdapter(Context context, List<User> arraylist, OnUserSwitched onUserSwitched) {
        this.context = context;
        this.arraylist = arraylist;
        this.onUserSwitched = onUserSwitched;
    }

    @Override
    public SwitchUserAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_type_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(SwitchUserAdapter.Holder holder, int position) {
        Holder viewHolder = (Holder) holder;
        viewHolder.setItem(arraylist.get(position));
    }

    public void setList(List<User> arraylist) {
        this.arraylist = arraylist;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (null != arraylist ? arraylist.size() : 0);
    }

    public User getItem(int position) {
        return arraylist.get(position);
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tvUserTitle;
        ImageView imageUser;
        LinearLayout root;

        public Holder(View itemView) {
            super(itemView);
            tvUserTitle = (TextView) itemView.findViewById(R.id.tv_user_title);
            imageUser = itemView.findViewById(R.id.image_user);
            root = (LinearLayout) itemView;
        }

        public void setItem(final User user) {
            if(user.isSelected()){
                root.setBackgroundColor(((BaseActivity)context).getAccentColor(context));
            }else{
                root.setBackgroundColor(((BaseActivity)context).getTextColor(context));
            }
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onUserSwitched.onUserSwitched(getAdapterPosition(), user.getUserId());
                    user.setSelected(true);
                    root.setBackgroundColor(((BaseActivity)context).getAccentColor(context));
                    notifyDataSetChanged();
                }
            });
            tvUserTitle.setText(user.getUserName());
        }

    }
}
