package com.bash.backingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.bash.backingapp.models.Step;
import java.util.List;

public class StepListAdapter extends RecyclerView.Adapter<StepListAdapter.StepListViewHolder> {
    private static final String TAG = StepListAdapter.class.getSimpleName();
    private List<Step> mStepList;
    private final Context mContext;
    final private StepItemClickListener mOnClickListener;

    public StepListAdapter(List<Step> mStepList, Context mContext, StepItemClickListener mOnClickListener) {
        this.mStepList = mStepList;
        this.mContext = mContext;
        this.mOnClickListener = mOnClickListener;
    }

    public interface StepItemClickListener {
        void OnListItemClick(Step step);
    }

    @NonNull
    @Override
    public StepListAdapter.StepListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.step_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new StepListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepListAdapter.StepListViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mStepList == null ? 0 : mStepList.size();
    }

    public void setRecipeListData(List<Step> stepList) {
        mStepList = stepList;
        notifyDataSetChanged();
    }

    public class StepListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView im_stepimage;
        TextView tv_step_order;
        TextView tv_step_shortdesc;
        ImageView iv_step_playbutton;
        View v_click;

        public StepListViewHolder(View itemView) {
            super(itemView);
            im_stepimage = itemView.findViewById(R.id.im_stepimage);
            tv_step_order = itemView.findViewById(R.id.tv_step_order);
            tv_step_shortdesc = itemView.findViewById(R.id.tv_step_shortdesc);
            iv_step_playbutton = itemView.findViewById(R.id.iv_step_playbutton);
            v_click = itemView.findViewById(R.id.step_clicker);
            v_click.bringToFront();
            v_click.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.OnListItemClick(mStepList.get(clickedPosition));
            Log.d(TAG,"step clicked: " + String.valueOf(clickedPosition));
        }

        public void bind(int position) {
            Step step = mStepList.get(position);
            tv_step_order.setText(String.valueOf(step.getId()));
            tv_step_shortdesc.setText(step.getShortDescription());
            String thumbsrc = step.getThumbnailURL();
            if (thumbsrc.length()==0) {
                im_stepimage.setImageResource(R.drawable.muffin);
            } else {
                try {
                    Picasso.with(mContext)
                            .load(thumbsrc)
                            .placeholder(R.drawable.muffin)
                            .error(R.mipmap.ic_launcher)
                            .into(im_stepimage);
                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage());
                }
            }
        }
    }
}