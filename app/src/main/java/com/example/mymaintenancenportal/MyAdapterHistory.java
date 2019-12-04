package com.example.mymaintenancenportal;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MyAdapterHistory extends RecyclerView.Adapter<MyAdapterHistory.MyViewHolderHistory> {

    Context context ;
    ArrayList<Request> requests;
    String userName;
    Boolean isLandlord;


    public MyAdapterHistory(Context c, ArrayList<Request> r, String u, Boolean l)
    {
        context = c;
        requests = r;
        userName = u;
        isLandlord = l;
    }

    @Override
    public MyViewHolderHistory onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewhistory, parent, false);
        MyViewHolderHistory holder = new MyViewHolderHistory(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolderHistory holder, final int position) {
        holder.name.setText(requests.get(position).getTenantName());
        holder.description.setText(requests.get(position).getRequestText());
        holder.urgency.setText(requests.get(position).getUrgency());
        holder.status.setText(requests.get(position).getStatus());
        holder.reason.setText(requests.get(position).getCancelReason());
        holder.setId(requests.get(position).getRequestID());
        if(!requests.get(position).getImage().equals(""))
        {
            Picasso.get()
                    .load(requests.get(position).getImage())
                    .fit()
                    .into(holder.requestImage);
        }
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public class MyViewHolderHistory extends RecyclerView.ViewHolder
    {
        TextView name, urgency, description, status, reason;
        ImageView requestImage;
        String id;
        private WeakReference<ClickListener> listenerRef;


        public MyViewHolderHistory(@NonNull View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.nameView);
            description = (TextView) itemView.findViewById(R.id.descriptionView);
            urgency = (TextView) itemView.findViewById(R.id.urgencyView);
            status = (TextView) itemView.findViewById(R.id.statusView);
            reason = (TextView) itemView.findViewById(R.id.reasonView);
            requestImage = (ImageView) itemView.findViewById(R.id.requestPic);
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

