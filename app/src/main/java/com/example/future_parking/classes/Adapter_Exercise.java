package com.example.future_parking.classes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.future_parking.R;

public class Adapter_Exercise extends RecyclerView.Adapter<Adapter_Exercise.ViewHolder> {

//    private ArrayList<Tour> mData = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;

    // data is passed into the constructor
    public Adapter_Exercise(Context context) {
        this.mInflater = LayoutInflater.from(context);
//        this.mData = data;
        this.context = context;
    }


    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.activity_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d("pttt", "position = " + position);

//        Tour tour = mData.get(position);
        holder.exercise_LBL_key.setText("No. "+ position);
//        holder.exercise_LBL_date.setText("date: "+ tour.getDate());
//        holder.exercise_LBL_time.setText("time: "+ tour.getTime_in_minutes());
//        holder.exercise_LBL_source.setText("source: "+ tour.getSource());
//        holder.exercise_LBL_dest.setText("dest: "+ tour.getDest());
//        holder.exercise_LBL_speed.setText("AVG speed: "+ tour.getAvg_speed());
//        holder.exercise_LBL_km.setText("km: "+ tour.getKm());

    }

    // total number of rows
    @Override
    public int getItemCount() {
//        if(mData == null){
//            return -1;
//        }
//        return mData.size();\
        return 1;
    }

//     convenience method for getting data at click position
//    Tour getItem(int position) {
//        return mData.get(position);
//    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView exercise_LBL_key;
        private TextView exercise_LBL_date;
        private TextView exercise_LBL_time;
        private TextView exercise_LBL_source;
        private TextView exercise_LBL_dest;
        private TextView exercise_LBL_speed;
        private TextView exercise_LBL_km;

        ViewHolder(View itemView) {
            super(itemView);
            exercise_LBL_key = itemView.findViewById(R.id.exercise_LBL_key);
            exercise_LBL_date = itemView.findViewById(R.id.exercise_LBL_date);
            exercise_LBL_time = itemView.findViewById(R.id.exercise_LBL_time);
            exercise_LBL_source = itemView.findViewById(R.id.exercise_LBL_source);
            exercise_LBL_dest = itemView.findViewById(R.id.exercise_LBL_dest);
            exercise_LBL_speed = itemView.findViewById(R.id.exercise_LBL_speed);
            exercise_LBL_km = itemView.findViewById(R.id.exercise_LBL_km);


        }

    }
}
