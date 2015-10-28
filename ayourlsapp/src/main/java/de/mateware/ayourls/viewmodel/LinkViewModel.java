package de.mateware.ayourls.viewmodel;


import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mateware.ayourls.R;
import de.mateware.ayourls.linkdetail.LinkDetailActivity;
import de.mateware.ayourls.model.Link;

/**
 * Created by mate on 09.10.2015.
 */
public class LinkViewModel extends BaseObservable {

    private static final Logger log = LoggerFactory.getLogger(LinkViewModel.class);

    private Context context;

    private Link link = new Link();

    private int largeQrSize;
    private int smallQrSize;

    public LinkViewModel(Context context) {
        this.context = context;
//        float scaleRatio = context.getResources().getDisplayMetrics().density;
//        float largeQrPix = context.getResources().getDimension(R.dimen.qr_size);
//        float smallQrPix = context.getResources().getDimension(R.dimen.qr_size_library);
//        largeQrSize = (int) (largeQrPix / scaleRatio);
//        smallQrSize = (int) (smallQrPix / scaleRatio);
        largeQrSize = 400;
        smallQrSize = 200;//context.getResources().getDimensionPixelSize(R.dimen.qr_size_library);
    }

    public void setLink(Link link) {
        log.debug("link settled {}",link);
        this.link = link;
        notifyChange();
        //notifyChange();
    }

    public Context getContext() {
        return context;
    }

    @Bindable
    public String getTitle() {
        return link.getTitle();
    }

    @Bindable
    public String getUrl() {
        return link.getUrl();
    }

    @Bindable
    public String getKeyword() {
        return link.getKeyword();
    }

    @Bindable
    public String getShorturl() {
        return link.getShorturl();
    }

    @Bindable
    public String getDate() {
        return link.getDate();
    }

    @Bindable
    public String getIp() {
        return link.getIp();
    }

    @Bindable
    public String getClicksText() {
        return context.getResources()
                      .getQuantityString(R.plurals.viewmodel_clicks, (int) link.getClicks(), link.getClicks());
    }

    public long getId() {
        return link.getId();
    }

    public Link getLink() {
        return link;
    }

    public View.OnClickListener onClickDetails() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AppCompatActivity context = (AppCompatActivity) v.getContext();
                Intent intent = new Intent(context, LinkDetailActivity.class);
                intent.putExtra(LinkDetailActivity.EXTRA_LINK_ID, link.getId());

                Pair<View, String> p1 = Pair.create(v.findViewById(R.id.qrImage), context.getString(R.string.transition_name_qrimage));

                @SuppressWarnings("unchecked") ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(context, p1);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    context.startActivity(intent, options.toBundle());
                } else {
                    context.startActivity(intent);
                }
            }
        };
    }

    public int getSmallQrSize() {
        return smallQrSize;
    }

    public int getLargeQrSize() {
        return largeQrSize;
    }
}
