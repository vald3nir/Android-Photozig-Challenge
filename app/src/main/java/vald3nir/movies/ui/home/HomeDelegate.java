package vald3nir.movies.ui.home;

import android.content.Context;

import java.util.Collection;

import vald3nir.movies.model.DataAssets;
import vald3nir.movies.model.Multimedia;
import vald3nir.movies.rest.RetrofitClient;
import vald3nir.movies.tasks.VideoDownloaderTask;

public class HomeDelegate {

    private final RetrofitClient client;
    private final IHomeDelegate listener;
    private DataAssets dataAssets;

    public HomeDelegate(IHomeDelegate listener) {
        this.listener = listener;
        this.client = new RetrofitClient();
    }


    public void listMultimedia() {
        client.listMultimedias(dataAssets -> {
            this.dataAssets = dataAssets;
            listener.bindAdapter(dataAssets.getAssetsLocation(), dataAssets.getMultimedia());
        });
    }

    public void downloadMultimedia(Context context, Multimedia multimedia) {
        new VideoDownloaderTask(context, multimedia.getVideo())
                .execute(dataAssets.getAssetsLocation() + "/" + multimedia.getVideo());
    }

    public String getAssetsLocation() {
        return dataAssets.getAssetsLocation();
    }

    public interface IHomeDelegate {
        void bindAdapter(String assetsLocation, Collection<Multimedia> multimedia);
    }
}
