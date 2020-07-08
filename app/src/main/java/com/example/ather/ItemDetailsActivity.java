package com.example.ather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ather.model.Contracts;
import com.example.ather.model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ItemDetailsActivity extends AppCompatActivity {
    private String contractNumberIntent = "";

    private TextView stationName, supplierName, contractNumber, validityTerm,
            contractDuration, averageUnitCost, contractTotalValue, discounts, department, status,
            commencementDate, expiryDate, createdBy;

    private Button closeBtn, editBtn, progressLogsBtn, historyBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        //Get the contract number from the previous activity to use it to get the rest of details
        contractNumberIntent = getIntent().getStringExtra("ContractNumber");

        //identify the xml views
        //TextViews
        stationName = (TextView) findViewById(R.id.station_name_details);
        supplierName = (TextView) findViewById(R.id.supplier_name_details);
        contractNumber = (TextView) findViewById(R.id.contract_number_details);
        validityTerm = (TextView) findViewById(R.id.validity_term_details);
        contractDuration = (TextView) findViewById(R.id.contract_duration_details);
        averageUnitCost = (TextView) findViewById(R.id.average_unit_cost_details);
        contractTotalValue = (TextView) findViewById(R.id.contract_total_value_details);
        discounts = (TextView) findViewById(R.id.discounts_details);
        department = (TextView) findViewById(R.id.department_name_details);
        status = (TextView) findViewById(R.id.contract_status_details);
        commencementDate = (TextView) findViewById(R.id.commencement_date_details);
        expiryDate = (TextView) findViewById(R.id.expiry_date_details);
        createdBy = (TextView) findViewById(R.id.created_by_details);
        //Buttons
        closeBtn = (Button) findViewById(R.id.close_btn_details);
        editBtn = (Button) findViewById(R.id.edit_btn_details);
        progressLogsBtn = (Button) findViewById(R.id.logs_btn_details);
        historyBtn = (Button) findViewById(R.id.history_btn_details);

        //getContractDetails gets the contract details from the database into textViews
        getContractDetails();

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemDetailsActivity.this, AddContractActivity.class);
                intent.putExtra("ContractNumber", contractNumberIntent);
                startActivity(intent);
            }
        });

        progressLogsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemDetailsActivity.this, ContractProgressActivity.class);
                intent.putExtra("ContractNumber", contractNumberIntent);
                startActivity(intent);
            }
        });


    }

    private void getContractDetails() {
        //define a database reference to the contracts node
        DatabaseReference contractRef = FirebaseDatabase.getInstance().getReference()
                .child("Contracts");
        contractRef.child(contractNumberIntent).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Contracts contracts = dataSnapshot.getValue(Contracts.class);

                    stationName.setText(contracts.getStationName());
                    supplierName.setText(contracts.getSupplierName());
                    contractNumber.setText(contracts.getContractNumber());
                    validityTerm.setText(contracts.getValidityTerm());
                    contractDuration.setText(contracts.getContractDuration());
                    averageUnitCost.setText(contracts.getAverageUnitCost());
                    contractTotalValue.setText(contracts.getContractTotalValue());
                    discounts.setText(contracts.getDiscounts());
                    department.setText(contracts.getDepartmentName());
                    status.setText(contracts.getContractStatus());
                    commencementDate.setText(contracts.getCommencementDate());
                    expiryDate.setText(contracts.getExpiryDate());

                    String userId = contracts.getUId();
                    // get user name using id
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                            .child("Users");
                    userRef.child(userId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                Users users = dataSnapshot.getValue(Users.class);
                                createdBy.setText(users.getName());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
