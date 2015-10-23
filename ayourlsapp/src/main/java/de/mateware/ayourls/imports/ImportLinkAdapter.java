package de.mateware.ayourls.imports;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import org.apache.commons.collections4.map.LinkedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mateware.ayourls.R;
import de.mateware.ayourls.databinding.ItemImportBinding;
import de.mateware.ayourls.model.Link;
import de.mateware.ayourls.viewmodel.LinkImportViewModel;

/**
 * Created by mate on 09.10.2015.
 */
public class ImportLinkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static Logger log = LoggerFactory.getLogger(ImportLinkAdapter.class);

    ImportLinkAdapterCallback callback;

    public ImportLinkAdapter(ImportLinkAdapterCallback callback) {
        this.callback = callback;
    }

    @Override
    public int getItemViewType(int position) {
        int type = 0;
        if (callback.hasMoreToLoad()) {
            if (position == getItemCount() - 1) type = 1;
        }
        log.debug("{} {}",position, type);
        return type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                ItemImportBinding linkBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_import, parent, false);
                return new BindingHolder(linkBinding);
            case 1:
                return new MoreHolder(LayoutInflater.from(parent.getContext())
                                                    .inflate(R.layout.item_import_loadmore, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 0:
                ItemImportBinding binding = ((BindingHolder) holder).binding;
                LinkImportViewModel linkViewModel = new LinkImportViewModel(binding.cardview.getContext());
                linkViewModel.setLink(callback.getLinkList()
                                              .getValue(position));
                binding.setViewModel(linkViewModel);
                break;
            case 1:
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callback.loadMore();
                    }
                });
                break;
        }
        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        int result = 0;
        if (callback.getLinkList() != null) result += callback.getLinkList()
                                                              .size();
        if (callback.hasMoreToLoad()) result += 1;
        log.debug("adapter count: {}", result);
        return result;
    }

    /**
     * Here is the key method to apply the animation
     */
    private int lastPosition = -1;

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(callback.getContext(), android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }


    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ItemImportBinding binding;

        public BindingHolder(ItemImportBinding binding) {
            super(binding.cardview);
            this.binding = binding;
        }
    }

    public static class MoreHolder extends RecyclerView.ViewHolder {
        public MoreHolder(View itemView) {
            super(itemView);
        }
    }

    public interface ImportLinkAdapterCallback {
        LinkedMap<String, Link> getLinkList();

        boolean hasMoreToLoad();

        void loadMore();

        Context getContext();
    }
}
