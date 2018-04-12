package in.ajm.sb.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import in.ajm.sb.R;
import in.ajm.sb.activities.SelectUserType;
import in.ajm.sb.adapter.SwitchUserAdapter;
import in.ajm.sb.data.User;
import in.ajm.sb.interfaces.OnUserChanged;
import in.ajm.sb.interfaces.OnUserSwitched;


/**
 * Created by dsd on 26/03/18.
 */

public class BottomSheetSwitchUser extends BottomSheetDialogFragment implements View.OnClickListener, OnUserSwitched {

    Button buttonAddUser;
    Button buttonCancel;
    Button buttonOk;

    RecyclerView recyclerView;
    OnUserChanged onUserChanged;
    List<User> userList = new ArrayList<>();
    User user;
    boolean isEdited = false;
    int selectedPosition = 0;

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    public BottomSheetSwitchUser() {

    }

    @SuppressLint("ValidFragment")
    public BottomSheetSwitchUser(OnUserChanged onUserChanged, boolean isEdited, int selectedPosition) {
        this.onUserChanged = onUserChanged;
        this.isEdited = isEdited;
        this.selectedPosition = selectedPosition;
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.switch_user, null);
        viewById(contentView);
        applyClickListeners();
        dialog.setContentView(contentView);
        setDialogBorder(dialog);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
        setRecyclerViewForUser();
    }


    /**
     * Intended for the Rounded Background of the dialog view.
     *
     * @param dialog
     */
    public void setDialogBorder(Dialog dialog) {
        FrameLayout bottomSheet = (FrameLayout) dialog.getWindow().findViewById(android.support.design.R.id.design_bottom_sheet);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            bottomSheet.setBackground(new ColorDrawable(Color.TRANSPARENT));
        }
        setMargins(bottomSheet, 10, 0, 10, 20);
    }

    /**
     * Setting the margins for the ViewGroup Programmatically
     *
     * @param view
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    private void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }


    public void viewById(View contentView) {
        buttonAddUser = contentView.findViewById(R.id.button_add_user);
        buttonCancel = contentView.findViewById(R.id.button_cancel);
        buttonOk = contentView.findViewById(R.id.button_ok);
        recyclerView = contentView.findViewById(R.id.recycler_selected_user);
    }

    public void applyClickListeners() {
        buttonAddUser.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
        buttonOk.setOnClickListener(this);
    }

    public void setRecyclerViewForUser() {
        userList = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            user = new User();
            user.setUserName(getResources().getString(R.string.current_user) + " " + i);
            user.setUserId(i + "");
            if (isEdited) {
                if (i == selectedPosition) {
                    user.setSelected(true);
                } else {
                    user.setSelected(false);
                }
            } else {
                if (i == 0) {
                    user.setSelected(true);
                } else {
                    user.setSelected(false);
                }
            }
            userList.add(user);
        }

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        SwitchUserAdapter switchUserAdapter = new SwitchUserAdapter(getActivity(), userList, this);
        recyclerView.setAdapter(switchUserAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add_user:
                dismiss();
                if (onUserChanged != null) {
                }
                break;
            case R.id.button_cancel:
                dismiss();
                break;
            case R.id.button_ok:
                dismiss();
                if (onUserChanged != null) {
                    onUserChanged.onUserSwitched(selectedPosition,userList.get(selectedPosition).getUserId() ,userList.get(selectedPosition).getUserName());
                }
                break;

            default:
                break;
        }
    }


    @Override
    public void onUserSwitched(int pos, String id) {
        dismiss();
        isEdited = true;
        selectedPosition = pos;
        setRecyclerViewForUser();

    }

    @Override
    public void onUserAddClicked() {
        dismiss();
        openSelectUserType();
    }

    public void openSelectUserType() {
        Intent intent = new Intent(getActivity(), SelectUserType.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}