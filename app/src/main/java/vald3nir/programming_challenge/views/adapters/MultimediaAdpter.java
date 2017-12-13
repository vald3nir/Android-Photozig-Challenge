package vald3nir.programming_challenge.views.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.Collection;

import vald3nir.programming_challenge.models.Multimedia;

/**
 * Created by vald3nir on 12/12/17
 */

@EBean
public class MultimediaAdpter extends BaseAdapter {

    private ArrayList<Multimedia> multimedias = new ArrayList<>();
    private String baseURL;

    @RootContext
    Context context;

    public void bind(String baseURL, Collection<Multimedia> multimedias) {
        this.multimedias = new ArrayList<>(multimedias);
        this.baseURL = baseURL;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.multimedias.size();
    }

    @Override
    public Multimedia getItem(int position) {
        return this.multimedias.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Builds the list item
        MultimediaItemView itemView = MultimediaItemView_.build(context);

        itemView.bind(baseURL, getItem(position));

        return itemView;
    }
}
