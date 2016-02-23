package com.funfit.usjr.thesis.funfitv2.leaderBoard;

import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.funfit.usjr.thesis.funfitv2.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ocabafox on 2/24/2016.
 */
public class FilterLeaderBoardAdapter extends RecyclerView.Adapter<FilterLeaderBoardAdapter.ViewHolder> {
    private static final String TAG = "LeaderBoardAdapter";
    private static final int VIEW_TYPE_GOD_TIER = 0; // god tier is for the first big layout
    private static final int VIEW_TYPE_TOP_TIER = 1; // top tier is for the small layout
    private boolean mUseGodTierLayout = true;
    //Notification types
    private static final int NOTIFICATION_ENEMY_ENVADE = 0;
    private static final int NOTIFICATION_ALLY_FORTIFIED = 1;

    private static ColorStateList ally;
    private static ColorStateList enemy;

    FilterData[] mList; // list of item from json object

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.txtName)
        TextView txtName;
        @Bind(R.id.txtScore)
        TextView txtScore;
        @Bind(R.id.imgIcon)
        ImageView imgIcon;
        @Bind(R.id.img_leader_status)
        ImageView mImageStatus;

        String getId; // getId to be pass by Detailed_Game

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            ButterKnife.bind(this, v);

            ally = ContextCompat.getColorStateList(v.getContext(), R.color.velocity);
            enemy = ContextCompat.getColorStateList(v.getContext(),R.color.impulse);
        }

        @Override
        public void onClick(View v) {
//            Intent intent = new Intent(v.getContext(), Detailed_Game.class);
//            intent.putExtra("id",getId);
//            v.getContext().startActivity(intent);
            Log.d("here", "naa ko");
        }

        public void giveMeThat(String id){
            this.getId = id; // get ViewHolder id and pass to getId
        } // get ID from clicked view
    }

    public FilterLeaderBoardAdapter(FilterData[] list) {
        mList = list; // get object from GamesTabFragment
    }

    // Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        if ( viewGroup instanceof RecyclerView ) {
            int layoutId = -1;
            switch (viewType) {
                // if viewType == 0 it will use the first big layout
                // else it will use the small layout
                case VIEW_TYPE_GOD_TIER: {
                    layoutId = R.layout.list_item_leaderboard_top;
                    break;
                }
                case VIEW_TYPE_TOP_TIER: {
                    layoutId = R.layout.list_item_leaderboard;
                    break;
                }
            }

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
            view.setFocusable(true);
            return new ViewHolder(view); // return the view layout
        } else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    /*This method internally calls onBindViewHolder(ViewHolder, int) to update the RecyclerView.ViewHolder contents
    with the item at the given position and also sets up some private fields to be used by RecyclerView.*/
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");
        // set text from view using viewHolder

        Glide.with(viewHolder.imgIcon.getContext())
                .load(mList[position].getImageUrl())
                .centerCrop()
                .crossFade()
                .into(viewHolder.imgIcon);

        viewHolder.mImageStatus.setImageResource(getStatusIcon(mList[position].getNotificationType()));
        viewHolder.txtName.setText(spanText(mList[position].getNotificationType(), mList[position].getName().length(),
                mList[position].getName()));
//        viewHolder.txtName.setText(mList[position].getName());
        viewHolder.txtScore.setText(infoMessage(mList[position].getNotificationType())+mList[position].getScore());

    }

    /*Returns the total number of items in the data set hold by the adapter.*/
    @Override
    public int getItemCount() {
        return mList.length;
    }

    /*Return the view type of the item at position for the purposes of view recycling.*/
    @Override
    public int getItemViewType(int position) {
        return (position == 0 && mUseGodTierLayout) ? VIEW_TYPE_GOD_TIER : VIEW_TYPE_TOP_TIER;
    }


    private int getStatusIcon(int notificationType) {
        switch (notificationType){
            case NOTIFICATION_ALLY_FORTIFIED: return R.drawable.ic_status_velocity;
            case NOTIFICATION_ENEMY_ENVADE: return R.drawable.ic_status_impulse;
        }
        return 0;
    }

    private String infoMessage(int notificationType) {
        switch (notificationType){
            case NOTIFICATION_ALLY_FORTIFIED: return "Points: ";
            case NOTIFICATION_ENEMY_ENVADE: return "Points: ";
        }
        return null;
    }

    private Spannable spanText(int notificationType, int nameLength, String text) {
        Spannable span = new SpannableString(text);
        StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);

        switch (notificationType){
            case NOTIFICATION_ALLY_FORTIFIED: span.setSpan(new ForegroundColorSpan(ally.getDefaultColor()), 0,
                    nameLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); break;
            case NOTIFICATION_ENEMY_ENVADE: span.setSpan(new ForegroundColorSpan(enemy.getDefaultColor()), 0,
                    nameLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); break;
        }

        span.setSpan(bss, 0, nameLength, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return span;
    }
}
