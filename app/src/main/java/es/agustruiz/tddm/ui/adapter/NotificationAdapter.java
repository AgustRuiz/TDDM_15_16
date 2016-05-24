package es.agustruiz.tddm.ui.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.agustruiz.tddm.R;
import es.agustruiz.tddm.model.Notification;

public class NotificationAdapter extends BaseAdapter {

    public static final String LOG_TAG = NotificationAdapter.class.getName() + "[A]";

    Context mContext;
    LayoutInflater mInflater;
    int mLayoutResourceId;
    List<Notification> mData = new ArrayList<>();

    public NotificationAdapter(Context context, LayoutInflater inflater, int layoutResourceId, List<Notification> data) {
        mContext = context;
        mInflater = inflater;
        mLayoutResourceId = layoutResourceId;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Notification getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mData.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        NotificationHolder holder;
        if (row == null) {
            row = mInflater.inflate(mLayoutResourceId, parent, false);
            holder = new NotificationHolder(row);
            row.setTag(holder);
        } else {
            holder = (NotificationHolder) row.getTag();
        }
        Notification notification = mData.get(position);
        holder.mTitle.setText(notification.getTitle());
        holder.mMessage.setText(notification.getMessage());
        holder.mTimestamp.setText(parseTimestamp(notification.getTimestamp()));
        return row;
    }

    protected static class NotificationHolder {
        @BindView(R.id.notification_row_title)
        TextView mTitle;
        @BindView(R.id.notification_row_message)
        TextView mMessage;
        @BindView(R.id.notification_row_timestamp)
        TextView mTimestamp;
        public NotificationHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    protected String parseTimestamp(long timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        return DateFormat.format(mContext.getString(R.string.format_time_date), cal).toString();
    }

}
