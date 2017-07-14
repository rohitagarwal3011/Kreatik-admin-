package com.app.rbc.admin.adapters;

/**
 * Created by rohit on 14/6/17.
 */

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.models.Employee;
import com.app.rbc.admin.utils.AdapterWithCustomItem;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.MySpinner;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

public class Attendance_mark_adapter extends RecyclerView.Adapter<Attendance_mark_adapter.MyViewHolder> {




    private List<Employee.Data> data;
    private Context context;
    private String fragment;
    public static HashMap<String, String> attendance_grid = new HashMap<>();

    public static  HashMap<String , HashMap<String, String>> remarks_grid = new HashMap<>();
    final String[] remarks = {"Most Common","Medical Emergency", "Not well","Family Issue", "Other"};

    ArrayAdapter<String> remarks_adapter;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView profile_pic;
        public TextView employee_name;
        public CheckBox absent;
        public CheckBox half_day;
        public Spinner remarksDropDown;
        public LinearLayout remarksLayout;
        public EditText remarksOther;
        public MyCustomEditTextListener myCustomEditTextListener;

        public MyViewHolder(final View view,MyCustomEditTextListener myCustomEditTextListener) {
            super(view);
            profile_pic = (SimpleDraweeView) view.findViewById(R.id.profile_pic);
            employee_name = (TextView) view.findViewById(R.id.employee_name);
            absent = (CheckBox) view.findViewById(R.id.absent);
            half_day = (CheckBox) view.findViewById(R.id.half_day);
            remarksDropDown= (Spinner)view.findViewById(R.id.remarks_drop_down);
            remarksLayout=(LinearLayout)view.findViewById(R.id.remarks_layout);
            remarksOther=(EditText)view.findViewById(R.id.remarks_other);

            this.myCustomEditTextListener = myCustomEditTextListener;
            this.remarksOther.addTextChangedListener(myCustomEditTextListener);



            remarks_adapter =  new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, remarks);
            remarks_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            remarksDropDown.setAdapter(remarks_adapter);


            remarksDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    AppUtil.logger("Drop DOwn : ",remarks[position]+" is selected");

                        if (position != (remarks.length - 1)) {
                            AppUtil.logger("From Drop DOwn :","Selected");
                            remarksOther.setVisibility(View.GONE);
                            remarksOther.setText("");

                            if(remarks_grid.containsKey(data.get(getAdapterPosition()).getUserId()))
                            {
                                remarks_grid.remove(data.get(getAdapterPosition()).getUserId());
                            }
                            remarks_grid.put(data.get(getAdapterPosition()).getUserId(),new HashMap<String, String>());
                            remarks_grid.get(data.get(getAdapterPosition()).getUserId()).put("Selected",remarks[position]);
                        }
                        else {

                            AppUtil.logger("From Drop DOwn :","Other");
                            remarksOther.setVisibility(View.VISIBLE);
                            if(remarks_grid.containsKey(data.get(getAdapterPosition()).getUserId()))
                            {
                                if(remarks_grid.get(data.get(getAdapterPosition()).getUserId()).containsKey("Other"))
                                {
//                                    remarks_grid.put(data.get(getAdapterPosition()).getUserId(),new HashMap<String, String>());
//                                    remarks_grid.get(data.get(getAdapterPosition()).getUserId()).put("Other",remarks_grid.get(data.get(position).getUserId()).get("Other"));
                                }
                                else
                                {
                                    remarks_grid.put(data.get(getAdapterPosition()).getUserId(),new HashMap<String, String>());
                                    remarks_grid.get(data.get(getAdapterPosition()).getUserId()).put("Other","");

                                }

                            }else
                            {
                                remarks_grid.put(data.get(getAdapterPosition()).getUserId(),new HashMap<String, String>());
                                remarks_grid.get(data.get(getAdapterPosition()).getUserId()).put("Other","");

                            }


                        }


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            absent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (half_day.isChecked()) {
                        half_day.setChecked(false);
                    }
                    if (attendance_grid.containsKey(data.get(getAdapterPosition()).getUserId())) {
                        attendance_grid.remove(data.get(getAdapterPosition()).getUserId());
                        remarks_grid.remove(data.get(getAdapterPosition()).getUserId());

                    }
                    if (absent.isChecked()) {
                        attendance_grid.put(data.get(getAdapterPosition()).getUserId(), "Absent");
                        remarks_grid.put(data.get(getAdapterPosition()).getUserId(),new HashMap<String, String>());
                        remarks_grid.get(data.get(getAdapterPosition()).getUserId()).put("Selected",remarks[0]);
                        remarksLayout.setVisibility(View.VISIBLE);
                       remarksOther.setVisibility(View.GONE);
                    }
                    else {
                        remarksLayout.setVisibility(View.GONE);
                        remarksOther.setVisibility(View.GONE);
                    }


                }
            });
            half_day.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (absent.isChecked()) {
                        absent.setChecked(false);
                    }
                    if (attendance_grid.containsKey(data.get(getAdapterPosition()).getUserId())) {
                        attendance_grid.remove(data.get(getAdapterPosition()).getUserId());
                        remarks_grid.remove(data.get(getAdapterPosition()).getUserId());


                    }
                    if (half_day.isChecked()) {
                        attendance_grid.put(data.get(getAdapterPosition()).getUserId(), "Half day");
                        remarks_grid.put(data.get(getAdapterPosition()).getUserId(),new HashMap<String, String>());
                        remarks_grid.get(data.get(getAdapterPosition()).getUserId()).put("Selected",remarks[0]);
                        remarksLayout.setVisibility(View.VISIBLE);
                        remarksOther.setVisibility(View.GONE);
                    }
                    else {
                        remarksLayout.setVisibility(View.GONE);
                        remarksOther.setVisibility(View.GONE);
                    }
                }
            });




