package com.bugtsa.iceandfire.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bugtsa.iceandfire.R;
import com.bugtsa.iceandfire.ui.activities.interfaces.CustomClickListener;
import com.bugtsa.iceandfire.ui.views.AspectRatioImageView;
import com.bugtsa.iceandfire.utils.ConstantManager;

import java.util.List;

public class CharactersAdapter extends RecyclerView.Adapter<CharactersAdapter.UserViewHolder> {
    private static final String TAG = ConstantManager.TAG_PREFIX + CharactersAdapter.class.getSimpleName();
    private Context mContext;
    private List<Character> mCharacters;

    private CustomClickListener mCustomClickListener;

    public CharactersAdapter(List<Character> characters, Context context, CustomClickListener customClickListener) {
        mContext = context;
        mCharacters = characters;
        mCustomClickListener = customClickListener;
    }

    @Override
    public CharactersAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list, parent, false);
        return new UserViewHolder(convertView, mCustomClickListener);
    }

    /**
     * Bind данных для Holder`a
     *
     * @param holder   элемент
     * @param position позиция
     */
    @Override
    public void onBindViewHolder(final CharactersAdapter.UserViewHolder holder, int position) {
//        final Character character = mCharacters.get(position);
//        final String userPhoto;
//        if (character.getPhoto().isEmpty()) {
//            userPhoto = "null";
//            LogUtils.d(TAG, "onBindViewHolder: user with name " + character.getFullName() + "has empty photo");
//        } else {
//            userPhoto = character.getPhoto();
//        }
//
//        DataManager.getInstance().getPicasso()
//                .load(userPhoto)
//                .error(holder.mBugtsa)
//                .placeholder(holder.mBugtsa)
//                .fit()
//                .centerCrop()
//                .networkPolicy(NetworkPolicy.OFFLINE)
//                .into(holder.userPhoto, new Callback() {
//                    @Override
//                    public void onSuccess() {
//                        LogUtils.d(TAG, " load from cache");
//                    }
//
//                    @Override
//                    public void onError() {
//                        DataManager.getInstance().getPicasso()
//                                .load(userPhoto)
//                                .error(holder.mBugtsa)
//                                .placeholder(holder.mBugtsa)
//                                .fit()
//                                .centerCrop()
//                                .into(holder.userPhoto, new Callback() {
//                                    @Override
//                                    public void onSuccess() {
//
//                                    }
//
//                                    @Override
//                                    public void onError() {
//                                        LogUtils.d(TAG, " Could not fetch image");
//                                    }
//                                });
//                    }
//                });
//
//        holder.mFullName.setText(character.getFullName());
//        holder.mRait.setText(String.valueOf(character.getRait()));
//        holder.mCodeLines.setText(String.valueOf(character.getCodeLines()));
//        holder.mProjects.setText(String.valueOf(character.getProjects()));
//
//        if (character.getBio() == null || character.getBio().isEmpty()) {
//            holder.mBio.setVisibility(View.GONE);
//        } else {
//            holder.mBio.setVisibility(View.VISIBLE);
//            holder.mBio.setText(character.getBio());
//        }
//
//        holder.mLikeQuantity.setText(String.valueOf(character.getRating() - character.getRait()));
    }

    @Override
    public int getItemCount() {
        return mCharacters.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected AspectRatioImageView userPhoto;
        protected TextView mFullName, mRait, mLikeQuantity, mCodeLines, mProjects, mBio;
        protected Button mShowMore;
        protected Drawable mBugtsa;
        protected ImageView mLike;
        protected LinearLayout mLikesLayout;

        private CustomClickListener mListener;

        public UserViewHolder(View itemView, CustomClickListener customClickListener) {
            super(itemView);
            mListener = customClickListener;

            userPhoto = (AspectRatioImageView) itemView.findViewById(R.id.user_photo_iv);
            mFullName = (TextView) itemView.findViewById(R.id.user_full_name_tv);
            mRait = (TextView) itemView.findViewById(R.id.user_rait_tv);
            mCodeLines = (TextView) itemView.findViewById(R.id.user_lines_code_tv);
            mProjects = (TextView) itemView.findViewById(R.id.user_projects_tv);
            mBio = (TextView) itemView.findViewById(R.id.user_bio_tv);
            mShowMore = (Button) itemView.findViewById(R.id.more_info_btn);
            mLike = (ImageView) itemView.findViewById(R.id.like_btn_iv);
            mLikeQuantity =(TextView) itemView.findViewById(R.id.like_quantity);
            mLikesLayout = (LinearLayout) itemView.findViewById(R.id.like_layout);

            mBugtsa = userPhoto.getContext().getResources().getDrawable(R.drawable.user_bg);

            mShowMore.setOnClickListener(this);
            mLikesLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener == null) {
                return;
            }
            int position = mCharacters.indexOf(mCharacters.get(getAdapterPosition()));
            switch (v.getId()) {
                case R.id.more_info_btn:
                    mListener.onUserItemClickListener(ConstantManager.START_PROFILE_ACTIVITY_KEY, position);
                    break;
            }
        }
    }
}
