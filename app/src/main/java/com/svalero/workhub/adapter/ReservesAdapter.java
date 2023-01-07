package com.svalero.workhub.adapter;

import static com.svalero.workhub.db.Constants.DATABASE_NAME;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.svalero.workhub.R;
import com.svalero.workhub.RegisterReserve;
import com.svalero.workhub.db.WorkHubDatabase;
import com.svalero.workhub.domain.Reserve;
import com.svalero.workhub.domain.Space;
import com.svalero.workhub.domain.User;
import com.svalero.workhub.domain.WorkPlace;

import org.w3c.dom.Text;

import java.util.List;

public class ReservesAdapter extends RecyclerView.Adapter<ReservesAdapter.SuperheroHolder> {
    public Context context;
    public List<Reserve> reserves;
    public boolean admin;
    public WorkPlace workPlace;
    public Space space;
    Intent intentFrom;

    public ReservesAdapter(Context context, List<Reserve> reserves, Intent intent, boolean admin){
        this.context = context;
        this.reserves = reserves;
        this.intentFrom = intent;
        this.admin = admin;
    }

    public SuperheroHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reserve_item, parent, false);
        return new SuperheroHolder(view);
    }

    public void onBindViewHolder(SuperheroHolder holder, int position){
        final WorkHubDatabase db = Room.databaseBuilder(context, WorkHubDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();

        space = db.getSpaceDAO().getSpaceById(reserves.get(position).getId_space());
        workPlace = db.getWorkPlaceDAO().getWorkPlaceById(space.getWorkplace());
        User user = db.getUserDAO().getById(reserves.get(position).getId_user());

        Log.i("ReserversAdapter", "onBindViewHolder - spaceID: " + space.getId());
        Log.i("ReserversAdapter", "onBindViewHolder - workplaceID: " + workPlace.getId());
        Log.i("ListWorkplaces", "onBindViewHolder - userID: " + user.getId());

        holder.workplaceName.setText(workPlace.getName());
        holder.spaceName.setText(space.getName());
        holder.reserveUsername.setText(user.getUsername());
        holder.date.setText(reserves.get(position).getDate().toString());
    }

    public int getItemCount() {
        return reserves.size();
    }


    public class SuperheroHolder extends RecyclerView.ViewHolder{
        public TextView workplaceName;
        public TextView spaceName;
        public TextView reserveUsername;
        public TextView date;
        public Button edit;
        public Button delete;
        public View parentView;

        public SuperheroHolder(View view){
            super(view);
            parentView = view;
            workplaceName = view.findViewById(R.id.listReserveWpName);
            spaceName = view.findViewById(R.id.listReserveSpaceName);
            reserveUsername = view.findViewById(R.id.listReserveUser);
            date = view.findViewById(R.id.listReserveDate);
            edit = view.findViewById(R.id.listReserveEdit);
            delete = view.findViewById(R.id.listReserveDelete);

            if(!admin){
                reserveUsername.setVisibility(View.GONE);
            }

            edit.setOnClickListener(v -> editReserve(getAdapterPosition()));
            delete.setOnClickListener(v -> deleteReserve(getAdapterPosition()));
        }
    }

    public void deleteReserve(int position){
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(context);
        deleteDialog.setMessage(R.string.confirmationMessage).setTitle(R.string.reserveDeleteMessage)
                .setPositiveButton(R.string.confirmationYes, (dialog, id) -> {
                    final WorkHubDatabase db = Room.databaseBuilder(context, WorkHubDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
                    Reserve reserve = reserves.get(position);
                    db.getReserveDAO().delete(reserve);
                    reserves.remove(reserve);
                    notifyItemRemoved(position);
                }).setNegativeButton(R.string.confirmationNo, (dialog, id) -> {
                    dialog.dismiss();
                });
        AlertDialog dialog = deleteDialog.create();
        dialog.show();
    }

    public void editReserve(int position){
        Reserve reserve = reserves.get(position);
        Intent intent = new Intent(context, RegisterReserve.class);
        String username = intentFrom.getStringExtra("username");
        Long userID = intentFrom.getLongExtra("userID", 0L);
        intent.putExtra("username", username);
        intent.putExtra("userID", userID);
        intent.putExtra("reserve", reserve);
        intent.putExtra("workplace", workPlace);
        intent.putExtra("space", space);

        context.startActivity(intent);
    }
}
