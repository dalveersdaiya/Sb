package in.ajm.sb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import in.ajm.sb.R;
import in.ajm.sb.api.model.schooladmin.Classes;

public class ClassSectionAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Classes> classSectionDataArrayList;
    private LayoutInflater inflater;

    public ClassSectionAdapter(Context context, List<Classes> classSectionDataArrayList) {
        this.context = context;
        this.classSectionDataArrayList = classSectionDataArrayList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //GET A SINGLE PLAYER
    @Override
    public Object getChild(int groupPos, int childPos) {
        return classSectionDataArrayList.get(groupPos).getSections().get(childPos);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_section, null);
        }
        TextView expandedListTextView = (TextView) convertView
                .findViewById(R.id.expandedListItem);
        expandedListTextView.setText(expandedListText);
        return convertView;
    }

    //GET NUMBER OF PLAYERS
    @Override
    public int getChildrenCount(int groupPosw) {
        // TODO Auto-generated method stub
        return classSectionDataArrayList.get(groupPosw).getSections().size();
    }

    @Override
    public Object getGroup(int groupPos) {
        // TODO Auto-generated method stub
        return classSectionDataArrayList.get(groupPos);
    }

    //GET NUMBER OF TEAMS
    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return classSectionDataArrayList.size();
    }


    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
//        ClassSectionData classSectionData = (ClassSectionData) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_class, null);
        }
        TextView listTitleTextView = convertView.findViewById(R.id.listTitle);
        ImageView imageView = convertView.findViewById(R.id.listimagetitle);
        listTitleTextView.setText(classSectionDataArrayList.get(groupPosition).getClassName());
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
}

