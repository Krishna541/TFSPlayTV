package in.fiberstory.tfsplaytv.pagination;

import android.view.ViewGroup;

import androidx.leanback.widget.Presenter;

public class LoadingPortraitPresenter extends Presenter {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        LoadingPortraitCardView cardView = new LoadingPortraitCardView(parent.getContext());
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        if (item instanceof LoadingCardView){
            LoadingPortraitCardView cardView = (LoadingPortraitCardView) viewHolder.view;
            cardView.isLoading(true);
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        if (viewHolder.view instanceof LoadingPortraitCardView) {
            LoadingPortraitCardView cardView = (LoadingPortraitCardView) viewHolder.view;
            cardView.isLoading(false);
        }
    }
}