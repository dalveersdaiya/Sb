package in.ajm.sb.activities.expandabledata;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

import in.ajm.sb.R;
import in.ajm.sb.activities.BaseActivity;
import in.ajm.sb.adapter.ClassSectionAdapter;
import in.ajm.sb.api.model.schooladmin.Classes;
import in.ajm.sb.api.model.schooladmin.Sections;
import in.ajm.sb.data.ClassSectionData;
import in.ajm.sb.fragments.BottomSheetAddClassSection;
import in.ajm.sb.helper.AppConfigs;
import in.ajm.sb.interfaces.OnClickAddClassSection;
import io.realm.RealmList;

public class ExpandableClassExample extends BaseActivity implements OnClickAddClassSection, View.OnClickListener {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    FloatingActionButton fab;
    ArrayList<ClassSectionData> allTeams;
    List<Classes> classesList;
    List<Sections> sectionsList;
    String className = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkForTheme(this);
        setContentView(R.layout.activity_expandable_class_example);
        viewByIds();
        setUi();
        applyClickListeners();
        setExpandableListView();
    }

    private void viewByIds() {
        expandableListView = findViewById(R.id.expandableListView);
        fab = findViewById(R.id.fab);
        allTeams = new ArrayList<>();
    }

    private void setExpandableListView() {
//        final ArrayList<ClassSectionData> team = getData();
        classesList = new ArrayList<>();
        sectionsList = new ArrayList<>();
        expandableListAdapter = new ClassSectionAdapter(this, classesList);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                className = classesList.get(groupPosition).getClassName();
                return false;
            }
        });
    }

    private void applyClickListeners() {
        fab.setOnClickListener(this);
    }

    public void setUi() {
        setupToolBar(getResources().getString(R.string.test), true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private ArrayList<ClassSectionData> getData() {
        ClassSectionData t1 = new ClassSectionData("Class 1");
        t1.sections.add("A");
        t1.sections.add("B");
        t1.sections.add("C");
        t1.sections.add("D");
        ClassSectionData t2 = new ClassSectionData("Class 2");
        t2.sections.add("A");
        t2.sections.add("B");
        t2.sections.add("C");
        t2.sections.add("D");
        ClassSectionData t3 = new ClassSectionData("Class 3");
        t3.sections.add("A");
        t3.sections.add("B");
        ClassSectionData t4 = new ClassSectionData("Class 4");
        t4.sections.add("A");
        allTeams = new ArrayList<ClassSectionData>();
        allTeams.add(t1);
        allTeams.add(t2);
        allTeams.add(t3);
        allTeams.add(t4);

        return allTeams;
    }

    public void openOtpBottomSheet(int type) {
        BottomSheetDialogFragment bottomSheetDialogFragment = new BottomSheetAddClassSection(this, type, className);
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }

//    private void addNewClass(String className){
//        ClassSectionData classSectionData = new ClassSectionData(className);
//        allTeams.add(classSectionData);
//        expandableListAdapter.notify();
//    }

//    private void addNewSection(String className){
//        ClassSectionData classSectionData;
//        for (int i = 0; i < allTeams.size(); i++) {
//            allTeams.get(i).classImage
//        }
//        allTeams.add(classSectionData);
//        expandableListAdapter.notify();
//    }


//    FIXME
    private void addClasses(String className) {
        beginRealmTransaction();
        Classes classes = new Classes();
        classes.setClassName(className);
        commitAndCloseRealmTransaction(classes);
        classesList.add(classes);
        synchronized(expandableListAdapter){
            expandableListAdapter.notify();
        }
    }
    //    FIXME NOTE
    private void addSections(String className, String sectionName) {
        Classes classes = Classes.getByClassName(className);
        beginRealmTransaction();
        Sections sections = new Sections();
        sections.setSectionName(sectionName);
        sectionsList.add(sections);
        realm.copyToRealmOrUpdate(sections);
        RealmList<Sections> sectionsRealmList = new RealmList<>();
        sectionsRealmList.set(sectionsRealmList.size() -1 , sections);
        classes.setSections(sectionsRealmList);
        commitAndCloseRealmTransaction(classes);
        synchronized(expandableListAdapter){
            expandableListAdapter.notify();
        }
    }

    @Override
    public void onAddClass(String classname) {
        addClasses(classname);
    }

    @Override
    public void onAddSection(String classname, String sectionName) {
        addSections(classname, sectionName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                openOtpBottomSheet(AppConfigs.ADD_CLASS);
                break;
        }
    }
}
