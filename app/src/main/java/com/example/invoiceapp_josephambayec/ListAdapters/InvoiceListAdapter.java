
package com.example.invoiceapp_josephambayec.ListAdapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.invoiceapp_josephambayec.R;
import com.example.invoiceapp_josephambayec.entities.Invoice;

import java.text.NumberFormat;
import java.util.List;

/**
 * Adapter for the RecyclerView that displays a list of invoices.
 */

public class InvoiceListAdapter extends RecyclerView.Adapter<InvoiceListAdapter.InvoiceViewHolder> {

    private final LayoutInflater mInflater;
    private List<Invoice> mInvoices; // Cached copy of invoices
	private static ClickListener clickListener;
    private static ClickListener invoiceClickListener;

    public InvoiceListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public InvoiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_invoice, parent, false);
        return new InvoiceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InvoiceViewHolder holder, int position) {
        if (mInvoices != null) {
            Invoice current = mInvoices.get(position);
            holder.invoiceNumberTextView.setText("Invoice Number: " + current.getId());
            String total = NumberFormat.getCurrencyInstance().format(current.getTotal());
            holder.totalTextView.setText("Total: " + total);
        }
    }

    /**
     * Associates a list of invoices with this adapter
    */
    public void setInvoices(List<Invoice> invoices) {
        mInvoices = invoices;
        notifyDataSetChanged();
    }

    /**
     * getItemCount() is called many times, and when it is first called,
     * mInvoices has not been updated (means initially, it's null, and we can't return null).
     */
    @Override
    public int getItemCount() {
        if (mInvoices != null)
            return mInvoices.size();
        else return 0;
    }

    /**
     * Gets the customer at a given position.
     * This method is useful for identifying which invoice
     * was clicked or swiped in methods that handle user events.
     *
     * @param position The position of the customer in the RecyclerView
     * @return The invoices at the given position
     */
    public Invoice getInvoiceAtPosition(int position) {
        return mInvoices.get(position);
    }

    class InvoiceViewHolder extends RecyclerView.ViewHolder {
        private final TextView invoiceNumberTextView;
        private final TextView totalTextView;
        private Button invoiceButton;
        private InvoiceViewHolder(View itemView) {
            super(itemView);
            invoiceNumberTextView = itemView.findViewById(R.id.invoiceNumber_Label);
            totalTextView = itemView.findViewById(R.id.invoiceTotal_Label);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(view, getAdapterPosition());
                }
            });
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        InvoiceListAdapter.clickListener = clickListener;
    }

    public void setOnButtonItemClickListener(ClickListener clickListener){
        InvoiceListAdapter.invoiceClickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }

}
