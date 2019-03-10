package com.singhey.womenux.adapters;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.singhey.womenux.R;
import com.singhey.womenux.interfaces.ContactsClickListener;
import com.singhey.womenux.sqlite.helper.DatabaseHelper;
import com.singhey.womenux.sqlite.model.EmergencyContactModel;

import java.util.ArrayList;

public class ContactsViewAdapter extends RecyclerView.Adapter<ContactsViewAdapter.ViewHolder> implements ContactsClickListener {

    protected RecyclerView recyclerView;
    private Context context;
    private ArrayList<EmergencyContactModel> data = new ArrayList<>();
    private String TAG = ContactsViewAdapter.class.getSimpleName();
    private LayoutInflater inflater;

    public ContactsViewAdapter(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
        inflater = LayoutInflater.from(context);
        fetchAndSetData();
    }

    @Override
    public ContactsViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.emergency_contact_view, parent, false);
        return new ViewHolder(v, recyclerView, this);
    }

    private int[] imageResources = {R.drawable.sherlock, R.drawable.me, R.drawable.person, R.drawable.scuba };

    @Override
    public void onBindViewHolder(ContactsViewAdapter.ViewHolder holder, int position) {
        EmergencyContactModel e = this.data.get(position);

        holder.getName().setText(e.getName());
        holder.getLocation().setText(e.getLocation());
        holder.getEmail().setText(e.getEmail());

        String number = e.getNumber();
        number = "+91 "+number.substring(0, 5)+" "+number.substring(5, number.length());

        holder.getPhone().setText(number);
        holder.getContactImage().setBackgroundResource(imageResources[position % imageResources.length]);

    }

    @Override
    public int getItemCount() {
        if(this.data == null)
            return 0;

        return this.data.size();
    }

    public void setData(ArrayList<EmergencyContactModel> data){
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public void deleteContact(int position) {
        DatabaseHelper db = new DatabaseHelper(context);
        db.deleteEmergencyContacts(this.data.get(position));
        fetchAndSetData();
    }

    public void fetchAndSetData() {
        DatabaseHelper db = new DatabaseHelper(context);
        ArrayList<EmergencyContactModel> data = db.fetchEmergencyContact();
        this.setData(data);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name,
                location,
                phone,
                email;
        private ImageButton deleteContact;
        private ImageView contactImage;

        public TextView getName() {
            return name;
        }

        public void setName(TextView name) {
            this.name = name;
        }

        public TextView getLocation() {
            return location;
        }

        public void setLocation(TextView location) {
            this.location = location;
        }

        public TextView getPhone() {
            return phone;
        }

        public void setPhone(TextView phone) {
            this.phone = phone;
        }

        public TextView getEmail() {
            return email;
        }

        public void setEmail(TextView email) {
            this.email = email;
        }

        public ImageButton getDeleteContact() {
            return deleteContact;
        }

        public void setDeleteContact(ImageButton deleteContact) {
            this.deleteContact = deleteContact;
        }

        private String TAG = ViewHolder.class.getSimpleName();

        public ViewHolder(final View itemView, final RecyclerView recyclerView, final ContactsClickListener listener) {
            super(itemView);

            this.setName((TextView) itemView.findViewById(R.id.contact_name));
            this.setEmail((TextView) itemView.findViewById(R.id.contact_mail));
            this.setPhone((TextView) itemView.findViewById(R.id.contact_phone));
            this.setLocation((TextView) itemView.findViewById(R.id.contact_location));
            this.setDeleteContact((ImageButton) itemView.findViewById(R.id.delete_contact));
            this.setContactImage((ImageView) itemView.findViewById(R.id.contact_image));

            this.getDeleteContact().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = recyclerView.getChildAdapterPosition(itemView);
                    Log.v(TAG, "Index is: " + index);
                    listener.deleteContact(index);
                }
            });

        }

        public ImageView getContactImage() {
            return contactImage;
        }

        public void setContactImage(ImageView contactImage) {
            this.contactImage = contactImage;
        }
    }

}
