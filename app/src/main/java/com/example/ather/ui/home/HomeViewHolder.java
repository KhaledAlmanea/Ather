package com.example.ather.ui.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ather.Interface.ItemClickListener;
import com.example.ather.R;

public class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtStationName, txtSupplierName, txtCommencementDate, txtExpiryDate,
            txtAvgUnitCost;

    public ItemClickListener listner;


    public HomeViewHolder(View itemView)
    {
        super(itemView);

        txtStationName = (TextView) itemView.findViewById(R.id.station_name_rc);
        txtSupplierName = (TextView) itemView.findViewById(R.id.supplier_name_rc);
        txtCommencementDate = (TextView) itemView.findViewById(R.id.com_date_rc);
        txtExpiryDate = (TextView) itemView.findViewById(R.id.expr_date_rc);
        txtAvgUnitCost = (TextView) itemView.findViewById(R.id.avg_cost_rc);
    }

    public void setItemClickListener(ItemClickListener listener)
    {
        this.listner = listener;
    }

    @Override
    public void onClick(View view)
    {
        listner.onClick(view, getAdapterPosition(), false);
    }
}