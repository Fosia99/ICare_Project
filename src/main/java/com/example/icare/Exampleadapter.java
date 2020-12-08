package com.example.icare;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Exampleadapter extends RecyclerView.Adapter<Exampleadapter.Exampleviewholder> {
    /** Declaring variables*/
    private ArrayList<Exampleitem> mexamplelist;
    private OnItemClickListener mlistener;

    public interface OnItemClickListener {
        void onitemclick(int position);

        void ondelete(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listner) {
        mlistener = listner;
    }
/** inflates the layout that shows the tasks lists*/
    public Exampleadapter(ArrayList<Exampleitem> examplelist) {
            mexamplelist = examplelist;
        }

        @Override
        public void onBindViewHolder(@NonNull Exampleviewholder holder, int position) {

            Exampleitem curitem = mexamplelist.get(position);
            holder.mtitle.setText(curitem.getTitle());

            /** Creates a new proccess for the tasks and reminders which sets the date, time ,repeatation and
             * different colors for the tasks/reminders */
            Process p = new Process();

            holder.mdate.setText(parsedate(p.incrementmonth(curitem.getDate())));
            holder.mtime.setText(parsetime(curitem.getTime()));
            holder.mrep.setText(curitem.getRepeat());

            String color = "Black";
            if (curitem.getMarker() != null) {
                color = curitem.getMarker();
            }
            switch (color) {
                case "Black":
                    holder.marker.setImageResource(R.drawable.mblack);
                    break;
                case "Red":
                    holder.marker.setImageResource(R.drawable.mred);
                    break;
                case "Green":
                    holder.marker.setImageResource(R.drawable.mgreen);
                    break;
                case "Yellow":
                    holder.marker.setImageResource(R.drawable.myellow);
                    break;
                case "Purple":
                    holder.marker.setImageResource(R.drawable.mpurple);
                    break;
            }
            /** Sets different colors for the example_item in the layout*/
            if (position % 3 == 0) {
                holder.itemView.setBackgroundColor(Color.parseColor("#E1A6A1"));
            } else if (position % 3 == 1) {
                holder.itemView.setBackgroundColor(Color.parseColor("#Feb2a8"));
            } else {
                holder.itemView.setBackgroundColor(Color.parseColor("#fad9c1"));
            }
        }

        @NonNull
        @Override
        public Exampleviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item, parent, false);
            Exampleviewholder evh = new Exampleviewholder(v, mlistener);
            return evh;
        }

        /** Methods for parsing date */
        public String parsedate(String d) {
            if (d.equals("---")) {
                return d;
            }
            String year = d.substring(0, 4), m = d.substring(4, 6), day = d.substring(6, 8);
            int month = Integer.parseInt(m);
            return day + "-" + month + "-" + year;
        }

    /** Methods for parsing time */
        public String parsetime(String d) {
            if (d.equals("---")) {
                return d;
            }
            String h = d.substring(0, 2), m = d.substring(2, 4);
            int hr = Integer.parseInt(h);
            Boolean pm = false;
            if (hr >= 12) {
                pm = true;
                hr %= 12;
            }
            if (hr == 0)
                hr = 12;
            h = String.valueOf(hr);
            return h + ":" + m + (pm ? " PM" : " AM");
        }

    /** Declaring variables*/

    public static class Exampleviewholder extends RecyclerView.ViewHolder {
        public TextView mtitle, mtime, mdate, mrep;
        public ImageView marker;
        /** Getting Id and assigning them to corresponding viewholder */

        public Exampleviewholder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            mtitle = itemView.findViewById(R.id.reltitle);
            mtime = itemView.findViewById(R.id.reltime);
            mdate = itemView.findViewById(R.id.reldate);
            mrep = itemView.findViewById(R.id.relrep);
            marker = itemView.findViewById(R.id.relmarker);

/** Assigning a position key to the viewholder clicked*/
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onitemclick(position);
                        }
                    }
                }
            });
        }
    }

        @Override
        public int getItemCount() {
            return mexamplelist.size();
        }
    }
