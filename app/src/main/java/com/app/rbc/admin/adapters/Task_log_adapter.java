package com.app.rbc.admin.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.activities.TaskActivity;
import com.app.rbc.admin.fragments.Task_details;
import com.app.rbc.admin.models.Employee;
import com.app.rbc.admin.models.Tasklogs;
import com.app.rbc.admin.utils.AppUtil;
import com.app.rbc.admin.utils.TagsPreferences;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.http.Field;


/**
 * Created by rohit on 5/6/17.
 */

public class Task_log_adapter extends RecyclerView.Adapter<Task_log_adapter.MyViewHolder> {



    private List<Tasklogs.Log> data;
    private Employee employee_list;
    private String pic_url;
    private Tasklogs.Log log;
    private Tasklogs tasklogs;
    private Context context;
    private Boolean first_time = true;
    private Task_details task_details;
    ArrayList posters= new ArrayList(1);
    private Bitmap bitmap;


    public class MyViewHolder extends RecyclerView.ViewHolder {


        SimpleDraweeView profilePic;
        TextView leftMessage;
        LinearLayout leftMessageLayout;
        TextView rightMessage;
        LinearLayout rightMessageLayout;
        TextView status;
        LinearLayout statusLayout;
        TextView log_time_left;
        TextView log_time_right;
        TextView left_attachment;
        TextView right_attachment;
        ImageView left_attachment_icon;
        ImageView right_attachment_icon;

        SimpleDraweeView left_attachment_image;
        SimpleDraweeView right_attachment_image;

        ImageView download_right;
        ImageView download_left;

        public MyViewHolder(View view) {

            super(view);
            profilePic = (SimpleDraweeView) view.findViewById(R.id.profile_pic);
            leftMessage = (TextView) view.findViewById(R.id.left_message);
            leftMessageLayout = (LinearLayout) view.findViewById(R.id.left_message_layout);
            rightMessage = (TextView) view.findViewById(R.id.right_message);
            rightMessageLayout = (LinearLayout) view.findViewById(R.id.right_message_layout);
            status = (TextView) view.findViewById(R.id.status);
            statusLayout = (LinearLayout) view.findViewById(R.id.status_layout);
            log_time_left = (TextView) view.findViewById(R.id.log_time_left);
            log_time_right = (TextView) view.findViewById(R.id.log_time_right);
            left_attachment = (TextView) view.findViewById(R.id.left_attachment);
            right_attachment = (TextView) view.findViewById(R.id.right_attachment);
            left_attachment_icon = (ImageView) view.findViewById(R.id.left_attachment_icon);
            right_attachment_icon = (ImageView) view.findViewById(R.id.right_attachment_icon);

            left_attachment_image = (SimpleDraweeView) view.findViewById(R.id.left_attachment_image);
            right_attachment_image = (SimpleDraweeView) view.findViewById(R.id.right_attachment_image);
            download_left = (ImageView) view.findViewById(R.id.download_left);
            download_right = (ImageView) view.findViewById(R.id.download_right);
            profilePic.setTag(this);

            left_attachment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppUtil.logger("Attachment Url ", data.get(getAdapterPosition()).getDocs());
                    String pdf_url = data.get(getAdapterPosition()).getDocs();
                    String pdf_name = pdf_url.substring(pdf_url.lastIndexOf('/'));
                    ((TaskActivity) context).download(pdf_url, pdf_name);

                }
            });

            right_attachment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppUtil.logger("Attachment Url ", data.get(getAdapterPosition()).getDocs());
                    String pdf_url = data.get(getAdapterPosition()).getDocs();
                    String pdf_name = pdf_url.substring(pdf_url.lastIndexOf('/') + 1);
//                    String extension = pdf_url.substring(pdf_url.lastIndexOf('.')+1);
//                    AppUtil.logger("Extension :", extension);
//                    if(extension.equalsIgnoreCase("pdf"))
                    ((TaskActivity) context).download(pdf_url, pdf_name);
//                    else{
                }


