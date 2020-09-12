package com.example.appchat;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchat.Adapter.UserJoinGroupAdapter;
import com.example.appchat.Model.User;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserJoinGroupBottomSheetDialog  extends BottomSheetDialogFragment {

    private BottomSheetBehavior mBehavior;
    private RecyclerView rvListUser;
    private TextView tv_huy_create_group;
    private Button btn_Join_Group;
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseDatabase fDatabase;
    private DatabaseReference dRef;

    private UserJoinGroupAdapter userJoinGroupAdapter;
    private List<User> arrListUser;
    private String idGroup;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View view = View.inflate(getContext(),R.layout.layout_bottom_sheet_user_join_group, null);

        LinearLayout linearLayout = view.findViewById(R.id.root);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        params.height =getFullScreen();
        linearLayout.setLayoutParams(params);
        bottomSheetDialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from( (View) view.getParent());
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        MapObject(view);


        // Firebase
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        fDatabase = FirebaseDatabase.getInstance();

        arrListUser = new ArrayList<>();
        rvListUser = view.findViewById(R.id.RecyclerView_List_User_Add_Group);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        rvListUser.setLayoutManager(layoutManager);


        LoadUserJoinGroup();

        EventControl();

        return bottomSheetDialog;
    }

    private void EventControl(){
        tv_huy_create_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        btn_Join_Group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddUserJoinGroup();
                mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
    }

    private void MapObject(View v){
        btn_Join_Group = v.findViewById(R.id.Button_Join_Group);
        tv_huy_create_group = v.findViewById(R.id.textView_Huy);
    }

    public static int getFullScreen(){
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    private void LoadUserJoinGroup(){

        dRef = fDatabase.getReference("Users");
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User u = snapshot.getValue(User.class);
                    if(! u.getId().equals(fUser.getUid())){
                        arrListUser.add(u);
                    }
                }
                // userAddGroupAdapter.notifyDataSetChanged();
                userJoinGroupAdapter = new UserJoinGroupAdapter(getContext(), arrListUser);
                rvListUser.setAdapter(userJoinGroupAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void AddUserJoinGroup(){

        if(userJoinGroupAdapter.getArrUserJoinGroup().size() > 0){
            for(String id : userJoinGroupAdapter.getArrUserJoinGroup()){
                DatabaseReference dRefJoinUser = fDatabase.getReference("Users").child(id).child("mGroup");
                HashMap<String, Object> IdGroup = new HashMap<>();
                IdGroup.put("idGroup", idGroup);
                dRefJoinUser.push().setValue(IdGroup);

            }
        }
    }

    public void getIdGroup(String idgroup){
        this.idGroup = idgroup;
    }
}
