package com.singhey.womenux.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.singhey.womenux.R;
import com.singhey.womenux.StoryReadActivity;
import com.singhey.womenux.sqlite.model.Story;

import java.util.ArrayList;

/**
 * Created by singh on 22-03-2018.
 */

public class StoryFragmentAdapter extends RecyclerView.Adapter<StoryFragmentAdapter.ViewHolder> {
    LayoutInflater inflater;
    private Context context;
    private RecyclerView recyclerView;
    private Activity activity;
    private ArrayList<Story> data = new ArrayList<>();

    public StoryFragmentAdapter(Context context, RecyclerView recyclerView, Activity activity) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.recyclerView = recyclerView;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View parentView = inflater.inflate(R.layout.story_view, parent, false);

        return new ViewHolder(this.activity, parentView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Story s = this.data.get(position);

        holder.getHeading().setText(s.getHeading());
        holder.getName().setText(s.getName());
        holder.getRead_count().setText("Read: "+s.getReadCount());
        holder.getStory().setText(formatStory(s.getStory()));

        Glide.with(context).load(s.getImageUrl())
                .into(holder.getImage());

    }

    private String formatStory(String story) {

        String s = "";
        if(story.length() > 150) {
            s = story.substring(0, 100)+"...";
        }else{
            s = story;
        }
        return s;

    }

    @Override
    public int getItemCount() {
        if(this.data == null ){
            return 0;
        }
        return this.data.size();
    }


    public void setData( ArrayList<Story> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView heading;
        private TextView story;

        public TextView getHeading() {
            return heading;
        }

        public void setHeading(TextView heading) {
            this.heading = heading;
        }

        public TextView getStory() {
            return story;
        }

        public void setStory(TextView story) {
            this.story = story;
        }

        public TextView getRead_count() {
            return read_count;
        }

        public void setRead_count(TextView read_count) {
            this.read_count = read_count;
        }

        public TextView getName() {
            return name;
        }

        public void setName(TextView name) {
            this.name = name;
        }

        public ImageView getImage() {
            return image;
        }

        public void setImage(ImageView image) {
            this.image = image;
        }

        private TextView read_count;
        private TextView name;

        public ViewHolder(final Activity activity, final View itemView) {
            super(itemView);
            this.heading = itemView.findViewById(R.id.story_heading);
            this.story = itemView.findViewById(R.id.story);
            this.read_count = itemView.findViewById(R.id.read_count);
            this.name = itemView.findViewById(R.id.writer_name);
            this.image = itemView.findViewById(R.id.writer_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = recyclerView.getChildAdapterPosition(view);
                    Story s = data.get(position);

                    Intent i = new Intent(context, StoryReadActivity.class);

                    i.putExtra("id", s.getId());
                    i.putExtra("name", s.getName());
                    i.putExtra("image_url", s.getImageUrl());
                    i.putExtra("heading", s.getHeading());
                    i.putExtra("story", s.getStory());
                    i.putExtra("read_count", s.getReadCount());


                    //pass transition things

                    Pair<View, String> imagePair = Pair.create(itemView.findViewById(R.id.writer_image), "transition_writer_image");
                    Pair<View, String> writerNamePair = Pair.create(itemView.findViewById(R.id.writer_name), "transition_writer_name");
                    Pair<View, String> storyHeadingPair = Pair.create(itemView.findViewById(R.id.story_heading), "transition_story_heading");
                    Pair<View, String> storyPair = Pair.create(itemView.findViewById(R.id.story), "transition_story");
                    Pair<View, String> readCountPair = Pair.create(itemView.findViewById(R.id.read_count), "transition_read_count");


                    ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation( activity ,
                            imagePair,
                            writerNamePair,
                            storyHeadingPair,
                            storyPair,
                            readCountPair);

                    context.startActivity(i, activityOptionsCompat.toBundle());
                }
            });

        }



        private ImageView image;
    }


}