//                }
            });


            right_attachment_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  AppUtil.showToast(context,"Image");
                    posters.clear();

                    posters.add(Uri.parse(data.get(getAdapterPosition()).getDocs()));

                    new ImageViewer.Builder<>(context, posters)
                            .setStartPosition(0)
                            .allowSwipeToDismiss(true)
                            .show();


//                    Intent intent = new Intent(context, com.app.rbc.inizio.activities.ImageView.class);
//                    intent.putExtra("uri",data.get(getAdapterPosition()).getDocs());
//                    ActivityOptions options = ActivityOptions.makeScaleUpAnimation(v, 0,
//                            0, v.getWidth(), v.getHeight());
//                    context.startActivity(intent, options.toBundle());
                }
            });

            left_attachment_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    posters.clear();

                    posters.add(Uri.parse(data.get(getAdapterPosition()).getDocs()));

                    new ImageViewer.Builder<>(context, posters)
                            .setStartPosition(0)
                            .allowSwipeToDismiss(true)
                            .show();
                }
            });

            download_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ImageRequest imageRequest = ImageRequest.fromUri(Uri.parse(data.get(getAdapterPosition()).getDocs()));

                    ImagePipeline imagePipeline = Fresco.getImagePipeline();
                    final DataSource<CloseableReference<CloseableImage>> dataSource =
                            imagePipeline.fetchDecodedImage(imageRequest, context);
                    dataSource.subscribe(new BaseBitmapDataSubscriber() {

                        @Override
                        public void onNewResultImpl(@Nullable Bitmap bitmap) {
                            if (dataSource.isFinished() && bitmap != null) {
                                AppUtil.logger("Bitmap", "has come");
                                bitmap = Bitmap.createBitmap(bitmap);

                                dataSource.close();

                                if(save_image(bitmap,data.get(getAdapterPosition()).getChangedBy(),false,getAdapterPosition()))
                                {
                                    ((TaskActivity)context).runOnUiThread(new Runnable() {
                                        public void run() {
                                            // Toast.makeText(((TaskActivity)context), "Hello", Toast.LENGTH_SHORT).show();
                                            AppUtil.showToast(context,"Saved Successfully");
                                            download_left.setVisibility(View.GONE);
                                        }
                                    });
                                }
//                                AppUtil.showToast(context,"Download Incomplete.Please try again");

                            }
                        }

                        @Override
                        public void onFailureImpl(DataSource dataSource) {
                            if (dataSource != null) {
                                dataSource.close();
                            }
                        }
                    }, CallerThreadExecutor.getInstance());

