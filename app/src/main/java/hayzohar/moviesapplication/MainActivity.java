package hayzohar.moviesapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import hayzohar.moviesapplication.Service.FetchMovieInformation;
import hayzohar.moviesapplication.Settings.SettingsActivity;
import hayzohar.moviesapplication.moviesapp.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private GridView mGridView;
    private GridViewAdapter mGridAdapter;
    private String mLastSavedSortBy;
    private int mMovieSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mMovieSet = 2;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<Movie> list = new ArrayList<>();
        list.add(null);
        mGridView = (GridView) findViewById(R.id.moviesGridView);
        mGridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, list);
        mGridView.setAdapter(mGridAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onListItemClick(position);
            }
        });
        mLastSavedSortBy = "";
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    UpdateMoviesData(mMovieSet, true);
                    mMovieSet++;
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        UpdateMoviesData(1,false);
    }

    private void UpdateMoviesData(int page, boolean orderToFetchMore) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String sortBy = prefs.getString("sort_list", "popularity");
        if(! mLastSavedSortBy.equalsIgnoreCase(sortBy) || orderToFetchMore) {
            new FetchMovieInformation(mGridAdapter, this, orderToFetchMore).execute(sortBy,String.valueOf(page));
            mLastSavedSortBy = sortBy;
        }
    }

    private void onListItemClick(int position) {
        Movie clickedMovie = mGridAdapter.getItem(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("movie", clickedMovie);

        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
