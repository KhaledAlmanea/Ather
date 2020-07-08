package com.example.ather.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ather.AddContractActivity;
import com.example.ather.ItemDetailsActivity;
import com.example.ather.R;
import com.example.ather.model.Contracts;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddContractActivity.class);
                startActivity(intent);
            }
        });

        //reference to the contract table
        DatabaseReference contractRef = FirebaseDatabase.getInstance().getReference().child("Contracts");

        recyclerView = root.findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);



        FirebaseRecyclerOptions<Contracts> options =
                new FirebaseRecyclerOptions.Builder<Contracts>()
                        .setQuery(contractRef, Contracts.class)
                        .build();

        FirebaseRecyclerAdapter<Contracts, HomeViewHolder> adapter =
                new FirebaseRecyclerAdapter<Contracts, HomeViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull HomeViewHolder holder, int position, @NonNull final Contracts model) {
                        holder.txtStationName.setText(model.getStationName());
                        holder.txtSupplierName.setText(model.getSupplierName());
                        holder.txtCommencementDate.setText("Commencement Date: " + model.getCommencementDate());
                        holder.txtExpiryDate.setText("Expiry Date: " + model.getExpiryDate());
                        holder.txtAvgUnitCost.setText("Average Unit Cost: " + model.getAverageUnitCost() + "$");

                        final String contractNumber  = model.getContractNumber();

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                               Intent intent = new Intent(getActivity(), ItemDetailsActivity.class);
                               intent.putExtra("ContractNumber", contractNumber);
                               startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contract_recycler_item_layout,
                                parent, false);
                        HomeViewHolder holder = new HomeViewHolder(view);
                        return holder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

        return root;
    }
}
