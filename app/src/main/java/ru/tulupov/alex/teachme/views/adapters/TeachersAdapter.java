package ru.tulupov.alex.teachme.views.adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ru.tulupov.alex.teachme.Constants;
import ru.tulupov.alex.teachme.R;
import ru.tulupov.alex.teachme.models.Teacher;
import ru.tulupov.alex.teachme.views.activivties.ShowTeacherActivity;


public class TeachersAdapter extends RecyclerView.Adapter<TeachersAdapter.TeacherViewHolder> {

    private List<Teacher> teachers = new ArrayList<>();
    private Context context;

    public TeachersAdapter(List<Teacher> teachers, Context context) {
        this.teachers = teachers;
        this.context = context;
    }

    @Override
    public TeacherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.teacher_item, parent, false);
        final TeacherViewHolder holder = new TeacherViewHolder(view);
        holder.getAdapterPosition();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    itemClicked(pos);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(TeacherViewHolder holder, int position) {
        Teacher teacher = teachers.get(position);

        String fullName = teacher.getFullName();
        holder.tvFullName.setText(fullName);
        holder.tvPrice.setText("300");
        holder.tvSubjects.setText("asdasdasd");
        holder.tvSubwayStation.setText(teacher.getSubways());

        if (teacher.getPhoto() != null) {
            Picasso.with(context).load(Constants.DOMAIN_IMAGE + teacher.getPhoto()).into(holder.imgAvatar);
        }
    }

    @Override
    public int getItemCount() {
        return teachers.size();
    }

    public boolean addNewItems(List<Teacher> newItems) {
        if(newItems != null && newItems.size() > 0) {
            teachers.addAll(newItems);
            notifyItemInserted(teachers.size() - 1);
            return true;
        } else {
            return false;
        }
    }

    private void itemClicked(int pos) {
        Intent intent = new Intent(context, ShowTeacherActivity.class);
        intent.putExtra("teacher", teachers.get(pos));
        context.startActivity(intent);
    }

    public class TeacherViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout rlCard;
        public ImageView imgAvatar;
        public TextView tvFullName;
        public TextView tvSubjects;
        public TextView tvPrice;
        public TextView tvSubwayStation;

        public TeacherViewHolder(View itemView) {
            super(itemView);

            rlCard = (RelativeLayout) itemView.findViewById(R.id.card_teacher);
            imgAvatar = (ImageView) itemView.findViewById(R.id.card_img_avatar);
            tvFullName = (TextView) itemView.findViewById(R.id.tv_card_full_name);
            tvSubjects = (TextView) itemView.findViewById(R.id.tv_card_subjects);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_card_price);
            tvSubwayStation = (TextView) itemView.findViewById(R.id.tv_card_subway_station);
        }


    }
}
