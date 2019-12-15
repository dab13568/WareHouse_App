package com.example.onboarding1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onboarding1.Data.Firebase_DBManager;
import com.example.onboarding1.Data.NotifyDataChange;
import com.example.onboarding1.Data.Parcel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView parcelRecyclerView;
    private List<Parcel> parcels;
    ImageButton imageButton;





    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageButton=findViewById(R.id.add_package);
        parcelRecyclerView=findViewById(R.id.parcelsList);
        parcelRecyclerView.setHasFixedSize(true);
        parcelRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Firebase_DBManager.notifyToParcelList(new NotifyDataChange<List<com.example.onboarding1.Data.Parcel>>() {
            @Override
            public void OnDataChanged(List<com.example.onboarding1.Data.Parcel> obj) {
                parcels = obj;

                if (parcelRecyclerView.getAdapter() == null) {
                    parcelRecyclerView.setAdapter(new ParcelRecycleViewAdapter());
                }
                else {
                    parcelRecyclerView.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(getBaseContext(), "error to get parcel list\n" + exception.toString(), Toast.LENGTH_LONG).show();

            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, AddPackage.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }



    protected void onDestroy(){
        Firebase_DBManager.stopNotifyToParcelList();
        super.onDestroy();}



    public class ParcelRecycleViewAdapter extends RecyclerView.Adapter<ParcelViewHolder> {
        @NonNull
        @Override
        public ParcelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getBaseContext()).inflate(R.layout.example_parcel,             parent,              false);
            return new ParcelViewHolder(v); }


            /*
        public ParcelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getBaseContext()).inflate(R.layout.activity_parcel_view_holder, parent, false);
            ExtraParcelInfo extraParcelInfo = (ExtraParcelInfo) getSupportFragmentManager().findFragmentById(R.id.extra_parcel_frg);
            return new ParcelViewHolder(v, extraParcelInfo);
        }

             */

        @Override
        public void onBindViewHolder(@NonNull ParcelViewHolder holder, int position) {
            Parcel parcel = parcels.get(position);

            holder.nameTextView.setText((CharSequence)parcel.getRecipientName());
            holder.phoneTextView.setText((CharSequence)parcel.getRecipientPhoneNumber());
        }

        @Override
        public int getItemCount() {
            return parcels.size();
        }
    }
    class ParcelViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView phoneTextView;
        ParcelViewHolder(View itemView)
        {
            super(itemView);

            //personImageView = itemView.findViewById(R.id. personImageView);
            nameTextView = itemView.findViewById(R.id.name_sender);
            phoneTextView = itemView.findViewById(R.id.phone_);
        }
    }
}