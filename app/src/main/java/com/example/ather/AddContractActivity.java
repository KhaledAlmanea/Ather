package com.example.ather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ather.model.Contracts;
import com.example.ather.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class AddContractActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Boolean contractNumberChk = false;

    private TextInputEditText stationName, supplierName, contractNumber, validityTerm,
            contractDuration, averageUnitCost, contractTotalValue, discounts;
    //variables to save user's input
    private String stationNameS, supplierNameS, contractNumberS, validityTermS,
            contractDurationS, averageUnitCostS, contractTotalValueS, discountsS,
            spinnerDepartmentS, spinnerStatusS, commencementDate, expiryDate;

    private FirebaseAuth mAuth;
    private String currentUserID;

    private Spinner spinnerDepartment, spinnerStatus;
    private TextInputLayout datePickerLayout;
    private TextView datePicker;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private Button cancelBtn, saveBtn;
    private DatabaseReference contractRef;
    private ProgressDialog loadingBar;
    private String contractNumberIntent;
    private ArrayAdapter<CharSequence> adapterDepartment, adapterStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contract);

        //get current user id to identify who created the contract
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        //get the contract number from ItemDetailsActivity, if available
        contractNumberIntent = getIntent().getStringExtra("ContractNumber");
        if (contractNumberIntent != null) {
            getDetailsFromDB();
        }

        //database reference to the contracts node
        contractRef = FirebaseDatabase.getInstance().getReference().child("Contracts");
        //loading bar
        loadingBar = new ProgressDialog(this);

        //identify the edittext and textviews and buttons
        stationName = (TextInputEditText) findViewById(R.id.station_name_et);
        supplierName = (TextInputEditText) findViewById(R.id.supplier_name_et);
        contractNumber = (TextInputEditText) findViewById(R.id.contract_number_et);
        validityTerm = (TextInputEditText) findViewById(R.id.validity_term_et);
        contractDuration = (TextInputEditText) findViewById(R.id.contract_duration_et);
        averageUnitCost = (TextInputEditText) findViewById(R.id.average_cost_et);
        contractTotalValue = (TextInputEditText) findViewById(R.id.value_et);
        discounts = (TextInputEditText) findViewById(R.id.discount_et);
        datePickerLayout = (TextInputLayout) findViewById(R.id.date_picker_ly);
        datePicker = (TextView) findViewById(R.id.date_picker);
        cancelBtn = (Button) findViewById(R.id.cancel_contract_add_btn);
        saveBtn = (Button) findViewById(R.id.save_contract_add_btn);

        //Text listener to the contract total value to automatically add "," decimal separator
        contractTotalValue.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                contractTotalValue.removeTextChangedListener(this);

                try {
                    String givenstring = editable.toString();
                    Long longval;
                    if (givenstring.contains(",")) {
                        givenstring = givenstring.replaceAll(",", "");
                    }
                    longval = Long.parseLong(givenstring);
                    DecimalFormat formatter = new DecimalFormat("#,###,###");
                    String formattedString = formatter.format(longval);
                    contractTotalValue.setText(formattedString);
                    contractTotalValue.setSelection(contractTotalValue.getText().length());
                    // to place the cursor at the end of text
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                contractTotalValue.addTextChangedListener(this);

            }
        });



        spinnerDepartment = findViewById(R.id.spinner_department);
        adapterDepartment = ArrayAdapter.createFromResource(this,
                R.array.department_list, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterDepartment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerDepartment.setAdapter(adapterDepartment);

        spinnerStatus = findViewById(R.id.spinner_status);
        adapterStatus = ArrayAdapter.createFromResource(this,
                R.array.status_list, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerStatus.setAdapter(adapterStatus);

        //set click listener to the cancel button to terminate the AddContractActivity
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddContractActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        //set click listener to the save button to save the contract details to the database
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateContractData();

            }
        });

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddContractActivity.this,
                        android.R.style.Theme_DeviceDefault_Light_Dialog,
                        onDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month +1;
                String date = month + "/" + dayOfMonth + "/" + year;
                datePicker.setText(date);
            }
        };



    }

    private void getDetailsFromDB() {

        contractRef = FirebaseDatabase.getInstance().getReference()
                .child("Contracts");
        contractRef.child(contractNumberIntent).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Contracts contracts = dataSnapshot.getValue(Contracts.class);
                stationName.setText(contracts.getStationName());
                supplierName.setText(contracts.getSupplierName());
                contractNumber.setText(contracts.getContractNumber());
                validityTerm.setText(contracts.getValidityTerm());
                contractDuration.setText(contracts.getContractDuration());
                averageUnitCost.setText(contracts.getAverageUnitCost());
                contractTotalValue.setText(contracts.getContractTotalValue());
                discounts.setText(contracts.getDiscounts());
                datePicker.setText(contracts.getCommencementDate());

                String department = contracts.getDepartmentName();

                if (department != null) {
                    int spinnerPosition = adapterDepartment.getPosition(department);
                    spinnerDepartment.setSelection(spinnerPosition);
                }


                String status = contracts.getContractStatus();

                if (status != null) {
                    int spinnerPosition = adapterStatus.getPosition(status);
                    spinnerStatus.setSelection(spinnerPosition);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ValidateContractData() {

        stationNameS = stationName.getText().toString();
        supplierNameS = supplierName.getText().toString();
        contractNumberS = contractNumber.getText().toString();
        validityTermS = validityTerm.getText().toString();
        contractDurationS = contractDuration.getText().toString();
        averageUnitCostS = averageUnitCost.getText().toString();
        contractTotalValueS = contractTotalValue.getText().toString();
        discountsS = discounts.getText().toString();
        spinnerDepartmentS = String.valueOf(spinnerDepartment.getSelectedItem());
        spinnerStatusS = String.valueOf(spinnerStatus.getSelectedItem());
        commencementDate = datePicker.getText().toString();

        //final Boolean contractNumberChk = false;

        DatabaseReference contRef = FirebaseDatabase.getInstance().getReference().child("Contracts");
        contRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(contractNumberS)){

                    contractNumberChk = true;
                }else{
                    contractNumberChk = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (contractNumberChk){
            Toast.makeText(AddContractActivity.this, "This contract Number" +
                    "is already assigned to another contract", Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(stationNameS)){
            Toast.makeText(this, "Contract operating station name is mandatory...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(supplierNameS)){
            Toast.makeText(this, "Contract operator name is mandatory...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(contractNumberS)){
            Toast.makeText(this, "Please insert the contract number", Toast.LENGTH_SHORT).show();

        }

        else if(TextUtils.isEmpty(validityTermS)){
            Toast.makeText(this, "Please insert the contract validity term", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(contractDurationS)){
            Toast.makeText(this, "Please insert the contract duration", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(averageUnitCostS)){
            Toast.makeText(this, "Please insert the contract average unit cost", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(contractTotalValueS)){
            Toast.makeText(this, "Please insert the contract total value", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(discountsS)){
            Toast.makeText(this, "Please insert the contract associated discounts", Toast.LENGTH_SHORT).show();
        }
        else if(commencementDate.equals("Select Date")){
            Toast.makeText(this, "Please select the contract commencement date", Toast.LENGTH_SHORT).show();
        }
        else {

            String[] date = commencementDate.split("/");
            String plusYear = String.valueOf(Integer.parseInt(date[2]) + Integer.parseInt(contractDurationS));
            expiryDate = date[0] + "/" + date[1] + "/" + plusYear;

            saveContractDetails();
        }
    }

    private void saveContractDetails() {

        // add validation for contract number
        loadingBar.setTitle("Add new contract");
        loadingBar.setMessage("Dear User, please wait until the contract details are saved.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();




        HashMap<String, Object> contractMap = new HashMap<>();
        contractMap.put("uId", currentUserID);
        contractMap.put("stationName", stationNameS);
        contractMap.put("supplierName", supplierNameS);
        contractMap.put("contractNumber", contractNumberS);
        contractMap.put("validityTerm", validityTermS);
        contractMap.put("contractDuration", contractDurationS);
        contractMap.put("averageUnitCost", averageUnitCostS);
        contractMap.put("contractTotalValue", contractTotalValueS);
        contractMap.put("discounts", discountsS);
        contractMap.put("departmentName", spinnerDepartmentS);
        contractMap.put("contractStatus", spinnerStatusS);
        contractMap.put("commencementDate", commencementDate);
        contractMap.put("expiryDate", expiryDate);

        contractRef.child(contractNumberS).updateChildren(contractMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            Intent intent = new Intent(AddContractActivity.this,
                                    MainActivity.class);
                            startActivity(intent);
                            loadingBar.dismiss();
                            Toast.makeText(AddContractActivity.this, "Contract is added " +
                                    "successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AddContractActivity.this, "Error: " +
                                    message, Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
