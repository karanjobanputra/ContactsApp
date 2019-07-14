package com.karanjobanputra.contactsapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {

    private List<String> listContactData;

    public ContactsAdapter(List<String> listContactData){
        this.listContactData = listContactData;
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View contactsListItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_list_item,parent,false);
        return new ContactsViewHolder(contactsListItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder contactsViewHolder, int i) {
        contactsViewHolder.tvFullName.setText(listContactData.get(i));
    }

    @Override
    public int getItemCount() {
        return listContactData.size();
    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder {
        TextView tvFullName;
        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFullName = itemView.findViewById(R.id.tvFullName);
        }
    }

}
