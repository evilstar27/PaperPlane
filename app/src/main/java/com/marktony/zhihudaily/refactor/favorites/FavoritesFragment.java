package com.marktony.zhihudaily.refactor.favorites;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.refactor.data.ContentType;
import com.marktony.zhihudaily.refactor.data.DoubanMomentNewsPosts;
import com.marktony.zhihudaily.refactor.data.GuokrHandpickNewsResult;
import com.marktony.zhihudaily.refactor.data.ZhihuDailyNewsQuestion;
import com.marktony.zhihudaily.refactor.details.DetailsActivity;

import java.util.List;

/**
 * Created by lizhaotailang on 2017/6/6.
 */

public class FavoritesFragment extends Fragment
        implements FavoritesContract.View {

    private FavoritesContract.Presenter mPresenter;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private View mEmptyView;

    private FavoritesAdapter mAdapter;

    public FavoritesFragment() {
        // Empty constructor is needed as a fragment
    }

    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.re_framgent_favorites, container, false);

        initViews(view);

        mRefreshLayout.setOnRefreshListener(() -> {

        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(FavoritesContract.Presenter presenter) {
        if(presenter != null) {
            mPresenter = presenter;
        }
    }

    @Override
    public void initViews(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRefreshLayout = view.findViewById(R.id.refresh_layout);
        mEmptyView = view.findViewById(R.id.empty_view);
    }

    @Override
    public boolean isActive() {
        return isAdded() && isResumed();
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        mRefreshLayout.post(() -> mRefreshLayout.setRefreshing(active));
    }

    @Override
    public void showFavorites(List<ZhihuDailyNewsQuestion> zhihuList,
                              List<DoubanMomentNewsPosts> doubanList,
                              List<GuokrHandpickNewsResult> guokrList) {
        if (zhihuList == null || doubanList == null || guokrList == null) {
            mEmptyView.setVisibility(View.VISIBLE);
            return;
        }

        if (mAdapter == null) {
            mAdapter = new FavoritesAdapter(getContext(), zhihuList, doubanList, guokrList);
            mAdapter.setOnItemClickListener((view, position) -> {
                int viewType = mAdapter.getItemViewType(position);

                if (viewType == FavoritesAdapter.ItemWrapper.TYPE_ZHIHU) {

                    Intent intent = new Intent(getActivity(), DetailsActivity.class);
                    intent.putExtra(DetailsActivity.KEY_ARTICLE_ID, zhihuList.get(mAdapter.getOriginalIndex(position)).getId());
                    intent.putExtra(DetailsActivity.KEY_ARTICLE_TYPE, ContentType.TYPE_ZHIHU_DAILY);
                    intent.putExtra(DetailsActivity.KEY_ARTICLE_TITLE, zhihuList.get(mAdapter.getOriginalIndex(position)).getTitle());
                    startActivity(intent);

                } else if (viewType == FavoritesAdapter.ItemWrapper.TYPE_DOUBAN
                        || viewType == FavoritesAdapter.ItemWrapper.TYPE_DOUBAN_NO_IMG) {

                    Intent intent = new Intent(getActivity(), DetailsActivity.class);
                    intent.putExtra(DetailsActivity.KEY_ARTICLE_ID, doubanList.get(mAdapter.getOriginalIndex(position)).getId());
                    intent.putExtra(DetailsActivity.KEY_ARTICLE_TYPE, ContentType.TYPE_DOUBAN_MOMENT);
                    intent.putExtra(DetailsActivity.KEY_ARTICLE_TITLE, doubanList.get(mAdapter.getOriginalIndex(position)).getTitle());
                    startActivity(intent);

                } else if (viewType == FavoritesAdapter.ItemWrapper.TYPE_GUOKR) {

                    Intent intent = new Intent(getActivity(), DetailsActivity.class);
                    intent.putExtra(DetailsActivity.KEY_ARTICLE_ID, guokrList.get(mAdapter.getOriginalIndex(position)).getId());
                    intent.putExtra(DetailsActivity.KEY_ARTICLE_TYPE, ContentType.TYPE_GUOKR_HANDPICK);
                    intent.putExtra(DetailsActivity.KEY_ARTICLE_TITLE, guokrList.get(mAdapter.getOriginalIndex(position)).getTitle());
                    startActivity(intent);

                }
            });
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.updateData(zhihuList, doubanList, guokrList);
        }
        mEmptyView.setVisibility((zhihuList.isEmpty() && doubanList.isEmpty() && guokrList.isEmpty()) ? View.VISIBLE : View.GONE);
    }
}
