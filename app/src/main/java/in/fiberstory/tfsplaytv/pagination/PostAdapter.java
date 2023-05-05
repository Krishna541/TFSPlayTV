package in.fiberstory.tfsplaytv.pagination;

import android.app.Activity;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import in.fiberstory.tfsplaytv.fragments.HomePageYoutubePresenter;
import in.fiberstory.tfsplaytv.model.Content;
import in.fiberstory.tfsplaytv.model.DocumentaryItemsModel;
import in.fiberstory.tfsplaytv.model.EpisodeDatumModel;
import in.fiberstory.tfsplaytv.model.Item;
import in.fiberstory.tfsplaytv.model.TVShowsDatumModel;
import in.fiberstory.tfsplaytv.model.TvSeriesEpisode;
import in.fiberstory.tfsplaytv.presenter.DocumentariesPresenter;
import in.fiberstory.tfsplaytv.presenter.EpisodePresenter;
import in.fiberstory.tfsplaytv.presenter.MoviePresenter;
import in.fiberstory.tfsplaytv.presenter.OnRentmoviePresenter;
import in.fiberstory.tfsplaytv.presenter.PlexigoExpisodePresenter;
import in.fiberstory.tfsplaytv.presenter.ShortFilmsPresenter;
import in.fiberstory.tfsplaytv.presenter.TVShowsPresenter;


public class PostAdapter extends PaginationAdapter {

    public PostAdapter(Activity context, DocumentariesPresenter documentariesPresenter, String tag) {
        super(context, documentariesPresenter, tag);
    }

    public PostAdapter(Activity context, PlexigoExpisodePresenter plexigoExpisodePresenter, String tag) {
        super(context, plexigoExpisodePresenter, tag);
    }

    public PostAdapter(Activity context, ShortFilmsPresenter shortFilmsPresenter, String tag) {
        super(context, shortFilmsPresenter, tag);
    }

    public PostAdapter(Activity context, EpisodePresenter episodePresenter, String tag) {
        super(context, episodePresenter, tag);
    }

    public PostAdapter(Activity context, MoviePresenter moviePresenter, String tag) {
        super(context, moviePresenter, tag);
    }

    public PostAdapter(Activity context, TVShowsPresenter tvShowsPresenter, String tag) {
        super(context, tvShowsPresenter, tag);
    }

    public PostAdapter(Activity context, OnRentmoviePresenter onRentmoviePresenter, String tag) {
        super(context, onRentmoviePresenter, tag);
    }
    public PostAdapter(Activity context, HomePageYoutubePresenter youtubePresenter, String tag) {
        super(context, youtubePresenter, tag);
    }


    @Override
    public void addAllMovieItems(List<?> items) {
        List<DocumentaryItemsModel> currentPosts = getAllMovieItems();
        ArrayList<DocumentaryItemsModel> posts = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            Object object = items.get(i);
            if (object instanceof DocumentaryItemsModel && !currentPosts.contains(object)) {
                posts.add((DocumentaryItemsModel) object);
            }
        }
        addPosts(posts);
    }

    @Override
    public List<DocumentaryItemsModel> getAllMovieItems() {
        List<Object> itemList = getItems();
        ArrayList<DocumentaryItemsModel> posts = new ArrayList<>();
        for (int i = 0; i < itemList.size(); i++) {
            Object object = itemList.get(i);
            if (object instanceof DocumentaryItemsModel) posts.add((DocumentaryItemsModel) object);
        }
        return posts;
    }


    @Override
    public List<TVShowsDatumModel> getAllTVShowsItems() {
        List<Object> itemList = getItems();
        ArrayList<TVShowsDatumModel> posts = new ArrayList<>();
        for (int i = 0; i < itemList.size(); i++) {
            Object object = itemList.get(i);
            if (object instanceof TVShowsDatumModel) posts.add((TVShowsDatumModel) object);
        }
        return posts;
    }

    @Override
    public void addAllEpisodeItems(List<?> items) {
        List<EpisodeDatumModel> currentPosts = getAllEpisodeItems();
        ArrayList<EpisodeDatumModel> posts = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            Object object = items.get(i);
            if (object instanceof EpisodeDatumModel && !currentPosts.contains(object)) {
                posts.add((EpisodeDatumModel) object);
            }
        }
//        Collections.sort(posts);
        addPosts(posts);
    }

    @Override
    public List<EpisodeDatumModel> getAllEpisodeItems() {
        List<Object> itemList = getItems();
        ArrayList<EpisodeDatumModel> posts = new ArrayList<>();
        for (int i = 0; i < itemList.size(); i++) {
            Object object = itemList.get(i);
            if (object instanceof EpisodeDatumModel) posts.add((EpisodeDatumModel) object);
        }
        return posts;
    }


    @Override
    public void addAllTVShowsItems(List<?> items) {
        List<TVShowsDatumModel> currentPosts = getAllTVShowsItems();
        ArrayList<TVShowsDatumModel> posts = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            Object object = items.get(i);
            if (object instanceof TVShowsDatumModel && !currentPosts.contains(object)) {
                posts.add((TVShowsDatumModel) object);
            }
        }
//        Collections.sort(posts);
        addPosts(posts);
    }

    @Override
    public List<TvSeriesEpisode> getAllPlexigoItems(){
        List<Object> itemList = getItems();
        ArrayList<TvSeriesEpisode> posts = new ArrayList<>();
        for (int i = 0; i < itemList.size(); i++) {
            Object object = itemList.get(i);
            if (object instanceof EpisodeDatumModel) posts.add((TvSeriesEpisode) object);
        }
        return posts;
    }

    @Override
    public void addAllPlexigoExpisode(@Nullable List<?> items) {
        List<TvSeriesEpisode> currentPosts = getAllPlexigoItems();
        ArrayList<TvSeriesEpisode> posts = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            Object object = items.get(i);
            if (object instanceof TvSeriesEpisode && !currentPosts.contains(object)) {
                posts.add((TvSeriesEpisode) object);
            }
        }
//        Collections.sort(posts);
        addPosts(posts);
    }

    @Override
    public void addAllContentByChannel(@Nullable List<?> items) {
        List<Content> currentPosts = getAllContentByChannel();
        ArrayList<Content> posts = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            Object object = items.get(i);
            if (object instanceof Content && !currentPosts.contains(object)) {
                posts.add((Content) object);
            }
        }
//        Collections.sort(posts);
        addPosts(posts);
    }

    @Nullable
    @Override
    public List<Content> getAllContentByChannel() {
        List<Object> itemList = getItems();
        ArrayList<Content> posts = new ArrayList<>();
        for (int i = 0; i < itemList.size(); i++) {
            Object object = itemList.get(i);
            if (object instanceof Content) posts.add((Content) object);
        }
        return posts;
    }

    @Override
    public void addAllYoutubePlaylistContent(@Nullable List<?> items) {
        List<Item> currentPosts = getAllYoutubePlaylistContent();
        ArrayList<Item> posts = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            Object object = items.get(i);
            if (object instanceof Item && !currentPosts.contains(object)) {
                posts.add((Item) object);
            }
        }
        addPosts(posts);
    }

    @Nullable
    @Override
    public List<Item> getAllYoutubePlaylistContent() {
        List<Object> itemList = getItems();
        ArrayList<Item> posts = new ArrayList<>();
        for (int i = 0; i < itemList.size(); i++) {
            Object object = itemList.get(i);
            if (object instanceof Item)
            {
                posts.add((Item) object);

            }
        }
        return posts;
    }

}
