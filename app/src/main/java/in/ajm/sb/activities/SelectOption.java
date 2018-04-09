package in.ajm.sb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.ajm.sb.R;
import in.ajm.sb.adapter.ClassOptionAdapter;
import in.ajm.sb.adapter.SectionOptionAdapter;
import in.ajm.sb.data.ClassOptions;
import in.ajm.sb.data.SectionOptions;
import in.ajm.sb.helper.AppConfigs;
import in.ajm.sb.interfaces.OnClassItemClick;
import in.ajm.sb.interfaces.OnSectionItemClick;

public class SelectOption extends BaseActivity implements OnClassItemClick, OnSectionItemClick {
    RecyclerView recyclerViewoptions;
    List<ClassOptions> classOptionsList = new ArrayList<>();
    ClassOptionAdapter classOptionAdapter;
    int optionType;

    List<SectionOptions> sectionOptionsList = new ArrayList<>();
    SectionOptionAdapter sectionOptionAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkForTheme(this);
        setContentView(R.layout.activity_select_option);
        getIntentValues();
        viewByIds();
        if(optionType == AppConfigs.REQUEST_CODE_SELECT_CLASS){
            setRecyclerViewoptions();
            setupToolBar(getResources().getString(R.string.select_class), true);
        }else{
            setRecyclerViewoptionsForSections();
            setupToolBar(getResources().getString(R.string.select_section), true);
        }


    }

    public void getIntentValues() {
        optionType = getIntent().getExtras().getInt("option_type", 101);
    }

    public void setRecyclerViewoptions() {
        recyclerViewoptions.setHasFixedSize(true);
        ClassOptions classOptions = new ClassOptions();
        for (int i = 0; i < 6; i++) {
            classOptionsList.add(classOptions);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewoptions.setLayoutManager(linearLayoutManager);
        classOptionAdapter = new ClassOptionAdapter(this, classOptionsList, this);
        recyclerViewoptions.setAdapter(classOptionAdapter);
    }

    public void setRecyclerViewoptionsForSections() {
        recyclerViewoptions.setHasFixedSize(true);
        SectionOptions sectionOptions = new SectionOptions();
        for (int i = 0; i < 6; i++) {
            sectionOptionsList.add(sectionOptions);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewoptions.setLayoutManager(linearLayoutManager);
        sectionOptionAdapter = new SectionOptionAdapter(this, sectionOptionsList, this);
        recyclerViewoptions.setAdapter(sectionOptionAdapter);
    }

    public void viewByIds() {
        recyclerViewoptions = (RecyclerView) findViewById(R.id.recycler_view);
    }

    @Override
    public void onClassItemClicked() {
        Intent intent = new Intent(this, SelectOption.class);
        setResult(RESULT_OK, intent);
        intent.putExtra("option_type", AppConfigs.REQUEST_CODE_SELECT_SECTION);
        startActivity(intent);
    }

    @Override
    public void onSectionItemClicked() {
        Intent intent = new Intent(SelectOption.this, HomeTestActivity.class);
        startActivity(intent);
    }
}
