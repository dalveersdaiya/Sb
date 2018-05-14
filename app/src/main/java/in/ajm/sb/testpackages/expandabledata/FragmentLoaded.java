package in.ajm.sb.testpackages.expandabledata;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

import in.ajm.sb.R;

public  class FragmentLoaded extends android.support.v4.app.Fragment {

    public static FragmentLoaded newInstance(Classes classes) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("classes", classes);
        FragmentLoaded f = new FragmentLoaded();
        f.setArguments(bundle);
        return f;
    }

    RecyclerView recyclerView;
    Classes classes;
    SectionAdapter sectionAdapter;
    List<Sections> sectionsList = new ArrayList<>();

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    ArrayList<Sections> allSections;
    CustomExpandableListAdapterTestNew myAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment, container, false);
        classes = getArguments().getParcelable("classes");
//        ((TextView) findById(v, R.id.name_txt)).setText(dog.getName());
//        ((TextView) findById(v, R.id.favorite_txt)).setText(dog.getFavoriteFood());
//        ((TextView) findById(v, R.id.age_txt)).setText(String.valueOf(dog.getAge()));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        setRecyclerView();
        setExpandableListView();
    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        expandableListView = view.findViewById(R.id.expandableListView);
    }

    private void setExpandableListView() {
        allSections = new ArrayList<>();
        final List<Sections> team = classes.getSectionsList();
        expandableListAdapter = new CustomExpandableListAdapterTestNew(getActivity(), team);
        myAdapter = (CustomExpandableListAdapterTestNew) expandableListAdapter;
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });
    }


    public void setRecyclerView(){
        sectionsList = classes.getSectionsList();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        sectionAdapter = new SectionAdapter(getActivity(), sectionsList);
        recyclerView.setAdapter(sectionAdapter);
        recyclerView.setVisibility(View.GONE);
    }
}
