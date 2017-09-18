package com.app.rbc.admin.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.TaskActivity;
import com.app.rbc.admin.fragments.Task_home;
import com.app.rbc.admin.models.Employee;
import com.app.rbc.admin.models.Todolist;
import com.app.rbc.admin.services.DeadlineNotificationService;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.TagsPreferences;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by rohit on 3/6/17.
 */

public class Tasks_assigned_adapter extends RecyclerView.Adapter<Tasks_assigned_adapter.MyViewHolder> {


    private List<Todolist.Data1> data;
    private Context context;
    private Task_home task_home;
    private Animation shake ;


    private DeadlineNotificationService alarm;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public SimpleDraweeView profile_pic;
        LinearLayout unread_msgs;
        TextView taskTitle;

        TextView toUser;



        TextView deadLine;

        TextView taskStatus;

        ImageView task_type_image;

        TextView by;
        ImageView task_delete_image;

        TextView unread_count;

        public MyViewHolder(View view) {

            super(view);
            profile_pic=(SimpleDraweeView) view.findViewById(R.id.profile_pic);
            taskTitle= (TextView)view.findViewById(R.id.task_title);
            toUser= (TextView)view.findViewById(R.id.to_user);
            deadLine= (TextView)view.findViewById(R.id.dead_line);
            taskStatus= (TextView)view.findViewById(R.id.status);
            task_type_image = (ImageView) view.findViewById(R.id.task_type_icon);
            task_delete_image=(ImageView) view.findViewById(R.id.task_delete_icon);
            by = (TextView) view.findViewById(R.id.by);
            unread_count = (TextView) view.findViewById(R.id.unread_count);
            unread_msgs = (LinearLayout) view.findViewById(R.id.unread_msgs);

//            for(int i = 0;i<data.size();i++)
//            {
//                if(!data.get(i).getStatus().equalsIgnoreCase("Complete"))
//                {
//                    String deadline = data.get(i).getDeadline().replace('T',' ');
//                    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
//                    try {
//                        Date formated = fmt.parse(deadline);
//                        alarm.setOnetimeTimer(context,formated,data.get(i).getTask_id(),data.get(i).getTitle(),"task_update");
//
//
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }

            task_delete_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    task_home.delete_task(data.get(getAdapterPosition()).getTask_id(),getAdapterPosition());
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//
                   if(!Task_home.show_delete)
                    ((TaskActivity)context).show_task_details(data.get(getAdapterPosition()).getTask_id(),data.get(getAdapterPosition()).getTitle());

                    else
                        task_home.stop_animation();
                }
            });



            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    task_home.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });

        }
    }


    public Tasks_assigned_adapter(List<Todolist.Data1> data ,Context context, Task_home task_home) {
        this.data = data;this.context =context;this.task_home=task_home;
        shake = AnimationUtils.loadAnimation(this.context.getApplicationContext(), R.anim.shake);
        alarm= new DeadlineNotificationService();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_to_do_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

//        if(data.get(position).getStatus().equalsIgnoreCase("Complete"))
//            removeAt(position);
        holder.by.setText("To : ");
        Typeface face= Typeface.createFromAsset(context.getAssets(),"fonts/roboto.regular.ttf");
        holder.taskTitle.setTypeface(face);
        holder.taskTitle.setText(data.get(position).getTitle());

        if(data.get(position).getUnread_count()>0 && !data.get(position).getStatus().equalsIgnoreCase("Complete"))
        {
            holder.unread_count.setText(data.get(position).getUnread_count().toString());
            holder.unread_count.setVisibility(View.VISIBLE);
            holder.unread_msgs.setVisibility(View.VISIBLE);
        }
        else {
            holder.unread_count.setVisibility(View.INVISIBLE);
            holder.unread_msgs.setVisibility(View.INVISIBLE);
        }
        int color = context.getResources().getColor(R.color.black_overlay);
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(7f);
        roundingParams.setBorder(color, 1.0f);
        roundingParams.setRoundAsCircle(true);
        holder.profile_pic.getHierarchy().setRoundingParams(roundingParams);



        final Employee emp = new Gson().fromJson(AppUtil.getString(context, TagsPreferences.EMPLOYEE_LIST), Employee.class);
        for(int i = 0;i<emp.getData().size();i++)
        {
            if(emp.getData().get(i).getUserId().equalsIgnoreCase(data.get(position).getToUser()))
            {
                holder.toUser.setText(emp.getData().get(i).getUserName());
                holder.profile_pic.setImageURI(Uri.parse(emp.getData().get(i).getMpic_url()));

                break;
            }
        }


        String date_recieved = data.get(position).getDeadline();
        String date = date_recieved.substring(0,date_recieved.indexOf('T'));
        AppUtil.logger("Date substring: ",date);
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date formated = fmt.parse(date);
            SimpleDateFormat fmtout = new SimpleDateFormat("EEE, MMM dd");
            AppUtil.logger("Final date : ", fmtout.format(formated));

            holder.deadLine.setText(fmtout.format(formated));

        } catch (ParseException e) {
            e.printStackTrace();
        }



        holder.taskStatus.setText(data.get(position).getStatus());

        char type=data.get(position).getTask_type().charAt(0);

        if(Task_home.show_delete)
        {
            holder.task_delete_image.setVisibility(View.VISIBLE);
            holder.task_type_image.setVisibility(View.GONE);
            holder.itemView.startAnimation(shake);
        }
        else {
            holder.itemView.clearAnimation();



           // holder.profile_pic.setImageURI(Uri.parse("http://plethron.pythonanywhere.com/media/profile.jpg"));
            holder.task_delete_image.setVisibility(View.GONE);
          //  holder.task_type_image.setVisibility(View.VISIBLE);
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
        }

    }






    @Override
    public int getItemCount() {
        return data.size();
    }

    public void removeAt(int position) {
        this.data.remove(position);
        //notifyDataSetChanged();
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, data.size());
    }
}

