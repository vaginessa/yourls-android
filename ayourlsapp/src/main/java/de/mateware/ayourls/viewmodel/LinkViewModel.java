package de.mateware.ayourls.viewmodel;


import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import de.mateware.ayourls.R;
import de.mateware.ayourls.linkdetail.LinkDetailActivity;
import de.mateware.ayourls.model.Link;

/**
 * Created by mate on 09.10.2015.
 */
public class LinkViewModel extends BaseObservable {

    private Context context;
    private Link link;

    public LinkViewModel(Context context, Link link) {
        this.link = link;
        this.context = context;
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

    public String getDate() {
        return link.getDate();
    }

    public String getIp() {
        return link.getIp();
    }

    public String getClicksText() {
        return context.getResources()
                      .getQuantityString(R.plurals.viewmodel_clicks, (int) link.getClicks(), link.getClicks());
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
}
