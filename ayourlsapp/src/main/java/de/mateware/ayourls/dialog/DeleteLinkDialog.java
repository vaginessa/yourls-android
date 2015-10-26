package de.mateware.ayourls.dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import de.mateware.ayourls.R;
import de.mateware.ayourls.model.Link;

/**
 * Created by mate on 26.10.2015.
 */
public class DeleteLinkDialog extends DialogCustomView {

    public static final String ARG_LONG_LINKID = "linkId";
    public static final String ARG_BOOL_DELETEONSERVER = "deleteOnServer";

    public DeleteLinkDialog setLinkId(long linkId) {
        args.putLong(ARG_LONG_LINKID, linkId);
        return this;
    }

    public DeleteLinkDialog setCheckBoxChecked(boolean checked) {
        args.putBoolean(ARG_BOOL_DELETEONSERVER, checked);
        return this;
    }

    public DeleteLinkDialog setLink(Link link) {
        return setLinkId(link.getId());
    }

    private long getLinkId() {
        long linkId = getArguments().getLong(ARG_LONG_LINKID,-1L);
        if (linkId == -1L){
            throw new IllegalStateException("DeleteLinkDialog must be created with setLinkId or setLink");
        }
        return linkId;
    }

    private Link getLink() {
        Link link = new Link();
        link.load(getContext(),getLinkId());
        return link;
    }

    private boolean getCheckBoxChecked() {
       return getArguments().getBoolean(ARG_BOOL_DELETEONSERVER,false);
    }

    @Override
    public View getView(LayoutInflater inflater) {
        Link link = getLink();
        View view = inflater.inflate(R.layout.dialog_delete, null, false);
        TextView messageView = (TextView) view.findViewById(R.id.message);
        if (hasMessage()) {
            messageView.setText(getMessage());
        } else {
            messageView.setVisibility(View.GONE);
        }
        TextView title =  (TextView) view.findViewById(R.id.title);
        title.setText(link.getTitle());
        TextView shortUrl =  (TextView) view.findViewById(R.id.shorturl);
        shortUrl.setText(link.getShorturl());

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);


        checkBox.setChecked(getCheckBoxChecked());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setCheckBoxChecked(isChecked);
            }
        });

        return view;
    }
}