//                    if(save_image(bitmap,data.get(getAdapterPosition()).getChangedBy(),false))
//                    {
//                        AppUtil.showToast(context,"Saved Successfully");
//                        download_left.setVisibility(View.GONE);
//                    }
//                    else {
//                        AppUtil.showToast(context,"Download Incomplete.Please try again");
//                    }

                }
            });
        }






    }


    public Task_log_adapter(List<Tasklogs.Log> data, Context context ,String pic_url,Task_details task_details) {
        this.data = data;
        this.tasklogs = tasklogs;
        this.context = context;
        this.pic_url = pic_url;
        this.task_details = task_details;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_log_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
//
//        String task_desc= tasklogs.getData().getTaskDesc();
//        String task_title = tasklogs.getData().getTitle();

//        if(first_time)
//        {
//            holder.profilePic.setVisibility(View.VISIBLE);
//            holder.leftMessageLayout.setVisibility(View.VISIBLE);
//            holder.leftMessage.setText(task_desc);
//            first_time=false;
//        }
//
        log = data.get(position);
        String deadline = log.getChangeTime().replace('T',' ');
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date formated = fmt.parse(deadline);
            SimpleDateFormat fmtout = new SimpleDateFormat(" h:mm a ");

            deadline = fmtout.format(formated);


            //AppUtil.logger("Ta : ", fmtout.format(formated));


//                    String dayOfTheWeek = (String) DateFormat.format("EE", formated); // Thursday
//                    String day          = (String) DateFormat.format("dd",   formated); // 20
//                    String monthString  = (String) DateFormat.format("MMM",  formated); // Jun
//                    String monthNumber  = (String) DateFormat.format("MM",   formated); // 06
//                    String year         = (String) DateFormat.format("yyyy", formated); // 2013

           // deadline = "Deadline : " + fmtout.format(formated);
        }
        catch (Exception e)
        {
            AppUtil.logger("Task log",e.toString());
        }

        if(log.getmLogtype().equalsIgnoreCase("Comment")||log.getmLogtype().equalsIgnoreCase("Attachment")) {

            int color = context.getResources().getColor(R.color.black_overlay);
            RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
            roundingParams.setBorder(color, 1.0f);
            roundingParams.setRoundAsCircle(true);
            holder.profilePic.getHierarchy().setRoundingParams(roundingParams);
            holder.profilePic.setImageURI(pic_url);
           // Picasso.with(context).load(pic_url).into(holder.profilePic);

            if (log.getChangedBy().equalsIgnoreCase(AppUtil.getString(context, TagsPreferences.USER_ID))) {
                holder.profilePic.setVisibility(View.INVISIBLE);
//                holder.profilePic.requestLayout();
//                holder.profilePic.getLayoutParams().height = 0;
                holder.leftMessageLayout.setVisibility(View.GONE);
                if(log.getmLogtype().equalsIgnoreCase("Attachment"))
                {
                    if(log.getDocs().substring(log.getDocs().lastIndexOf('.')+1).equalsIgnoreCase("pdf")) {
                        holder.right_attachment.setVisibility(View.VISIBLE);
                        holder.rightMessage.setVisibility(View.GONE);
                        holder.right_attachment_icon.setVisibility(View.VISIBLE);
                        holder.right_attachment_image.setVisibility(View.GONE);
                    }
                    else
                    {
                        holder.right_attachment.setVisibility(View.GONE);
                        holder.rightMessage.setVisibility(View.GONE);
                        holder.right_attachment_icon.setVisibility(View.GONE);
                        holder.right_attachment_image.setVisibility(View.VISIBLE);
                        holder.right_attachment_image.setImageURI(Uri.parse(log.getDocs()));
//                        PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
//                        controller.setUri(Uri.parse(log.getDocs()));
//                        controller.setOldController(holder.right_attachment_image.getController());
//                        controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
//                            @Override
//                            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
//                                super.onFinalImageSet(id, imageInfo, animatable);
//                                if (imageInfo == null || holder.right_attachment_image == null) {
//                                    return;
//                                }
//                                holder.right_attachment_image.update(imageInfo.getWidth(), imageInfo.getHeight());
//                            }
//                        });
//
//                        holder.right_attachment_image.setController(controller.build());

                        //Picasso.with(context).load(log.getDocs()).into(holder.right_attachment_image);

                    }
                }
                else {
                    holder.rightMessage.setText(log.getComment());
                    holder.rightMessage.setVisibility(View.VISIBLE);
                    holder.right_attachment_icon.setVisibility(View.GONE);
                    holder.right_attachment.setVisibility(View.GONE);
                    holder.right_attachment_image.setVisibility(View.GONE);
                }
                holder.rightMessageLayout.setVisibility(View.VISIBLE);

                holder.statusLayout.setVisibility(View.GONE);
                holder.log_time_right.setText(deadline);
            } else {
                if (position >= 2 && !data.get(position-1).getmLogtype().equalsIgnoreCase("Status change")) {


                    if (!data.get(position - 1).getChangedBy().equalsIgnoreCase(AppUtil.getString(context, TagsPreferences.USER_ID)) ) {
                        holder.profilePic.setVisibility(View.INVISIBLE);
//                        holder.profilePic.requestLayout();
//                        holder.profilePic.getLayoutParams().height = 0;
                    } else {

                        holder.profilePic.setVisibility(View.VISIBLE);

                    }
                } else {

                    holder.profilePic.setVisibility(View.VISIBLE);
                }

                    holder.leftMessageLayout.setVisibility(View.VISIBLE);

                    if(log.getmLogtype().equalsIgnoreCase("Attachment"))
                    {
                        if(log.getDocs().substring(log.getDocs().lastIndexOf('.')+1).equalsIgnoreCase("pdf")) {
                            holder.left_attachment.setVisibility(View.VISIBLE);
                            holder.leftMessage.setVisibility(View.GONE);
                            holder.left_attachment_icon.setVisibility(View.VISIBLE);
                            holder.left_attachment_image.setVisibility(View.GONE);
                        }
                        else {
                            holder.left_attachment.setVisibility(View.GONE);
                            holder.leftMessage.setVisibility(View.GONE);
                            holder.left_attachment_icon.setVisibility(View.GONE);
                            holder.left_attachment_image.setVisibility(View.VISIBLE);
                            holder.left_attachment_image.setImageURI(Uri.parse(log.getDocs()));
                        }
                    }
                    else {
                        holder.leftMessage.setText(log.getComment());
                        holder.leftMessage.setVisibility(View.VISIBLE);
                        holder.left_attachment.setVisibility(View.GONE);
                        holder.left_attachment_icon.setVisibility(View.GONE);
                        holder.left_attachment_image.setVisibility(View.GONE);
                    }
                    holder.log_time_left.setText(deadline);
                    holder.rightMessageLayout.setVisibility(View.GONE);
                    holder.statusLayout.setVisibility(View.GONE);


                    holder.leftMessageLayout.setVisibility(View.VISIBLE);
                    holder.leftMessage.setText(log.getComment());
//                    holder.log_time_left.setText(deadline);
//                    holder.rightMessageLayout.setVisibility(View.GONE);
//                    holder.statusLayout.setVisibility(View.GONE);


            }
        }

        else
        {
            holder.profilePic.setVisibility(View.GONE);
            holder.leftMessageLayout.setVisibility(View.GONE);
            holder.rightMessageLayout.setVisibility(View.GONE);
            holder.statusLayout.setVisibility(View.VISIBLE);

//            if(position==0)
//            {
//                holder.status.setText(log.getComment());
//            }
//            else {

                holder.status.setText(log.getStatus());

            if(log.getStatus().equalsIgnoreCase("Deleted")||log.getStatus().equalsIgnoreCase("Complete"))
            {
                task_details.set_task_deleted();
            }
//            }
        }

       File file;
        file = new File(Environment.getExternalStorageDirectory().getPath(), "Kreatik/Recieved_Images"+ "/" +log.getDocs().substring(log.getDocs().lastIndexOf('/')+1));
        if(!file.exists()) {


//            if (holder.right_attachment_image.getVisibility() == View.VISIBLE) {
//                holder.download_right.setVisibility(View.VISIBLE);
//            } else {
//                holder.download_right.setVisibility(View.GONE);
//            }
            if (holder.left_attachment_image.getVisibility() == View.VISIBLE) {
                holder.download_left.setVisibility(View.VISIBLE);
            } else {
                holder.download_left.setVisibility(View.GONE);
            }
        }
        else {
            holder.download_left.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public Boolean save_image(Bitmap bitmap,String user_id,Boolean sent,int position)
    {

        String filename = getFilename(user_id,sent,position);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filename);


//          write the compressed bitmap at the destination specified by filename.
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
//            ((TaskActivity)context).runOnUiThread(new Runnable() {
//                public void run() {
//                   // Toast.makeText(((TaskActivity)context), "Hello", Toast.LENGTH_SHORT).show();
//                    AppUtil.showToast(context,"Saved Successfully");
//                }
//            });

            return true;

        } catch (FileNotFoundException e) {

            e.printStackTrace();
            return false;
        }


    }
    public String getFilename(String user_id, Boolean sent,int position) {
        String[] user = AppUtil.get_employee_from_user_id(context,user_id);
        File file = null;
        if(sent){
           file = new File(Environment.getExternalStorageDirectory().getPath(), "Kreatik/Sent_Images");
        }
        else {
            file = new File(Environment.getExternalStorageDirectory().getPath(), "Kreatik/Recieved_Images");
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting;
        if(sent)
            uriSting = (file.getAbsolutePath() + "/" +data.get(position).getDocs().substring(data.get(position).getDocs().lastIndexOf('/')+1));
        else
            uriSting = (file.getAbsolutePath() + "/" +data.get(position).getDocs().substring(data.get(position).getDocs().lastIndexOf('/')+1));
        return uriSting;

    }
}
