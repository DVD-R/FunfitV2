package com.funfit.usjr.thesis.funfitv2.notificationEvents;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.model.EventModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dj on 1/20/2016.
 */
public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {
    private static final String TAG = EventsAdapter.class.getSimpleName();
    private static List<EventModel> mList;
    private static int colors[];

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_event)
        ImageView mImageEvent;
        @BindView(R.id.txt_event)
        TextView mTextEvent;
        @BindView(R.id.txt_bounty)
        TextView mTextBounty;
        @BindView(R.id.txt_location)
        TextView mTextLocation;
        @BindView(R.id.card_view)
        CardView mCardView;

        public ViewHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(itemView.getContext(), EventDetailActivity.class);
                    i.putExtra("EVENT", mList.get(getPosition()));

                    itemView.getContext().startActivity(i);
                }
            });
            ButterKnife.bind(this, itemView);
            mTextEvent.setTypeface(Typeface.createFromAsset(itemView.getContext().getAssets(), "HelveticaBold.otf"));
        }
    }

    public EventsAdapter(List<EventModel> list) {
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
        EventModel data = mList.get(position);

//        Glide.with(holder.mImageEvent.getContext())
//                .load(data.getImgUrl())
//                .asBitmap()
//                .centerCrop()
//                .into((new BitmapImageViewTarget(holder.mImageEvent) {
//                    @Override
//                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
//                        super.onResourceReady(bitmap, anim);
//                        Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
//                            @Override
//                            public void onGenerated(Palette palette) {
//                                try {
//                                    Palette.Swatch swatch = palette.getLightMutedSwatch();
//                                    int color = palette.getMutedColor(swatch.getTitleTextColor());
//                                    colors[position] = color;
//                                    holder.mCardView.setBackgroundColor(color);
//                                }catch (NullPointerException e){
//                                    Log.e(TAG, "Failed to load color at "+position);
//                                }
//                            }
//                        });
//                    }
//                }));
        holder.mTextEvent.setText(data.eventName);
        holder.mTextLocation.setText(data.locationName);
        holder.mTextBounty.setText("No Bounty please Edit");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
