package vald3nir.movies.ui.multimedia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;

import vald3nir.movies.R;
import vald3nir.movies.model.Multimedia;

/**
 * Created by vald3nir on 12/12/17
 */

public class MultimediaAdapter extends RecyclerView.Adapter<MultimediaAdapter.MultimediaViewHolder> {

    private ArrayList<Multimedia> items = new ArrayList<>();
    private String baseURL;
    private final IMultimediaAdapterListener listener;

    public MultimediaAdapter(IMultimediaAdapterListener listener) {
        this.listener = listener;
    }

    public void bind(String baseURL, Collection<Multimedia> multimedias) {
        this.items = new ArrayList<>(multimedias);
        this.baseURL = baseURL;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MultimediaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.multimedia_item_view, parent, false);
        return new MultimediaViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MultimediaViewHolder multimediaViewHolder, int position) {
        multimediaViewHolder.bind(baseURL, items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class MultimediaViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleTextview;
        private final ImageView backgroundImageview;
        private final IMultimediaAdapterListener listener;

        MultimediaViewHolder(@NonNull View itemView, IMultimediaAdapterListener listener) {
            super(itemView);
            titleTextview = itemView.findViewById(R.id.title_textView);
            backgroundImageview = itemView.findViewById(R.id.background_imageView);
            this.listener = listener;
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
                if (listener != null) {
                    listener.onClickMultimedia(multimedia);
                }
            });
        }
    }

    public interface IMultimediaAdapterListener {
        void onClickMultimedia(Multimedia multimedia);
    }
}
