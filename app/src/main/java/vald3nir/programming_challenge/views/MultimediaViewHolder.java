package vald3nir.programming_challenge.views;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import vald3nir.programming_challenge.R;
import vald3nir.programming_challenge.model.Multimedia;

class MultimediaViewHolder extends RecyclerView.ViewHolder {

    private TextView titleTextview;
    private ImageView backgroundImageview;
    private IMultimediaViewHolder iMultimediaViewHolder;

    MultimediaViewHolder(@NonNull View itemView, IMultimediaViewHolder iMultimediaViewHolder) {
        super(itemView);
        titleTextview = itemView.findViewById(R.id.title_textView);
        backgroundImageview = itemView.findViewById(R.id.background_imageView);
        this.iMultimediaViewHolder = iMultimediaViewHolder;
    }

    void bind(String baseURL, Multimedia multimedia) {

        if (multimedia != null && baseURL != null) {

            if (multimedia.getName() != null) {
                titleTextview.setText(multimedia.getName());
            }

            if (multimedia.getImage() != null) {
                Picasso.get().load(baseURL + "/" + multimedia.getImage()).into(backgroundImageview);
            }
        }

        backgroundImageview.setOnClickListener(v -> {
            if (iMultimediaViewHolder != null) {
                iMultimediaViewHolder.onClickMultimedia(multimedia);
            }
        });
    }

    interface IMultimediaViewHolder {
        void onClickMultimedia(Multimedia multimedia);
    }
}
