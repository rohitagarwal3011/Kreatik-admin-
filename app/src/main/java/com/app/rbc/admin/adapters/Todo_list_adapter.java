package com.app.rbc.admin.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.TaskActivity;
import com.app.rbc.admin.fragments.Task_home;
import com.app.rbc.admin.models.Employee;
import com.app.rbc.admin.models.Todolist;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.TagsPreferences;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by rohit on 3/6/17.
 */

public class Todo_list_adapter extends RecyclerView.Adapter<Todo_list_adapter.MyViewHolder> {


    private List<Todolist.Data> data;
    private Context context;
    private Task_home task_home;
    private Animation shake ;

    public interface OnItemLongClickListener {
        public boolean onItemLongClicked(int position);
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {



        TextView taskTitle;

        TextView toUser;



        TextView deadLine;

        TextView taskStatus;
        ImageView task_type_image;
        ImageView task_delete_image;

        TextView by;


        public MyViewHolder(final View view) {

            super(view);
            taskTitle= (TextView)view.findViewById(R.id.task_title);
            toUser= (TextView)view.findViewById(R.id.to_user);
            deadLine= (TextView)view.findViewById(R.id.dead_line);
            taskStatus= (TextView)view.findViewById(R.id.status);
            task_type_image = (ImageView) view.findViewById(R.id.task_type_icon);
            task_delete_image = (ImageView) view.findViewById(R.id.task_delete_icon);
            by = (TextView) view.findViewById(R.id.by);


//            task_delete_image.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    task_home.delete_task(data.get(getAdapterPosition()).getTask_id(),getAdapterPosition());
//                }
//            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//
//                    TaskActivity taskActivity = new TaskActivity();
//                    taskActivity.show_task_details("TD_100");

//                    if(!Task_home.show_delete)
                    ((TaskActivity)context).show_task_details(data.get(getAdapterPosition()).getTask_id(),data.get(getAdapterPosition()).getTitle());
//
//                    else
//                        task_home.stop_animation();

                }
            });

//            view.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    task_home.onItemLongClicked(getAdapterPosition());
//                    return true;
//                }
//            });
        }




    }


    public Todo_list_adapter(List<Todolist.Data> data ,Context context , Task_home task_home) {
        this.data = data;this.context =context;this.task_home=task_home;
        shake = AnimationUtils.loadAnimation(this.context.getApplicationContext(), R.anim.shake);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_to_do_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

//        if(position==0)
//        {
//            holder.taskTitle.setTextSize(60);
//        }

        holder.by.setText("By : ");
        holder.taskTitle.setText(data.get(position).getTitle());
        final Employee emp = new Gson().fromJson(AppUtil.getString(context, TagsPreferences.EMPLOYEE_LIST), Employee.class);
        for(int i = 0;i<emp.getData().size();i++)
        {
            if(emp.getData().get(i).getUserId().equalsIgnoreCase(data.get(position).getFromUser()))
            {
                holder.toUser.setText(emp.getData().get(i).getUserName());
                break;
            }
        }


        String date_recieved = data.get(position).getDeadline();
        String date = date_recieved.substring(0,date_recieved.indexOf('T'));
        //AppUtil.logger("Date substring: ",date);
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date formated = fmt.parse(date);
            SimpleDateFormat fmtout = new SimpleDateFormat("EEE, MMM dd");
           // AppUtil.logger("Final date : ", fmtout.format(formated));

            holder.deadLine.setText(fmtout.format(formated));

        } catch (ParseException e) {
            e.printStackTrace();
        }



        holder.taskStatus.setText(data.get(position).getStatus());


        char type=data.get(position).getTask_type().charAt(0);


//       if(Task_home.show_delete)
//       {
//           holder.task_delete_image.setVisibility(View.VISIBLE);
//           holder.task_type_image.setVisibility(View.GONE);
//           holder.itemView.startAnimation(shake);
//       }
//       else {
//           holder.itemView.clearAnimation();

           holder.task_delete_image.setVisibility(View.GONE);
           holder.task_type_image.setVisibility(View.VISIBLE);
           switch (type) {
               case 'L':
                   holder.task_type_image.setImageDrawable(context.getResources().getDrawable(R.drawable.mailbox));
                   break;
               case 'M':
                   holder.task_type_image.setImageDrawable(context.getResources().getDrawable(R.drawable.colored_handshake));
                   break;
               case 'D':
                   holder.task_type_image.setImageDrawable(context.getResources().getDrawable(R.drawable.daily));
                   break;
           }
//       }

    }


    



    @Override
    public int getItemCount() {
        return data.size();
    }


}

