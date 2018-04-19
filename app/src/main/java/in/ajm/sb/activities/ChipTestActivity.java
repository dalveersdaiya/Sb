package in.ajm.sb.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import java.util.List;

import in.ajm.sb.R;
import in.ajm.sb.adapter.ContactOnChipAdapter;
import in.ajm.sb_library.chips.ChipsInputLayout;
import in.ajm.sb_library.chips.ContactChip;
import in.ajm.sb_library.chips.ContactLoadingActivity;
import in.ajm.sb_library.particle.ParticlesDrawable;

public class ChipTestActivity extends ContactLoadingActivity implements ContactOnChipAdapter.OnContactClickListener {

    LinearLayout linearLayoutRoot;
    ParticlesDrawable mDrawable;
    RecyclerView recycler;
    private ContactOnChipAdapter contactAdapter;
    private ChipsInputLayout chipsInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chip_test);
        viewById();
        loadContactsWithRuntimePermission();
        setParticle();
        setRecycler();
    }

    public void setRecycler() {
        this.contactAdapter = new ContactOnChipAdapter(this);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(contactAdapter);
    }

    public void viewById() {
        this.chipsInput = (ChipsInputLayout) findViewById(R.id.chips_input);
        linearLayoutRoot = (LinearLayout) findViewById(R.id.root);
    }

    @Override
    public void onContactClicked(ContactChip chip) {
    }

    public void setParticle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mDrawable = (ParticlesDrawable) getDrawable(R.drawable.particles_customized);
        } else {
            mDrawable = new ParticlesDrawable();
        }
        linearLayoutRoot.setBackground(mDrawable);
    }

    @Override
    public void onContactsAvailable(List<ContactChip> chips) {
        System.out.println("Number of contacts: " + chips.size());
        this.chipsInput.setFilterableChipList(chips);
    }

    @Override
    public void onContactsReset() {

    }
}
