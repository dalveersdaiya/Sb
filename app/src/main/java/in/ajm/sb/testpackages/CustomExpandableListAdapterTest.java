package in.ajm.sb.testpackages;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import in.ajm.sb.R;
import in.ajm.sb.data.ClassSectionData;
import in.ajm.sb.interfaces.OnClickAddSection;

/**
 * Created by dsd on 03/02/18.
 */

public class CustomExpandableListAdapterTest extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<ClassSectionData> team;
    private LayoutInflater inflater;
    private OnClickAddSection onClickAddSection;

    public CustomExpandableListAdapterTest(Context context, ArrayList<ClassSectionData> team, OnClickAddSection onClickAddSection) {
        this.context = context;
        this.team = team;
        this.onClickAddSection = onClickAddSection;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    //GET A SINGLE PLAYER
    @Override
    public Object getChild(int groupPos, int childPos) {
        return team.get(groupPos).sections.get(childPos);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(final int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_section, null);
        }
        TextView expandedListTextView = (TextView) convertView.findViewById(R.id.expandedListItem);
        ImageView  imageViewAdd = convertView.findViewById(R.id.image_view_add);
        if(isLastChild){
            imageViewAdd.setVisibility(View.VISIBLE);
        }else{
            imageViewAdd.setVisibility(View.GONE);
        }
        expandedListTextView.setText(expandedListText);
        imageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAddSection.onClickAddSection(team.get(listPosition).className, listPosition, expandedListPosition);
            }
        });
        return convertView;
    }

    //GET NUMBER OF PLAYERS
    @Override
    public int getChildrenCount(int groupPosw) {
        // TODO Auto-generated method stub
        return team.get(groupPosw).sections.size();
    }

    @Override
    public Object getGroup(int groupPos) {
        // TODO Auto-generated method stub
        return team.get(groupPos);
    }

    //GET NUMBER OF TEAMS
    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return team.size();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
    }


    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        ClassSectionData t=(ClassSectionData) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_class, null);
        }
        TextView listTitleTextView = convertView
                .findViewById(R.id.listTitle);
        ImageView img= convertView.findViewById(R.id.listimagetitle);
        ImageView imageViewAdd  = convertView.findViewById(R.id.image_view_add);
        if(team.get(groupPosition).sections.size() == 0){
            imageViewAdd.setVisibility(View.VISIBLE);
        }else{
            imageViewAdd.setVisibility(View.GONE);
        }
        imageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAddSection.onClickAddSection(team.get(groupPosition).className, groupPosition, 0);
            }
        });

        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(t.className);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    public void setList(ArrayList<ClassSectionData> team){
        this.team = team;
        notifyDataSetChanged();
    }
}
