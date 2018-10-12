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

import com.example.artem.personscontrol.AspNet_Classes.UserTasks;
import com.example.artem.personscontrol.Fragments.TasksFragment;
import com.example.artem.personscontrol.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Adapter_Tasks extends BaseAdapter implements Filterable {

    Context context;
    Fragment fragment;
    ArrayList<UserTasks> projects;
    ArrayList<UserTasks> filterList;
    CustomFilterprojects customFilter;

    LayoutInflater layoutInflater;

    public Adapter_Tasks(Context context, ArrayList<UserTasks> projects, Fragment fragment) {
        this.context = context;
        this.projects = projects;
        this.filterList = this.projects;
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return projects.size();
    }

    @Override
    public Object getItem(int position) {
        return projects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return projects.indexOf(projects.get(position));
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null)
            convertView = layoutInflater.inflate(R.layout.status_task_row, null);

        TextView title =  (TextView) convertView.findViewById(R.id.titleTextView);
        TextView userFrom =  (TextView) convertView.findViewById(R.id.fromUserTextView);
        TextView deadline =  (TextView) convertView.findViewById(R.id.statusTextView);

        title.setText(projects.get(position).title);
        userFrom.setText(projects.get(position).userFrom.displayName);
        deadline.setText(projects.get(position).dateTimeEnd);
        deadline.setTextColor(Color.WHITE);

        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Calendar cal = Calendar.getInstance();
            Date dt = (Date) dateFormat.parse(projects.get(position).dateTimeEnd);
            if(cal.getTime().after(dt) && !projects.get(position).isComplite){
                deadline.setBackgroundColor(Color.parseColor("#ff0000"));
                deadline.setText("\tMissing time\t");
            } else if(!cal.getTime().after(dt) && !projects.get(position).isComplite){
                deadline.setBackgroundColor(Color.parseColor("#F44F0D"));
                deadline.setText("\tIn progress\t");
            } else {
                deadline.setBackgroundColor(Color.parseColor("#02ff13"));
                deadline.setText("\tComplited\t");
                deadline.setTextColor(Color.GRAY);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fragment != null)
                    //fragment.getFragmentManager().beginTransaction().replace(R.id.navigation_container, new AboutFragment()).commit();
                    ((TasksFragment)fragment).ShowSelectedUserInfo( projects.get(position));
            }
        });

        if(position % 2 != 0 )
            convertView.setBackgroundColor(Color.parseColor("#e2f1f8"));
        else
            convertView.setBackgroundColor(Color.parseColor("#6ec6ff"));

        return convertView;
    }

    @Override
    public Filter getFilter() {

        if(customFilter == null)
            customFilter = new CustomFilterprojects();

        return customFilter;
    }

    class CustomFilterprojects extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults filterResults = new FilterResults();

            if(constraint != null && constraint.length() > 0){
                constraint  = constraint.toString().toUpperCase();

                ArrayList<UserTasks> user_firebaseListFiltered = new ArrayList<>();

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
            projects = (ArrayList<UserTasks>) results.values;
            notifyDataSetChanged();
        }
    }
    
}
