package vald3nir.movies.ui.home;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collection;

import vald3nir.movies.R;
import vald3nir.movies.model.Multimedia;
import vald3nir.movies.ui.multimedia.MultimediaActivity;
import vald3nir.movies.ui.multimedia.MultimediaAdapter;

public class HomeActivity extends AppCompatActivity implements
        MultimediaAdapter.IMultimediaAdapterListener,
        HomeDelegate.IHomeDelegate {

    private final HomeDelegate delegate = new HomeDelegate(this);
    private final MultimediaAdapter multimediaAdapter = new MultimediaAdapter(this);
    private AlertDialog alertDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupActionBar();

        RecyclerView recyclerView = findViewById(R.id.multimedia_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(multimediaAdapter);

        delegate.listMultimedia();
    }

    private void setupActionBar() {
        setSupportActionBar(findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.app_name));
        }
    }

    @Override
    public void bindAdapter(String assetsLocation, Collection<Multimedia> multimedia) {
        multimediaAdapter.bind(assetsLocation, multimedia);
    }

    @Override
    public void onClickMultimedia(Multimedia multimedia) {
        showDialog(multimedia);
    }

    public void showDialog(Multimedia multimedia) {
        if (alertDialog != null) alertDialog.dismiss();

        alertDialog = new AlertDialog.Builder(this)
                .setMessage(R.string.select_an_option)
                .setCancelable(false)
                .setPositiveButton(R.string.go_to_next_page, (dialog, id) -> {
                    dialog.cancel();
                    openMultimediaDetails(multimedia);
                })
                .setNegativeButton(R.string.download_video, (dialog, id) -> {
                    dialog.cancel();
                    downloadMultimedia(multimedia);
                })
                .create();

        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
    }

    public void downloadMultimedia(Multimedia multimedia) {
        delegate.downloadMultimedia(this, multimedia);
    }

    public void openMultimediaDetails(Multimedia multimedia) {
        String assetsLocation = delegate.getAssetsLocation();
        MultimediaActivity.startActivity(this, multimedia, assetsLocation);
    }
}
