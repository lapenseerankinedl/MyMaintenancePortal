package com.example.mymaintenancenportal;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context ;
    ArrayList<Request> requests;
    String userName;
    Boolean isLandlord;


    public MyAdapter(Context c, ArrayList<Request> r, String u, Boolean l)
    {
        context = c;
        requests = r;
        userName = u;
        isLandlord = l;
        //this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.name.setText(requests.get(position).getTenantName());
        holder.description.setText(requests.get(position).getRequestText());
        holder.urgency.setText(requests.get(position).getUrgency());
        holder.status.setText(requests.get(position).getStatus());
        holder.setId(requests.get(position).getRequestID());
        Picasso.get().load(requests.get(position).getImage()).into(holder.requestImage);
        if (isLandlord)
        {
            holder.editButton.setVisibility(View.VISIBLE);
        }
        holder.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,  "onClick: clicked on: cancel button");
                Intent intent = new Intent(context, CancelRequest.class);
                intent.putExtra("username", userName);
                intent.putExtra("id", requests.get(position).getRequestID());
                context.startActivity(intent);
            }
        });
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,  "onClick: clicked on: edit button");
                Intent intent = new Intent(context, EditRequest.class);
                intent.putExtra("username", userName);
                intent.putExtra("id", requests.get(position).getRequestID());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView name, urgency, description, status;
        ImageView requestImage;
        Button cancelButton, editButton;
        String id;
        private WeakReference<ClickListener> listenerRef;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //listenerRef = new WeakReference<>(listener);
            name = (TextView) itemView.findViewById(R.id.nameView);
            description = (TextView) itemView.findViewById(R.id.descriptionView);
            urgency = (TextView) itemView.findViewById(R.id.urgencyView);
            status = (TextView) itemView.findViewById(R.id.statusView);
            requestImage = (ImageView) itemView.findViewById(R.id.requestPic);
            cancelButton = (Button) itemView.findViewById(R.id.cancelRequest);
            editButton = (Button) itemView.findViewById(R.id.editRequest);
        }

        public void setId(String i)
        {
            id = i;
        }

    }

    public interface ClickListener {
        void onButtonClick(int position);
    }
}
