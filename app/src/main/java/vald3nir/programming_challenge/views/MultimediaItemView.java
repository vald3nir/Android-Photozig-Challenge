package vald3nir.programming_challenge.views;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import vald3nir.programming_challenge.R;
import vald3nir.programming_challenge.models.Multimedia;

/**
 * Created by vald3nir on 12/12/17
 */

@EViewGroup(R.layout.multimedia_item_view)
public class MultimediaItemView extends LinearLayout {

    public MultimediaItemView(Context context) {
        super(context);
    }

    @ViewById
    TextView titleTextview;

    public void bind(Multimedia multimedia) {
        titleTextview.setText(multimedia.getName());
    }
}
