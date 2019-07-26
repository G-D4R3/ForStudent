package com.example.forstudent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class TodoFragment extends Fragment {
    ArrayList<Assignment> AssList = new ArrayList<Assignment>();
    TodoListAdapter adapter;
    TextView mTitle;
    ListView mlistView;
    String title = "남은 과제 수";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_todo, container, false);

        mTitle = (TextView)view.findViewById(R.id.restDo);
        mlistView = (ListView)view.findViewById(R.id.assignmentList);
        Button mAdd = (Button)view.findViewById(R.id.addAss);

        mTitle.setText(title);

        adapter = new TodoListAdapter(AssList);
        mlistView.setAdapter(adapter);



        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity main = (MainActivity)getActivity();
                main.FragmentAdd(new AddNewAssignment());
                mTitle.setText(title);
            }
        });

        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                String name = adapter.data.get(position).getName();
                String[] menu = {"수정", "삭제"};
                Check();

                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle(name);
                dialog.setItems(menu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                ModifyAss(adapter.data.get(pos));
                                dialog.dismiss();
                                break;
                            case 1:
                                AlertDialog.Builder remove = new AlertDialog.Builder(getContext());
                                remove.setTitle("삭제");
                                remove.setMessage("할 일을 삭제 합니다.");
                                remove.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        RemoveAss(adapter.data.get(pos));
                                        mTitle.setText(title);
                                        dialog.dismiss();
                                    }
                                });
                                remove.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();

                                    }
                                });
                                remove.show();
                                break;

                        }
                    }
                });
                dialog.create();
                dialog.show();

            }
        });







        return view;
    }


    public void AddNewAss(Assignment a){
        AssList.add(a);
        Collections.sort(AssList);
        adapter.notifyDataSetChanged();
        title = String.format("남은 과제 : %d",AssList.size());
    }

    public void RemoveAss(Assignment a){
        AssList.remove(a);
        Collections.sort(AssList);
        if(AssList.size()==0){
            title = "남은 과제가 없습니다.";
        }
        else {
            title = String.format("남은 과제 : %d", AssList.size());
        }

        adapter.notifyDataSetChanged();
    }

    public void ModifyAss(Assignment a){
        Assignment temp=null;

        if(a.getMemo()==null){
            temp = new Assignment(a.getName(),a.getPeriod());
        }
        else{
            temp = new Assignment(a.getName(),a.getPeriod(),a.getMemo());
        }

        RemoveAss(a);
        MainActivity main = (MainActivity)getActivity();
        AddNewAssignment fragment = AddNewAssignment.newInstance();
        fragment.ass = a;
        fragment.MOD=true;
        fragment.Name=a.getName();
        fragment.Date = String.format((a.getPeriod().get(Calendar.MONTH)+1)+"월 "+a.getPeriod().get(Calendar.DAY_OF_MONTH)+"일");
        main.FragmentAdd(fragment);
        adapter.notifyDataSetChanged();
    }

    public void Check(){
        Toast toast = Toast.makeText(getContext(),"할 일 완료", Toast.LENGTH_LONG);
        toast.show();
        for(int i=0; i<AssList.size(); i++){
            if(adapter.viewHolder.Check.isChecked()==true){
                RemoveAss(AssList.get(i));
            }
        }
        Collections.sort(AssList);
        adapter.notifyDataSetChanged();
    }
}