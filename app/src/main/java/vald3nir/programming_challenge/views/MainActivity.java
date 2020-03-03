package vald3nir.programming_challenge.views.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.Collection;

import vald3nir.programming_challenge.R;
import vald3nir.programming_challenge.models.Multimedia;
import vald3nir.programming_challenge.views.MultimediaAdapter;

public class MainActivity extends AppCompatActivity {

    private MainDelegate delegate;
    private MultimediaAdapter multimediaAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        delegate = new MainDelegate(this);
        setupActionBar();

        multimediaAdapter = new MultimediaAdapter(this);

        RecyclerView recyclerView = findViewById(R.id.multimedia_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(multimediaAdapter);
//        recyclerView.setOnItemClickListener((parent, view, position, id) -> delegate.showDialog(multimediaAdapter.getItem(position)));

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
