
package com.example.invoiceapp_josephambayec.ListAdapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.invoiceapp_josephambayec.R;
import com.example.invoiceapp_josephambayec.entities.InvoiceDetail;

import java.text.NumberFormat;
import java.util.List;

/**
 * Adapter for the RecyclerView that displays a list of invoice details.
 */

public class InvoiceDetailListAdapter extends RecyclerView.Adapter<InvoiceDetailListAdapter.InvoiceViewHolder> {

    private final LayoutInflater mInflater;
    private List<InvoiceDetail> mInvoiceDetails; // Cached copy of customers
	private static ClickListener clickListener;
    private static ClickListener invoiceClickListener;

    public InvoiceDetailListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public InvoiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_invoice_detail, parent, false);
        return new InvoiceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InvoiceViewHolder holder, int position) {
        if (mInvoiceDetails != null) {
            InvoiceDetail current = mInvoiceDetails.get(position);
            holder.detailsProductName.setText(current.getProductName());
            String pricePerUnit = NumberFormat.getCurrencyInstance().format(current.getPricePerUnit());
            holder.detailsPricePerUnit.setText(pricePerUnit);
            int quantity = current.getQuantity();
            String quantityString = Integer.toString(quantity);
            holder.detailsQuantity.setText(quantityString);
            String lineTotal = NumberFormat.getCurrencyInstance().format(current.getLineTotal());
            holder.detailsLineTotal.setText(lineTotal);
        }
    }

    public void setInvoices(List<InvoiceDetail> invoiceDetails) {
        mInvoiceDetails = invoiceDetails;
        notifyDataSetChanged();
    }

    /**
     * getItemCount() is called many times, and when it is first called,
     * mInvoices has not been updated (means initially, it's null, and we can't return null).
     */
    @Override
    public int getItemCount() {
        if (mInvoiceDetails != null)
            return mInvoiceDetails.size();
        else return 0;
    }

    public InvoiceDetail getInvoiceDetailAtPosition(int position) {
        return mInvoiceDetails.get(position);
    }

    class InvoiceViewHolder extends RecyclerView.ViewHolder {
        private final TextView detailsProductName;
        private final TextView detailsPricePerUnit;
        private final TextView detailsQuantity;
        private final TextView detailsLineTotal;


        private InvoiceViewHolder(View itemView) {
            super(itemView);
            detailsProductName = itemView.findViewById(R.id.invoiceDetailProductName_Label);
            detailsPricePerUnit = itemView.findViewById(R.id.invoiceDetailPricePerUnit_Label);
            detailsQuantity = itemView.findViewById(R.id.invoiceDetailQuantity_Label);
            detailsLineTotal = itemView.findViewById(R.id.invoiceDetailLineTotal_Label);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(view, getAdapterPosition());
                }
            });

        }


    }

    public void setOnItemClickListener(ClickListener clickListener) {
        InvoiceDetailListAdapter.clickListener = clickListener;
    }

    public void setOnButtonItemClickListener(ClickListener clickListener){
        InvoiceDetailListAdapter.invoiceClickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }

}
