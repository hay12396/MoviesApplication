package hayzohar.moviesapplication.Service;

import android.os.AsyncTask;
import android.util.Log;

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

/**
 * Created by Hay Zohar on 03/10/2015.
 */
public class GetMovieTrailer extends AsyncTask<String, Void, String>  {
    private Movie mMovie;

    public GetMovieTrailer(Movie movie) {
        mMovie = movie;
    }

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;
        try {
            URL url = new URL(StringHolder.TRAILER_URL_PRE_ID + mMovie.getmMovieId() + StringHolder.TRAILER_URL_POST_ID + StringHolder.API_KEY);

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
            return getTrailerFromJson(forecastJsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getTrailerFromJson(String forecastJsonStr) throws JSONException {
        JSONObject movie = new JSONObject(forecastJsonStr);
        JSONArray array = movie.getJSONArray("results");
        JSONObject movieValues = array.getJSONObject(0);
        return movieValues.getString("key");
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        mMovie.setmTrailerUrl(StringHolder.YOUTUBE_URL_PRE + result);
    }
}
