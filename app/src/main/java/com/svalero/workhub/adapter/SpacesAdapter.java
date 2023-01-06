package com.svalero.workhub.adapter;

import static com.svalero.workhub.db.Constants.DATABASE_NAME;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.svalero.workhub.R;
import com.svalero.workhub.RegisterSpace;
import com.svalero.workhub.RegisterWorkplace;
import com.svalero.workhub.db.WorkHubDatabase;
import com.svalero.workhub.domain.Space;
import com.svalero.workhub.domain.WorkPlace;

import org.w3c.dom.Text;

import java.util.List;

public class SpacesAdapter extends RecyclerView.Adapter<SpacesAdapter.SuperheroHolder> {

    public Context context;
    public List<Space> spaces;
    Intent intentFrom;
    public boolean admin;
    public WorkPlace workPlace;

    public SpacesAdapter(Context context, List<Space> spaces, Intent intent, boolean admin, WorkPlace workPlace){
        this.context = context;
        this.spaces = spaces;
        this.intentFrom = intent;
        this.admin = admin;
        this.workPlace = workPlace;
    }


    public SuperheroHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.space_item, parent, false);
        return new SuperheroHolder(view);
    }

    public void onBindViewHolder(SuperheroHolder holder, int position) {
        holder.name.setText(spaces.get(position).getName());
        holder.description.setText(spaces.get(position).getDescription());
        holder.services.setText(spaces.get(position).getServices());
    }

    @Override
    public int getItemCount() {
        return spaces.size();
    }

    public class SuperheroHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView description;
        public TextView services;
        public Button reserve;
        public Button delete;
        public Button edit;
        public View parentView;

        public SuperheroHolder(View view) {
            super(view);
            parentView = view;
            name = view.findViewById(R.id.spaceItemName);
            description = view.findViewById(R.id.spaceItemDescription);
            services = view.findViewById(R.id.spaceItemServices);
            reserve = view.findViewById(R.id.spaceItemReserve);
            edit = view.findViewById(R.id.spaceItemEdit);
            delete = view.findViewById(R.id.spaceItemDelete);

            if(!admin){
                edit.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
            }

            reserve.setOnClickListener(v -> reserveSpace(getAdapterPosition()));
            edit.setOnClickListener(v -> editSpace(getAdapterPosition()));
            delete.setOnClickListener(v -> deleteSpace(getAdapterPosition()));
        }
    }

    public void reserveSpace(int position){
        Space space = spaces.get(position);
        /*
        Intent intent = new Intent(context, ReserveSpace.class);
        String username = intentFrom.getStringExtra("username");
        Long userID = intentFrom.getLongExtra("userID", 0L);
        intent.putExtra("username", username);
        intent.putExtra("userID", userID);
        intent.putExtra("workplace", workPlace);
        intent.putExtra("space", space);
        context.startActivity(intent);
        */
    }

    public void editSpace(int position){
        Space space = spaces.get(position);
        Intent intent = new Intent(context, RegisterSpace.class);
        String username = intentFrom.getStringExtra("username");
        Long userID = intentFrom.getLongExtra("userID", 0L);
        intent.putExtra("username", username);
        intent.putExtra("userID", userID);
        intent.putExtra("workplace", workPlace);
        intent.putExtra("space", space);
        context.startActivity(intent);
    }

    public void deleteSpace(int position){
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(context);
        deleteDialog.setMessage(R.string.confirmationMessage).setTitle(R.string.spaceItemDeleteMessage)
                .setPositiveButton(R.string.confirmationYes, (dialog, id) -> {
                    final WorkHubDatabase db = Room.databaseBuilder(context, WorkHubDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
                    Space space = spaces.get(position);
                    db.getSpaceDAO().delete(space);
                    spaces.remove(space);
                    notifyItemRemoved(position);
                }).setNegativeButton(R.string.confirmationNo, (dialog, id) -> {
                    dialog.dismiss();
                });
        AlertDialog dialog = deleteDialog.create();
        dialog.show();
    }
}
