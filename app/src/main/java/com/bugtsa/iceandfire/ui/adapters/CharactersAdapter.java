package com.bugtsa.iceandfire.ui.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouse;
import com.bugtsa.iceandfire.databinding.ItemCharacterAdapterBinding;
import com.bugtsa.iceandfire.utils.ConstantManager;

import java.util.ArrayList;
import java.util.List;

public class CharactersAdapter extends RecyclerView.Adapter<CharactersAdapter.ItemCharacterViewHolder> {
    private static final String TAG = ConstantManager.TAG_PREFIX + CharactersAdapter.class.getSimpleName();

    private List<CharacterOfHouse> mCharacter = new ArrayList<>();
    private Listener mListener;

    public CharactersAdapter(Listener listener) {
        mListener = listener;
    }

    /**
     * кол-во элементов
     */
    @Override
    public int getItemCount() {
        return mCharacter.size();
    }

    /**
     * id по позиции
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public CharactersAdapter.ItemCharacterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemCharacterViewHolder(ItemCharacterAdapterBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false)
                .getRoot());
    }

    @Override
    public void onBindViewHolder(CharactersAdapter.ItemCharacterViewHolder holder, int position) {
        CharacterOfHouse character = mCharacter.get(position);

        holder.mBinding.setCharacter(character);
        holder.mBinding.getRoot().setOnClickListener(view -> mListener.showCosts(character));
    }

    public void setCharacter(List<CharacterOfHouse> character) {
        mCharacter.clear();
        mCharacter.addAll(character);
        notifyDataSetChanged();
    }

    class ItemCharacterViewHolder extends RecyclerView.ViewHolder {
        ItemCharacterAdapterBinding mBinding;

        public ItemCharacterViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }

    public interface Listener {
        void showCosts(CharacterOfHouse character);
    }
}
