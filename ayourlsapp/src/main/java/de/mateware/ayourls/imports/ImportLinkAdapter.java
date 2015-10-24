package de.mateware.ayourls.imports;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.apache.commons.collections4.map.LinkedMap;

import de.mateware.ayourls.R;
import de.mateware.ayourls.databinding.ItemImportBinding;
import de.mateware.ayourls.model.Link;
import de.mateware.ayourls.viewmodel.LinkImportViewModel;

/**
 * Created by mate on 09.10.2015.
 */
public class ImportLinkAdapter extends RecyclerView.Adapter<ImportLinkAdapter.BindingHolder> {

    ImportLinkAdapterCallback callback;


    public ImportLinkAdapter(ImportLinkAdapterCallback callback) {
        this.callback = callback;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemImportBinding linkBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_import, parent, false);
        return new BindingHolder(linkBinding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        ItemImportBinding binding = holder.binding;
        LinkImportViewModel linkViewModel = new LinkImportViewModel(binding.cardview.getContext());
        linkViewModel.setLink(callback.getData().getValue(position));
        binding.setViewModel(linkViewModel);
    }



    @Override
    public int getItemCount() {
        return callback.getData().size();
    }

    public void addItem(Link link) {
        if (callback.getData().containsKey(link.getKeyword())) {
            callback.getData().put(link.getKeyword(), link);
            notifyItemChanged(callback.getData().indexOf(link.getKeyword()));
        } else {
            callback.getData().put(link.getKeyword(), link);
            notifyItemInserted(callback.getData()
                                       .indexOf(link.getKeyword()));
        }
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ItemImportBinding binding;

        public BindingHolder(ItemImportBinding binding) {
            super(binding.cardview);
            this.binding = binding;
        }
    }

    public interface ImportLinkAdapterCallback {
        LinkedMap<String, Link> getData();
    }
}
