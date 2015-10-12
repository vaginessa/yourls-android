package de.mateware.ayourls.viewmodel;

import android.databinding.BaseObservable;

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

}
