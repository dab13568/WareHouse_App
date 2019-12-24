package com.example.onboarding1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onboarding1.Data.Action;
import com.example.onboarding1.Data.Firebase_DBManager;
import com.example.onboarding1.Data.NotifyDataChange;
import com.example.onboarding1.Data.Parcel;
import com.google.firebase.storage.OnProgressListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView parcelRecyclerView;
    private List<Parcel> parcelsCopy;
    private List<com.example.onboarding1.Data.Parcel> parcels;


    ImageButton imageButton;
    EditText search;
    static boolean  updateFlag;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        action_no_signal();
        updateFlag=false;
        search=findViewById(R.id.phone_number);
        imageButton=findViewById(R.id.add_package);
        parcelRecyclerView=findViewById(R.id.parcelsList);
        parcelRecyclerView.setHasFixedSize(true);
        parcelRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Firebase_DBManager.notifyToParcelList(new NotifyDataChange<List<com.example.onboarding1.Data.Parcel>>() {

            @Override
            public void OnDataChanged(List<com.example.onboarding1.Data.Parcel> obj) {
                parcels = obj;
                parcelsCopy=parcels;
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

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            //SEARCH OPTION
            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().equals(""))
                {
                    Firebase_DBManager.notifyToParcelList(new NotifyDataChange<List<com.example.onboarding1.Data.Parcel>>() {
                        @Override
                        public void OnDataChanged(List<com.example.onboarding1.Data.Parcel> obj) {
                            parcels = obj;
                            parcelsCopy = obj;

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
                }
                else {
                    parcels = new ArrayList<>();
                    for (Parcel p : parcelsCopy) {
                        if (p.getRecipientAddress().contains(search.getText().toString()) || p.getRecipientName().contains(search.getText().toString()) || p.getRecipientPhoneNumber().contains(search.getText().toString()))
                            parcels.add(p);
                    }
                    Firebase_DBManager.stopNotifyToParcelList();
                    parcelRecyclerView.setAdapter(new ParcelRecycleViewAdapter());
                }
            }
        });
    }





    protected void onDestroy(){
        Firebase_DBManager.stopNotifyToParcelList();
        super.onDestroy();}



    public class ParcelRecycleViewAdapter extends RecyclerView.Adapter<ParcelViewHolder>
    {
        @NonNull
        @Override
        public ParcelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getBaseContext()).inflate(R.layout.example_parcel, parent,false);
            return new ParcelViewHolder(v); }


        @Override
        public void onBindViewHolder(@NonNull ParcelViewHolder holder, int position) {
            Parcel parcel = parcels.get(position);

            holder.nameTextView.setText(parcel.getRecipientName());


            holder.phoneTextView.setText(parcel.getRecipientPhoneNumber());
            holder.address.setText(parcel.getRecipientAddress());
            holder.id.setText(String.valueOf(position+1));

            if(parcel.getFragile())
                holder.other_details.setText(" החבילה היא מסוג " + parcel.getType().toString()+ "," +" והיא מכילה תוכן שביר! ");
            else holder.other_details.setText(" החבילה היא מסוג " + parcel.getType().toString()+ "," +" ואינה מכילה תוכן שביר! ");


        }

        @Override
        public int getItemCount()
        {
            return parcels.size();
        }
    }

    class ParcelViewHolder extends RecyclerView.ViewHolder
    {
        TextView nameTextView;
        TextView phoneTextView;
        TextView other_details;
        TextView address;
        TextView id;

        ParcelViewHolder(View itemView)
        {
            super(itemView);

            id= itemView.findViewById(R.id.id_parcel);
            address = itemView.findViewById(R.id.address_parcel);
            other_details = itemView.findViewById(R.id.other_details);
            nameTextView = itemView.findViewById(R.id.name_sender);
            phoneTextView = itemView.findViewById(R.id.phone_);

            // itemView.setOnClickListener();
            itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    menu.setHeaderTitle("אפשרויות");
                    MenuItem delete = menu.add(Menu.NONE, 1, 1, "מחיקה");
                    delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int position = getAdapterPosition();
                            String id = parcels.get(position).getParcelId();
                            Firebase_DBManager.removeParcel(parcels.get(position).getRecipientPhoneNumber()+"/"+id, new Action<String>() {


                                @Override
                                public void onSuccess(String obj) {

                                }

                                @Override
                                public void onFailure(Exception exception) {                     }
                                @Override
                                public void onProgress(String status, double percent) {                      }                 });
                            return true;

                        }
                    });
                    //MenuItem update= menu.add(Menu.NONE,2,1,"עדכון");
                } });
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void action_no_signal()
    {
        if (!isNetworkAvailable())
        {
            Intent intent = new Intent(MainActivity.this, no_signal.class);
            startActivity(intent);
            finish();
        }
    }
}