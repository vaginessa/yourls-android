package de.mateware.ayourls;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import de.mateware.ayourls.databinding.ItemLinkBinding;
import de.mateware.ayourls.model.Link;
import de.mateware.ayourls.viewmodel.LinkViewModel;

/**
 * Created by mate on 09.10.2015.
 */
public class LinkLibraryAdapter extends RecyclerView.Adapter<LinkLibraryAdapter.BindingHolder> {

    private List<Link> links;

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemLinkBinding linkBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_link, parent, false);
        return new BindingHolder(linkBinding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        ItemLinkBinding binding = holder.binding;
        binding.setViewModel(new LinkViewModel(links.get(position)));
    }

    @Override
    public int getItemCount() {
        if (links == null) return 0;
        return links.size();
    }

    public void setItems(List<Link> links) {
        this.links = links;
        notifyDataSetChanged();
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ItemLinkBinding binding;

        public BindingHolder(ItemLinkBinding binding) {
            super(binding.cardview);
            this.binding = binding;
        }
    }

}
