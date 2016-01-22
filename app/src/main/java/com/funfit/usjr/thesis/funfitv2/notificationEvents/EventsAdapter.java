package com.funfit.usjr.thesis.funfitv2.notificationEvents;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.model.Events;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dj on 1/20/2016.
 */
public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {
    private static final String TAG = EventsAdapter.class.getSimpleName();
    private static ArrayList<Events> mList;
    private static int colors[];

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.img_event)
        ImageView mImageEvent;
        @Bind(R.id.txt_event)
        TextView mTextEvent;
        @Bind(R.id.txt_bounty)
        TextView mTextBounty;
        @Bind(R.id.txt_location)
        TextView mTextLocation;
        @Bind(R.id.card_view)
        CardView mCardView;

        public ViewHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(itemView.getContext(), EventDetailActivity.class);
                    i.putExtra("EVENT",mList.get(getPosition()));

                    itemView.getContext().startActivity(i);
                }
            });
            ButterKnife.bind(this, itemView);
            mTextEvent.setTypeface(Typeface.createFromAsset(itemView.getContext().getAssets(), "HelveticaBold.otf"));
        }
    }

    public EventsAdapter(ArrayList<Events> list) {
        this.mList = list;
        colors = new int[list.size()];
    }

    @Override
    public EventsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_event, parent, false);
        v.setFocusable(true);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Events data = mList.get(position);

        Glide.with(holder.mImageEvent.getContext())
                .load(data.getImgUrl())
                .asBitmap()
                .centerCrop()
                .into((new BitmapImageViewTarget(holder.mImageEvent) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                        super.onResourceReady(bitmap, anim);
                        Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                try {
                                    Palette.Swatch swatch = palette.getLightMutedSwatch();
                                    int color = palette.getMutedColor(swatch.getTitleTextColor());
                                    colors[position] = color;
                                    holder.mCardView.setBackgroundColor(color);
                                }catch (NullPointerException e){
                                    Log.e(TAG, "Failed to load color at "+position);
                                }
                            }
                        });
                    }
                }));
        holder.mTextEvent.setText(data.getEventName());
        holder.mTextLocation.setText(data.getLocation());
        holder.mTextBounty.setText(data.getReward());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
