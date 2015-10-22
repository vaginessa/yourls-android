package de.mateware.ayourls.imports;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import de.mateware.ayourls.R;
import de.mateware.ayourls.databinding.ItemLinkImportBinding;
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
        ItemLinkImportBinding linkBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_link_import, parent, false);
        return new BindingHolder(linkBinding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        ItemLinkImportBinding binding = holder.binding;
        LinkImportViewModel linkViewModel = new LinkImportViewModel(binding.cardview.getContext());
        linkViewModel.setLink(callback.getLinkList().get(position));
        binding.setViewModel(linkViewModel);
    }

    @Override
    public int getItemCount() {
        if (callback.getLinkList() == null) return 0;
        return callback.getLinkList().size();
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ItemLinkImportBinding binding;

        public BindingHolder(ItemLinkImportBinding binding) {
            super(binding.cardview);
            this.binding = binding;
        }
    }

    public interface ImportLinkAdapterCallback {
        List<Link> getLinkList();
    }
}
