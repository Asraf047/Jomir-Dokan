package com.example.amanullah.myapplication50;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AMANULLAH on 05-Feb-18.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<SearchUser> contactList;
    private List<SearchUser> contactListFiltered;
    private ContactsAdapterListener listener;
    private LatLng current=new LatLng(37.4134391,-122.1513075);

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, type,address;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            type = view.findViewById(R.id.type);
            address = view.findViewById(R.id.address);

            //Typeface tf = Typeface.createFromAsset(context.getContext().getAssets(), "fonts/Roboto-Regular.ttf");
//        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/montserrat.otf");
//        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/JosefinSans-Regular.ttf");
//        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
//        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/Raleway-Regular.ttf");
//        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
          Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/Sansation-Regular.ttf");
            name.setTypeface(tf);
            type.setTypeface(tf);
            address.setTypeface(tf);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(contactListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public ContactsAdapter(Context context, List<SearchUser> contactList, ContactsAdapterListener listener, LatLng currents) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
        this.current=currents;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final SearchUser contact = contactListFiltered.get(position);
        holder.name.setText(contact.getName());

        LatLng mv=new LatLng(37.4134391,-122.1513075);
        double dis=SphericalUtil.computeDistanceBetween(contact.getCent(), current);
        String distance=new DecimalFormat("##.##").format(dis )+"";
        contact.setDistance(distance);
        if(dis<=99000) {
            String disten=contact.getType()+",  "+new DecimalFormat("##.##").format(dis )+" m"+",  "+contact.getArea()+" Sq Ft"+", "+contact.getPrice()+" USD";
            holder.type.setText(disten);
        } else {
            String disten=contact.getType()+",  "+contact.getArea()+" Sq Ft"+", "+contact.getPrice()+" USD";
            holder.type.setText(disten);
        }
        //holder.address.setText(contact.getAddress());
        holder.address.setText(contact.getAddress());


//        Glide.with(context)
//                .load(contact.getImage())
//                //.thumbnail(/*sizeMultiplier=*/ 0.9f)
//                .apply(RequestOptions.circleCropTransform())
//                .into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = contactList;
                } else {
                    List<SearchUser> filteredList = new ArrayList<>();
                    for (SearchUser row : contactList) {
                        Log.i("getKeys: ", row.getAddress()+" = rowAddress: "+row.getKey());
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())
                                || row.getAddress().contains(charSequence)
                                || row.getAddress().toLowerCase().contains(charString.toLowerCase())
                                || row.getType().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<SearchUser>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public List<SearchUser> getFilterValue() {
        return contactListFiltered;
    }

    public interface ContactsAdapterListener {
        void onContactSelected(SearchUser contact);
    }
}
