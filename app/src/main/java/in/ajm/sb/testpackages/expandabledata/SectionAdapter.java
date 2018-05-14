package in.ajm.sb.testpackages.expandabledata;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.ajm.sb.R;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.Holder> {

    private Context context;
    List<Sections> sectionsList = new ArrayList<>();


    public SectionAdapter(Context context, List<Sections> sectionsList) {
        this.context = context;
        this.sectionsList = sectionsList;
    }

    public List<Sections> getList() {
        return sectionsList;
    }

    @Override
    public int getItemCount() {
        return sectionsList != null ? sectionsList.size() : 0;
    }

    public Sections getItem(int i) {
        return sectionsList.get(i);
    }

    public void addItem(String name, int pos) {
        Sections sections = new Sections();
        sections.setSectionName(name);
        sections.setClassName("");
        sections.setNoOfStudents(2);
        sectionsList.add(sections);
    }


    @Override
    public SectionAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        return new SectionAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(final SectionAdapter.Holder holder, final int position) {
        holder.tvSectionName.setText(sectionsList.get(position).getSectionName());
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tvSectionName;

        public Holder(View itemView) {
            super(itemView);
            tvSectionName =  itemView.findViewById(R.id.name_txt);
        }

        public void setData(Sections sections){
            tvSectionName.setText(sections.getSectionName());
        }

    }

}
