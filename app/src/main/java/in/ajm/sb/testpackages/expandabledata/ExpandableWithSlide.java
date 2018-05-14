package in.ajm.sb.testpackages.expandabledata;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import in.ajm.sb.R;
import in.ajm.sb_library.expandableadapter.ArrayFragmentStatePagerAdapter;


public class ExpandableWithSlide extends AppCompatActivity {

    Toolbar toolbar;
    Button buttonClass;
    Button buttonStudent;
    TabLayout tabLayout;
    ViewPager viewPager;
    ArrayList<Classes> classesArrayList;
    MyStatePagerAdapter adapter;
    Button buttonSection;
    int currentClassPosition = 0;
    int currentSectionPosition = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_with_slide);
        viewByIds();
        setSupportActionBar(toolbar);
        applyClickListeners();
        setViewPager();
    }


    public void viewByIds() {
        toolbar = findViewById(R.id.toolbar);
        buttonClass = findViewById(R.id.button_class);
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        buttonSection = findViewById(R.id.button_section);
        buttonStudent = findViewById(R.id.button_student);
    }

    public void applyClickListeners() {
        buttonClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addClass();
            }
        });
        buttonSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSection();
            }
        });
        buttonStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudent();
            }
        });
    }

    /**
     * This method should be dynamic : It should depend on the school selection :
     * Eg. If admin creates a primary school he should be provided with the five classes
     * each with a single section. then he can create more sections or classes and so on.
     */
    public void setViewPager() {
        classesArrayList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Classes classes = new Classes();
            classes.className = "Class " + (1 + i);
            classes.classId = String.valueOf(i);
            classes.noOfSections = String.valueOf(i + 1);
            classes.sectionsList = new ArrayList<>();
            for (int j = 0; j < 1; j++) {
                Sections sections = new Sections();
                sections.setSectionName("Section " + (j + 1));
                sections.setNoOfStudents(40);
                sections.setClassName(classes.getClassName());
                classes.sectionsList.add(sections);
                for (int k = 0; k < 10; k++) {
                    StudentsList studentsList = new StudentsList();
                    studentsList.setRollNum(String.valueOf(k));
                    studentsList.setSectionName(sections.getSectionName());
                    studentsList.setStudentName("Student " + k);
                    classes.getSectionsList().get(j).studentsLists.add(studentsList);
                }
            }
            classesArrayList.add(classes);
        }
        adapter = new MyStatePagerAdapter(getSupportFragmentManager(), classesArrayList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        buttonStudent.setEnabled(false);

    }

    private void addClass() {
        currentClassPosition = classesArrayList.size() -1;
        Classes classes = new Classes();
        classes.className = "Class " + (1 + (classesArrayList.size()));
        classes.classId = String.valueOf(classesArrayList.size() + 1);
        classes.noOfSections = String.valueOf(classesArrayList.size() + 1);
        classes.sectionsList = new ArrayList<>();
        for (int j = 0; j < 1; j++) {
            Sections sections = new Sections();
            sections.setSectionName("Section " + (j + 1));
            sections.setNoOfStudents(40);
            sections.setClassName(classes.getClassName());
            classes.sectionsList.add(sections);
            for (int k = 0; k < 10; k++) {
                StudentsList studentsList = new StudentsList();
                studentsList.setRollNum(String.valueOf(k));
                studentsList.setSectionName(sections.getSectionName());
                studentsList.setStudentName("Student " + k);
                classes.getSectionsList().get(j).studentsLists.add(studentsList);
            }

        }
        classesArrayList.add(classes);
//        classesArrayList.add(classesArrayList.size() - 1, classes);
        adapter = new MyStatePagerAdapter(getSupportFragmentManager(), classesArrayList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(classesArrayList.size() -1, true);
        buttonStudent.setEnabled(false);
    }

    private void addSection() {
        currentClassPosition = viewPager.getCurrentItem();
        Classes classes = classesArrayList.get(viewPager.getCurrentItem());
        classes.sectionsList = classesArrayList.get(viewPager.getCurrentItem()).sectionsList;
        Sections sections = new Sections();
        sections.setSectionName("Section " + (classes.sectionsList.size() + 1));
        sections.setNoOfStudents(40);
        sections.setClassName(classes.getClassName());
        classes.sectionsList.add(sections);
        for (int k = 0; k < 10; k++) {
            StudentsList studentsList = new StudentsList();
            studentsList.setRollNum(String.valueOf(k));
            studentsList.setSectionName(sections.getSectionName());
            studentsList.setStudentName("Student " + k);
            classes.getSectionsList().get(classes.sectionsList.size() - 1).studentsLists.add(studentsList);
        }
        adapter = new MyStatePagerAdapter(getSupportFragmentManager(), classesArrayList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentClassPosition, true);
        buttonStudent.setEnabled(false);

    }

    private void addStudent() {
        currentClassPosition = viewPager.getCurrentItem();
        Classes classes = classesArrayList.get(viewPager.getCurrentItem());
        classes.sectionsList = classesArrayList.get(viewPager.getCurrentItem()).sectionsList;
        Sections sections = classesArrayList.get(viewPager.getCurrentItem()).sectionsList.get(currentSectionPosition);
        StudentsList studentsList = new StudentsList();
        studentsList.setRollNum(String.valueOf((classes.sectionsList.get(currentSectionPosition).studentsLists.size() + 1)));
        studentsList.setSectionName(sections.getSectionName());
        studentsList.setStudentName("Student " + ((classes.sectionsList.get(currentSectionPosition).studentsLists.size() )));
        classes.getSectionsList().get(currentSectionPosition).studentsLists.add(studentsList);
        adapter = new MyStatePagerAdapter(getSupportFragmentManager(), classesArrayList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentClassPosition, true);
        buttonStudent.setEnabled(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                ((FragmentLoaded) adapter.getFragment(classesArrayList.get(currentClassPosition), currentClassPosition)).keepGroupExpanded(currentSectionPosition);
            }
        }, 500);
    }

    private class MyStatePagerAdapter extends ArrayFragmentStatePagerAdapter<Classes> {
        List<Classes> classesList = new ArrayList<>();

        public MyStatePagerAdapter(FragmentManager fm, ArrayList<Classes> classes) {
            super(fm, classes);
            this.classesList = classes;
        }

        @Override
        public Fragment getFragment(Classes item, int position) {
            return FragmentLoaded.newInstance(item);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return classesList.get(position).getClassName();
        }
    }

    public void setCurrentSectionPosition(int currentSectionPosition) {
        this.currentSectionPosition = currentSectionPosition;
        buttonStudent.setEnabled(true);
    }

}
