
package com.example.invoiceapp_josephambayec.ListAdapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.invoiceapp_josephambayec.R;
import com.example.invoiceapp_josephambayec.entities.Customer;

import java.util.List;

/**
 * Adapter for the RecyclerView that displays a list of customers.
 */

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.CustomerViewHolder> {

    private final LayoutInflater mInflater;
    private List<Customer> mCustomers; // Cached copy of customers
	private static ClickListener clickListener;
    private static ClickListener invoiceClickListener;

    public CustomerListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public CustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_customer, parent, false);
        return new CustomerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomerViewHolder holder, int position) {
        if (mCustomers != null) {
            Customer current = mCustomers.get(position);
            holder.customerItemView.setText(current.getName());
        } else {
            // Covers the case of data not being ready yet.
            holder.customerItemView.setText(R.string.no_word);
        }
    }

    /**
     * Associates a list of customers with this adapter
    */
    public void setCustomers(List<Customer> customers) {
        mCustomers = customers;
        notifyDataSetChanged();
    }

    /**
     * getItemCount() is called many times, and when it is first called,
     * mCustomers has not been updated (means initially, it's null, and we can't return null).
     */
    @Override
    public int getItemCount() {
        if (mCustomers != null)
            return mCustomers.size();
        else return 0;
    }

    /**
     * Gets the customer at a given position.
     * This method is useful for identifying which customer
     * was clicked or swiped in methods that handle user events.
     *
     * @param position The position of the customer in the RecyclerView
     * @return The customer at the given position
     */
    public Customer getCustomerAtPosition(int position) {
        return mCustomers.get(position);
    }

    class CustomerViewHolder extends RecyclerView.ViewHolder {
        private final TextView customerItemView;
        private Button invoiceButton;
        private CustomerViewHolder(View itemView) {
            super(itemView);
            customerItemView = itemView.findViewById(R.id.invoiceNumber_Label);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(view, getAdapterPosition());
                }
            });

            invoiceButton = itemView.findViewById(R.id.viewInvoices_Button);
            invoiceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    invoiceClickListener.onItemClick(v, getAdapterPosition());
                }
            });

        }


    }

    public void setOnItemClickListener(ClickListener clickListener) {
        CustomerListAdapter.clickListener = clickListener;
    }

    public void setOnButtonItemClickListener(ClickListener clickListener){
        CustomerListAdapter.invoiceClickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }

}
