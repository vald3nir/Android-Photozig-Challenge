package vald3nir.programming_challenge.views;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;

import vald3nir.programming_challenge.R;
import vald3nir.programming_challenge.model.Multimedia;

/**
 * Created by vald3nir on 12/12/17
 */

public class MultimediaAdapter extends RecyclerView.Adapter<MultimediaViewHolder> {

    private ArrayList<Multimedia> arrayList = new ArrayList<>();
    private String baseURL;
    private MultimediaViewHolder.IMultimediaViewHolder iMultimediaViewHolder;

    public MultimediaAdapter(MultimediaViewHolder.IMultimediaViewHolder iMultimediaViewHolder) {
        this.iMultimediaViewHolder = iMultimediaViewHolder;
    }

    public void bind(String baseURL, Collection<Multimedia> multimedias) {
        this.arrayList = new ArrayList<>(multimedias);
        this.baseURL = baseURL;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MultimediaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.multimedia_item_view, parent, false);
        return new MultimediaViewHolder(v, iMultimediaViewHolder);
    }

    @Override
    public void onBindViewHolder(@NonNull MultimediaViewHolder multimediaViewHolder, int position) {
        multimediaViewHolder.bind(baseURL, arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

}
