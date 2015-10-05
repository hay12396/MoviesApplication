package hayzohar.moviesapplication.Service;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import hayzohar.moviesapplication.GridViewAdapter;
import hayzohar.moviesapplication.Movie;
import hayzohar.moviesapplication.StringHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hay Zohar on 02/10/2015.
 */
public class FetchMovieInformation extends AsyncTask<String, Void, List<Movie>> {
    private GridViewAdapter mGridAdapter;
    private Activity mActivity;
    private String mSortBy;
    private boolean mOrderToFetchMore;

    @Override
    protected List<Movie> doInBackground(String... params) {
        return FetchFromServer(params[0], Integer.valueOf(params[1]));
    }

    public FetchMovieInformation(GridViewAdapter gridAdapter, Activity activity, boolean orderToFetchMore) {
        super();
        mActivity = activity;
        mGridAdapter = gridAdapter;
        mOrderToFetchMore = orderToFetchMore;
    }

    private List<Movie> FetchFromServer(String sortBy, int page) {
        mSortBy = sortBy;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;
        try {
            URL url = new URL(StringHolder.MOVIE_LIST_URL_PRE + page + "?sort_by=" + sortBy + StringHolder.MOVIE_LIST_URL_POST + StringHolder.API_KEY);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }

            forecastJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e("Error: ", "Weather data was not recieved. ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("Error: ", "Error closing stream", e);
                }
            }
        }
        try {
            return getMoviesFromJson(forecastJsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Movie> getMoviesFromJson(String forecastJsonStr) throws JSONException {
        JSONObject json = new JSONObject(forecastJsonStr);
        JSONArray jsonedMovies = json.getJSONArray("results");
        List<Movie> listofMovies = new ArrayList<>();
        for (int index = 0; index < jsonedMovies.length(); index++) {
            JSONObject jsonedMovie = jsonedMovies.getJSONObject(index);

            String movieId = jsonedMovie.getString("id");
            String posterImageUrl = StringHolder.IMAGE_URL_PRE + jsonedMovie.getString("backdrop_path");
            String movieTitle = jsonedMovie.getString("original_title");
            String movieLang = jsonedMovie.getString("original_language");
            String movieReleaseDate = jsonedMovie.getString("release_date");
            double movieAvgScore = jsonedMovie.getDouble("vote_average");
            int movieVoteCount = jsonedMovie.getInt("vote_count");
            String movieDesc = jsonedMovie.getString("overview");

            Movie movieToAdd = new Movie(movieId, posterImageUrl, movieTitle, movieDesc
                    , movieLang, movieReleaseDate, movieVoteCount, movieAvgScore);

            new GetMovieTrailer(movieToAdd).execute();
            listofMovies.add(movieToAdd);
        }

        return listofMovies;
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        if(!mOrderToFetchMore)
            mGridAdapter.clear();

        if(movies == null) {
            Toast.makeText(mActivity, "No internet connection", Toast.LENGTH_LONG).show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    waitUntilInternetIsAvailable();
                }
            }).start();

            return;
        }

        for (Movie movie: movies) {
            mGridAdapter.add(movie);
        }
    }

    private boolean haveNetworkConnection() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private boolean waitUntilInternetIsAvailable()
    {
        while(!haveNetworkConnection()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        new FetchMovieInformation(mGridAdapter, mActivity, false).execute(mSortBy, "1");
        return true;
    }
}
