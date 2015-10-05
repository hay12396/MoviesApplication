package hayzohar.moviesapplication;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.session.MediaController;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import hayzohar.moviesapplication.moviesapp.R;

public class DetailActivity extends AppCompatActivity {
    private MediaController mMediaController;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ShowDetailsInformation(this);
    }


    private void ShowDetailsInformation(final Context context)
    {
        Bundle bundle = getIntent().getExtras();
        final Movie movieToDisplay = (Movie) bundle.getSerializable("movie");

        if (movieToDisplay != null) {
            TextView movieDesc = (TextView) findViewById(R.id.movieDescTextView);
            TextView movieAvgScore = (TextView) findViewById(R.id.movieAvgScoreTextView);
            TextView movieLang = (TextView) findViewById(R.id.movieLangTextView);
            TextView movieYear = (TextView) findViewById(R.id.movieYearTextView);
            TextView movieVoteCount = (TextView) findViewById(R.id.movieVoteCountTextView);
            ImageView moviePoster = (ImageView) findViewById(R.id.moviePosterImageView);
            WebView trailerWebView = (WebView) findViewById(R.id.trailerWebView);
            trailerWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return false;
                }
            });

            String frameVideo = "<html><body>Trailer:<br><iframe width=\"315\" height=\"315\" src=\""
                    + movieToDisplay.getmTrailerUrl() + "\" frameborder=\"0\" allowfullscreen></iframe></body></html>";

            movieDesc.setText(movieToDisplay.getmMovieDesc());
            movieAvgScore.setText("Score: " + movieToDisplay.getmMovieAvarageScore());
            movieLang.setText("Lang: " + movieToDisplay.getmMovieLang());
            movieYear.setText(movieToDisplay.getmReleaseDate());
            movieVoteCount.setText("Votes: " + String.valueOf(movieToDisplay.getmMovieVoteCount()));
            Picasso.with(context).load(movieToDisplay.getmImageUrl()).into(moviePoster);
            trailerWebView.getSettings().setJavaScriptEnabled(true);
            trailerWebView.loadData(frameVideo, "text/html", "utf-8");

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShareContent(movieToDisplay);
                }
            });
            setTitle(movieToDisplay.getmMovieTitle());
        }
    }

    private void ShareContent(Movie movieToDisplay) {
        try{
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            shareIntent.setType("text/plain");//what kind of data we want to share.
            shareIntent.putExtra(Intent.EXTRA_TEXT, movieToDisplay.toString());
            startActivity(shareIntent);

        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No application can perform this action.", Toast.LENGTH_SHORT).show();
        }
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