//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    task_create.set_employee_id(data.get(getAdapterPosition()).getUserId());
//
//                }
//            });

        }
    }


    public Attendance_mark_adapter(List<Employee.Data> data, Context context, String fragment) {
        this.data = data;
        this.context = context;
        this.fragment = fragment;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attendance_mark_list, parent, false);
        return new MyViewHolder(itemView,new MyCustomEditTextListener());
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

       // holder.remarksOther.setTag(position);
        AppUtil.logger("Position : ", String.valueOf(position));

        AppUtil.logger("Adapter Position : ", String.valueOf(holder.getAdapterPosition()));
        holder.myCustomEditTextListener.updatePosition(position);
        AppUtil.logger("Attendance Grid : ", attendance_grid.toString());
        if (attendance_grid.containsKey(data.get(holder.getAdapterPosition()).getUserId())) {

            if(remarks_grid.get(data.get(holder.getAdapterPosition()).getUserId()).containsKey("Selected"))
            {
                holder.remarksOther.setVisibility(View.GONE);
                holder.remarksDropDown.setVisibility(View.VISIBLE);
                for (int i=0;i<remarks.length;i++)
                {
                    if(remarks_grid.get(data.get(holder.getAdapterPosition()).getUserId()).get("Selected").equalsIgnoreCase(remarks[i]))
                    {
                        holder.remarksDropDown.setSelection(i);
                        break;
                    }
                }

            }
            else
            {

                holder.remarksOther.setVisibility(View.VISIBLE);
                holder.remarksDropDown.setVisibility(View.VISIBLE);
                holder.remarksDropDown.setSelection(remarks.length-1);
                holder.remarksOther.setText(remarks_grid.get(data.get(position).getUserId()).get("Other"));
                AppUtil.logger("User id : "+data.get(position).getUserName()," Remarks : "+holder.remarksOther.getText().toString() );
//                remarks_grid.remove(data.get(holder.getAdapterPosition()).getUserId());
//                remarks_grid.put(data.get(holder.getAdapterPosition()).getUserId(), new HashMap<String, String>());
//                remarks_grid.get(data.get(holder.getAdapterPosition()).getUserId()).put("Other", holder.remarksOther.getText().toString());


        }

            if (attendance_grid.get(data.get(holder.getAdapterPosition()).getUserId()).equalsIgnoreCase("Absent")) {
                holder.absent.setChecked(true);
                holder.half_day.setChecked(false);
                holder.remarksLayout.setVisibility(View.VISIBLE);
            } else {
                holder.absent.setChecked(false);
                holder.half_day.setChecked(true);
                holder.remarksLayout.setVisibility(View.VISIBLE);

            }
        }
        else {
            holder.absent.setChecked(false);
            holder.half_day.setChecked(false);
            holder.remarksLayout.setVisibility(View.GONE);
            holder.remarksOther.setVisibility(View.GONE);
        }
        holder.employee_name.setText(data.get(position).getUserName());

        int color = context.getResources().getColor(R.color.black_overlay);
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
        roundingParams.setBorder(color, 1.0f);
        roundingParams.setRoundAsCircle(true);
        holder.profile_pic.getHierarchy().setRoundingParams(roundingParams);


        holder.profile_pic.setImageURI(Uri.parse(data.get(position).getMpic_url()));



//        TextWatcher watcher= new TextWatcher() {
//            public void afterTextChanged(Editable s) {
//                if (holder.remarksOther.getText().toString().equals("")) {
//
//
//                } else {
//
//                    if (remarks_grid.containsKey(data.get(holder.getAdapterPosition()).getUserId())) {
//                        remarks_grid.remove(data.get(holder.getAdapterPosition()).getUserId());
//                    }
//
//                    remarks_grid.put(data.get(holder.getAdapterPosition()).getUserId(), new HashMap<String, String>());
//                    remarks_grid.get(data.get(holder.getAdapterPosition()).getUserId()).put("Other", holder.remarksOther.getText().toString());
//                }
//            }
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                //Do something or nothing.
//            }
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
////                    EditText abc = (EditText)remarksOther.getTag();
////                    if((int)remarksOther.getTag()==getAdapterPosition()) {
//
////                    }
//
//                //Do something or nothing
//            }
//        };
//
//        holder.remarksOther.addTextChangedListener(watcher);
        AppUtil.logger("Remarks : " , remarks_grid.toString());
        //Picasso.with(context).load(data.get(position).getMpic_url()).into(holder.profile_pic);


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    // we make TextWatcher to be aware of the position it currently works with
    // this way, once a new item is attached in onBindViewHolder, it will
    // update current position MyCustomEditTextListener, reference to which is kept by ViewHolder
    private class MyCustomEditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            AppUtil.logger("CharSequence : ",charSequence.toString());
            if(charSequence.toString().length()>0) {
                if (remarks_grid.containsKey(data.get(position).getUserId())) {
                    remarks_grid.remove(data.get(position).getUserId());
                }

                remarks_grid.put(data.get(position).getUserId(), new HashMap<String, String>());
                remarks_grid.get(data.get(position).getUserId()).put("Other", charSequence.toString());
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }
}
