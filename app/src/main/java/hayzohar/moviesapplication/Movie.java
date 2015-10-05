package hayzohar.moviesapplication;

import java.io.Serializable;

/**
 * Created by Hay Zohar on 02/10/2015.
 */
public class Movie implements Serializable {

    private String mImageUrl;
    private String mMovieTitle;
    private String mMovieDesc;
    private String mMovieLang;
    private String mReleaseDate;
    private String mMovieId;
    private String mTrailerUrl;

    private int mMovieVoteCount;
    private double mMovieAvarageScore;

    public Movie(String movieId ,String imageUrl, String title, String desc, String lang, String releaseDate, int voteCount, double avarageScore) {
        mMovieId = movieId;
        mImageUrl = imageUrl;
        mMovieTitle = title;
        mMovieDesc = desc;
        mMovieLang = lang;
        mReleaseDate = releaseDate;
        mMovieVoteCount = voteCount;
        mMovieAvarageScore = avarageScore;
    }

    @Override
    public String toString() {
        return "Movie Title:" + mMovieTitle + "\n\nPlot:\n" + mMovieDesc;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }
    public String getmMovieAvarageScore() {
        return String.valueOf(mMovieAvarageScore) + "/10";
    }
    public String getmMovieDesc() {
        return mMovieDesc;
    }
    public String getmMovieLang() {
        return mMovieLang;
    }
    public String getmMovieTitle() {
        return mMovieTitle;
    }
    public int getmMovieVoteCount() {
        return mMovieVoteCount;
    }
    public String getmReleaseDate() {
        return mReleaseDate;
    }
    public String getmMovieId() {
        return mMovieId;
    }
    public String getmTrailerUrl() {
        return mTrailerUrl;
    }
    public void setmTrailerUrl(String mTrailerUrl) {
        this.mTrailerUrl = mTrailerUrl;
    }
}

