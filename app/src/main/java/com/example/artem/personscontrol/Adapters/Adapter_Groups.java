package com.example.artem.personscontrol.Adapters;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.artem.personscontrol.AspNet_Classes.Groups;
import com.example.artem.personscontrol.DataClasses.Data_Singleton;
import com.example.artem.personscontrol.Fragments.GroupsFragment;
import com.example.artem.personscontrol.R;

import java.util.ArrayList;

public class Adapter_Groups extends BaseAdapter implements Filterable {
    Context context;
    Fragment fragment;
    ArrayList<Groups> users;
    ArrayList<Groups> filterList;
    CustomFilterUsers customFilter;

    LayoutInflater layoutInflater;

    public Adapter_Groups(Context context, ArrayList<Groups> users, Fragment fragment) {
        this.context = context;
        this.users = users;
        this.filterList = this.users;
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return users.indexOf(users.get(position));
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null)
            convertView = layoutInflater.inflate(R.layout.group_row, null);

        TextView title =  (TextView) convertView.findViewById(R.id.statusTextView);
        TextView owner =  (TextView) convertView.findViewById(R.id.ownerName);

        title.setText(users.get(position).title);
        owner.setText(users.get(position).ownerInfo.displayName);

        if(users.get(position).ownerInfo.id.equals(Data_Singleton.getInstance().currentUser.id))
            owner.setTextColor(Color.parseColor("#ff8a65"));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fragment != null)
                    //fragment.getFragmentManager().beginTransaction().replace(R.id.navigation_container, new AboutFragment()).commit();
                    ((GroupsFragment)fragment).ShowSelectedUserInfo( users.get(position));
            }
        });

        if(position % 2 != 0 )
            convertView.setBackgroundColor(Color.parseColor("#4fc2f7"));
//        else #8bf5ff
//            convertView.setBackgroundColor(Color.parseColor("#4fc2f7"));

        return convertView;
    }

    @Override
    public Filter getFilter() {

        if(customFilter == null)
            customFilter = new CustomFilterUsers();

        return customFilter;
    }

    class CustomFilterUsers extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults filterResults = new FilterResults();

            if(constraint != null && constraint.length() > 0){
                constraint  = constraint.toString().toUpperCase();

                ArrayList<Groups> user_firebaseListFiltered = new ArrayList<>();

                for (int i = 0; i< filterList.size(); i++)
                    if(filterList.get(i).title != null && filterList.get(i).title.toUpperCase().contains(constraint))
                        user_firebaseListFiltered.add(filterList.get(i));

                filterResults.count = user_firebaseListFiltered.size();
                filterResults.values = user_firebaseListFiltered;
            }else {
                filterResults.count = filterList.size();
                filterResults.values = filterList;
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            users = (ArrayList<Groups>) results.values;
            notifyDataSetChanged();
        }
    }
}
