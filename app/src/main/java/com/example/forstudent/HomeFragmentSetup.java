package com.example.forstudent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragmentSetup extends Fragment {

    MainActivity main;

    public boolean[] select = {true, true, true, true}; //리스트뷰 표시
    int assignmentViewCheck; //과제 표시

    /***** instanciate *****/
    public static HomeFragmentSetup newInstance(){
        return new HomeFragmentSetup();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = (MainActivity)getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        /***** view load *****/
        View view = (View)inflater.inflate(R.layout.home_setup, container, false);
        TextView mLayout = (TextView)view.findViewById(R.id.layoutset);
        RadioGroup mAssignmentView = (RadioGroup)view.findViewById(R.id.AssignmentView);

        select[0]=main.getUser().homeScheduleCheck;
        select[1]=main.getUser().homeClassCheck;
        select[2]=main.getUser().homeAssignmentCheck;
        select[3]=main.getUser().homeExamCheck;

        assignmentViewCheck = main.getUser().getHomeAssignmentViewCheck();
        mAssignmentView.check(assignmentViewCheck);


        /***** toolbar *****/
        main.BACK_STACK=true;
        main.toolbar.setTitle("");
        main.centerToolbarTitle.setText("설정");
        main.invalidateOptionsMenu();

        main.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.FragmentRemove(HomeFragmentSetup.this);
            }
        });



        mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] menu = {"일정","수업","과제","시험"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("화면에 표시할 메뉴");
                dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        main.getUser().setHomeScheduleCheck(select[0]);
                        main.getUser().setHomeClassCheck(select[1]);
                        main.getUser().setHomeAssignmentCheck(select[2]);
                        main.getUser().setHomeExamCheck(select[3]);
                        main.getUserDataBox().removeAll();
                        main.getUserDataBox().put(main.getUser());
                        dialog.dismiss();
                        Toast.makeText(getContext(),"설정이 적용됩니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.setMultiChoiceItems(menu, select, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if(isChecked==true){
                            select[which]=true;
                        }
                        else if(isChecked==false){
                            select[which]=false;
                        }
                    }
                });
                dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        select[0]=main.getUser().homeScheduleCheck;
                        select[1]=main.getUser().homeClassCheck;
                        select[2]=main.getUser().homeAssignmentCheck;
                        select[3]=main.getUser().homeExamCheck;
                        dialog.dismiss();
                        Toast.makeText(getContext(),"설정이 적용되지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.create();
                dialog.show();
            }
        });


        mAssignmentView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.ImpOnly:
                        main.homeFragment.mSetAssignment = false;
                        break;
                    case R.id.viewAllAssign:
                        main.homeFragment.mSetAssignment = true;
                        break;
                }
            }
        });




        return view;

    }
    @Override
    public void onResume() {
        super.onResume();

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_add_assignment,menu);
    }
}
