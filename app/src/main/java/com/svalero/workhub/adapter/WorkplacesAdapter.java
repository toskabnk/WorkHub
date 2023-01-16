package com.svalero.workhub.adapter;

import static com.svalero.workhub.db.Constants.DATABASE_NAME;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.svalero.workhub.DetailWorkplace;
import com.svalero.workhub.R;
import com.svalero.workhub.RegisterWorkplace;
import com.svalero.workhub.db.WorkHubDatabase;
import com.svalero.workhub.domain.WorkPlace;

import java.io.File;
import java.util.List;

public class WorkplacesAdapter extends RecyclerView.Adapter<WorkplacesAdapter.SuperheroHolder> {

    public Context context;
    public List<WorkPlace> workplaces;
    Intent intentFrom;
    public boolean admin;


    public WorkplacesAdapter(Context context, List<WorkPlace> workplaces, Intent intent, boolean admin){
        this.context = context;
        this.workplaces = workplaces;
        this.intentFrom = intent;
        this.admin = admin;
    }
    @Override
    public SuperheroHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workplace_item, parent, false);
        return new SuperheroHolder(view);
    }

    @Override
    public void onBindViewHolder(SuperheroHolder holder, int position) {
        holder.name.setText(workplaces.get(position).getName());
        holder.description.setText(workplaces.get(position).getDescription());
        if(workplaces.get(position).getFilePath() != null) {
            Uri imageUri = Uri.fromFile(new File(workplaces.get(position).getFilePath()));
            holder.image.setImageURI(imageUri);
        }
    }

    @Override
    public int getItemCount() {
        return workplaces.size();
    }

    public class SuperheroHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView description;
        public Button details;
        public Button delete;
        public Button edit;
        public ImageView image;
        public View parenView;

        public SuperheroHolder(View view){
            super(view);
            parenView = view;
            name = view.findViewById(R.id.tvListWorkplaceName);
            description = view.findViewById(R.id.tvListWrokplaceDescription);
            details = view.findViewById(R.id.bListDetails);
            edit = view.findViewById(R.id.bListEdit);
            delete = view.findViewById(R.id.bListDelete);
            image = view.findViewById(R.id.ivListDetailsImage);

            if(!admin){
                edit.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
            }

            details.setOnClickListener(v -> seeWorkplaceDetails(getAdapterPosition()));
            edit.setOnClickListener(v -> editWorkplace(getAdapterPosition()));
            delete.setOnClickListener(v -> deleteWorkplace(getAdapterPosition()));

        }
    }

    public void seeWorkplaceDetails(int position){
        WorkPlace workPlace = workplaces.get(position);
        Intent intent = new Intent(context, DetailWorkplace.class);
        String username = intentFrom.getStringExtra("username");
        Long userID = intentFrom.getLongExtra("userID", 0L);
        intent.putExtra("username", username);
        intent.putExtra("userID", userID);
        intent.putExtra("workplace", workPlace);
        context.startActivity(intent);
    }

    public void editWorkplace(int position){
        WorkPlace workPlace = workplaces.get(position);
        Intent intent = new Intent(context, RegisterWorkplace.class);
        String username = intentFrom.getStringExtra("username");
        Long userID = intentFrom.getLongExtra("userID", 0L);
        intent.putExtra("username", username);
        intent.putExtra("userID", userID);
        intent.putExtra("workplace", workPlace);
        context.startActivity(intent);

    }

    public void deleteWorkplace(int position){
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(context);
        deleteDialog.setMessage(R.string.confirmationMessage).setTitle(R.string.removeWorkplaceMessage)
                .setPositiveButton(R.string.confirmationYes, (dialog, id) -> {
                    final WorkHubDatabase db = Room.databaseBuilder(context, WorkHubDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
                    WorkPlace workPlace = workplaces.get(position);
                    db.getWorkPlaceDAO().delete(workPlace);
                    workplaces.remove(workPlace);
                    notifyItemRemoved(position);
                }).setNegativeButton(R.string.confirmationNo, (dialog, id) -> {
                    dialog.dismiss();
                });
        AlertDialog dialog = deleteDialog.create();
        dialog.show();
    }
}
