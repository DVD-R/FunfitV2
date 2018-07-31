package com.funfit.usjr.thesis.funfitv2.notificationEvents;

import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.model.Notification;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dj on 1/20/2016.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    ArrayList<Notification> mList;

    //Notification types
    private static final int NOTIFICATION_ENEMY_ENVADE = 0;
    private static final int NOTIFICATION_ALLY_FORTIFIED = 1;

    //temporary var for time
    private int time = 2;

    private static ColorStateList ally;
    private static ColorStateList enemy;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_playertwo)
        ImageView mImagePlayerTwo;
        @BindView(R.id.img_status)
        ImageView mImageStatus;
        @BindView(R.id.text_info)
        TextView mTextInfo;
        @BindView(R.id.text_timestamp)
        TextView mTextTime;


        public ViewHolder(View itemView) {
            super(itemView);

            ally = ContextCompat.getColorStateList(itemView.getContext(), R.color.velocity);
            enemy = ContextCompat.getColorStateList(itemView.getContext(), R.color.impulse);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            ButterKnife.bind(this, itemView);
        }
    }

    public NotificationAdapter(ArrayList<Notification> list) {
        this.mList = list;
    }

    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_notification, parent, false);
        v.setFocusable(true);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Notification data = mList.get(position);
        String name = data.getPlayerTwoUsername();

        Glide.with(holder.mImagePlayerTwo.getContext())
                .load(data.getPlayerTwoImgUrl())
                .centerCrop()
                .crossFade()
                .into(holder.mImagePlayerTwo);
        holder.mImageStatus.setImageResource(getStatusIcon(data.getNotificationType()));
        holder.mTextInfo.setText(spanText(data.getNotificationType(),
                name.length(), name + infoMessage(data.getNotificationType())));
        holder.mTextTime.setText(time++ + " hours ago");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private int getStatusIcon(int notificationType) {
        switch (notificationType) {
            case NOTIFICATION_ALLY_FORTIFIED:
                return R.drawable.ic_status_velocity;
            case NOTIFICATION_ENEMY_ENVADE:
                return R.drawable.ic_status_impulse;
        }
        return 0;
    }

    private String infoMessage(int notificationType) {
        switch (notificationType) {
            case NOTIFICATION_ALLY_FORTIFIED:
                return " has fortified your territory.";
            case NOTIFICATION_ENEMY_ENVADE:
                return " has envaded your territory.";
        }
        return null;
    }

    private Spannable spanText(int notificationType, int nameLength, String text) {
        Spannable span = new SpannableString(text);
        StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);

        switch (notificationType) {
            case NOTIFICATION_ALLY_FORTIFIED:
                span.setSpan(new ForegroundColorSpan(ally.getDefaultColor()), 0,
                        nameLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            case NOTIFICATION_ENEMY_ENVADE:
                span.setSpan(new ForegroundColorSpan(enemy.getDefaultColor()), 0,
                        nameLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
        }

        span.setSpan(bss, 0, nameLength, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return span;
    }

}
