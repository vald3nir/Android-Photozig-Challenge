package vald3nir.movies.views;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collection;

import vald3nir.movies.R;
import vald3nir.movies.model.Multimedia;

public class MainActivity extends AppCompatActivity {

    private MainDelegate delegate;
    private MultimediaAdapter multimediaAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        delegate = new MainDelegate(this);
        setupActionBar();

        multimediaAdapter = new MultimediaAdapter(multimedia -> delegate.showDialog(multimedia));

        RecyclerView recyclerView = findViewById(R.id.multimedia_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(multimediaAdapter);

        delegate.listMultimediaConfiguration();
    }


    @SuppressLint("RestrictedApi")
    private void setupActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.app_name));
        }
    }

    public void bind(String assetsLocation, Collection<Multimedia> multimedia) {
        multimediaAdapter.bind(assetsLocation, multimedia);
    }
}
