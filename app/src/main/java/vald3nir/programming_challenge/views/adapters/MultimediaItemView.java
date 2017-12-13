package vald3nir.programming_challenge.views.adapters;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

    @ViewById
    ImageView backgrounImageview;

    public void bind(String baseURL, Multimedia multimedia) {

        if (multimedia != null && baseURL != null) {

            if (multimedia.getName() != null) {
                titleTextview.setText(multimedia.getName());
            }

            if (multimedia.getImage() != null) {
                Picasso.with(getContext()).load(baseURL + "/" + multimedia.getImage()).into(backgrounImageview);
            }

        }


    }
}
