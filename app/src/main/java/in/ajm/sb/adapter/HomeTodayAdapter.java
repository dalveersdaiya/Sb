package in.ajm.sb.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import in.ajm.sb.R;
import in.ajm.sb.activities.BaseActivity;
import in.ajm.sb.data.HomeTodayData;
import in.ajm.sb.interfaces.OnThisDayItemClicked;

/**
 * Created by DSD on 26/03/18.
 */

public class HomeTodayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    OnThisDayItemClicked onThisDayItemClicked;
    boolean showScreenShotInfo;
    private Context context;
    private List<HomeTodayData> arraylist;

    public HomeTodayAdapter(Context context, List<HomeTodayData> arraylist, OnThisDayItemClicked onThisDayItemClicked, boolean showScreenShotInfo) {
        this.context = context;
        this.arraylist = arraylist;
        this.onThisDayItemClicked = onThisDayItemClicked;
        this.showScreenShotInfo = showScreenShotInfo;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.home_today_test_item, parent, false);
            return new MyItemViewHolder(view);

        } else if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_for_screen_shot, parent, false);
            return new HeaderViewHolder(view);

        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }

    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    private boolean isPositionFooter(int position) {
        return position > arraylist.size();
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder viewHolder = (HeaderViewHolder) holder;
            viewHolder.setItem(getItem(position));
        } else if (holder instanceof MyItemViewHolder) {
            MyItemViewHolder viewHolder = (MyItemViewHolder) holder;
            viewHolder.setItem(getItem(position - 1));
        }
    }

    @Override
    public int getItemCount() {
        return (null != arraylist ? arraylist.size() + 1 : 0);
    }

    public HomeTodayData getItem(int position) {
        return arraylist.get(position);
    }

    public class MyItemViewHolder extends RecyclerView.ViewHolder {
        TextView tv_student_name;
        TextView tv_total_marks;
        TextView tv_result_marks;

        LinearLayout root;

        public MyItemViewHolder(View itemView) {
            super(itemView);
            tv_student_name = (TextView) itemView.findViewById(R.id.tv_student_name);
            tv_total_marks = (TextView) itemView.findViewById(R.id.tv_total_marks);
            tv_result_marks = (TextView) itemView.findViewById(R.id.tv_result_marks);
            root = (LinearLayout) itemView;
        }

        public void setItem(final HomeTodayData homeTodayData) {
            tv_student_name.setText(homeTodayData.getUserName());
            tv_total_marks.setText(homeTodayData.getTotalMarks());
            tv_result_marks.setText(homeTodayData.getResult());
            if (homeTodayData.isPersonal()) {
                root.setBackgroundColor(((BaseActivity) context).getColorMyThemelight(context));
                tv_result_marks.setTextColor(((BaseActivity) context).getTextColor(context));
                tv_total_marks.setTextColor(((BaseActivity) context).getTextColor(context));
                tv_student_name.setTextColor(((BaseActivity) context).getTextColor(context));

            }
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (homeTodayData.isPersonal()) {
                        ((BaseActivity) context).showDialog(context, context.getResources().getString(R.string.want_to_see_more_of, homeTodayData.getUserName()), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if (onThisDayItemClicked != null) {
                                    onThisDayItemClicked.onThisDayItemClicked();
                                }
                            }
                        });
                    }
                }
            });
        }

    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        public View View;
        LinearLayout linearScreenShotInfo;
        TextView tvCurrentYear;
        TextView tv_selected_school;
        TextView tv_current_class;
        TextView tv_current_section;
        TextView tv_this_day;
        TextView tv_user_name;


        public HeaderViewHolder(View itemView) {
            super(itemView);
            linearScreenShotInfo = itemView.findViewById(R.id.linear_screen_shot_info);
            ((BaseActivity) context).expandingView(linearScreenShotInfo);
            tvCurrentYear = itemView.findViewById(R.id.tv_current_year);
            tv_selected_school = itemView.findViewById(R.id.tv_selected_school);
            tv_current_class = itemView.findViewById(R.id.tv_current_class);
            tv_current_section = itemView.findViewById(R.id.tv_current_section);
            tv_current_section = itemView.findViewById(R.id.tv_current_section);
            tv_this_day = itemView.findViewById(R.id.tv_this_day);
            tv_user_name = itemView.findViewById(R.id.tv_user_name);
            View = itemView;
            if (showScreenShotInfo) {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                },200);
                ((BaseActivity) context).expand(300, linearScreenShotInfo);
            }

        }

        public void setItem(final HomeTodayData homeTodayData) {
            if (homeTodayData.isPersonal()) {
                tv_current_class.setText(homeTodayData.getClassname());
                tv_current_section.setText(homeTodayData.getSection());
                tv_selected_school.setText(homeTodayData.getSchoolName());
                tvCurrentYear.setText(homeTodayData.getCurrentYear());
                tv_this_day.setText(homeTodayData.getDate());
                tv_user_name.setText(homeTodayData.getUserName());
            }

        }
    }


}
