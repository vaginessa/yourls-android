package de.mateware.ayourls.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;

import de.mateware.ayourls.linkdetail.LinkDetailActivity;
import de.mateware.ayourls.model.Link;

/**
 * Created by mate on 09.10.2015.
 */
public class LinkViewModel extends BaseObservable {

    private Link link;

    public LinkViewModel(Link link) {
        this.link = link;
    }

    public String getTitle() {
        return link.getTitle();
    }

    public String getUrl() {
        return link.getUrl();
    }

    public String getKeyword() {
        return link.getKeyword();
    }

    public String getShorturl() {
        return link.getShorturl();
    }

    public View.OnClickListener onClickDetails() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, LinkDetailActivity.class);
                intent.putExtra(LinkDetailActivity.EXTRA_LINK_ID,link.getId());
                context.startActivity(intent);
            }
        };
    }

}
