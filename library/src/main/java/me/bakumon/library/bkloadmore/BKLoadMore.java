/*
 * Copyright (c) 2017.  bakumon@aliyun.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.bakumon.library.bkloadmore;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;


/**
 * BKLoadMore
 * Created by Bakumon on 2017/4/13 17:37.
 */
public class BKLoadMore {
    private final RecyclerView recyclerView;
    private final Callbacks callbacks;
    private WrapperAdapter wrapperAdapter;

    private BKLoadMore(RecyclerView recyclerView,
                       Callbacks callbacks,
                       boolean isShowFootRaw,
                       LoadingItem loadingListItemCreator,
                       NoMoreDataItem noMoreDataItem,
                       ItemSpanLookup itemSpanLookup) {
        this.recyclerView = recyclerView;
        this.callbacks = callbacks;

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                checkEndOffset();
            }
        });

        if (isShowFootRaw) {

            // 包装 Adapter
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            wrapperAdapter = new WrapperAdapter(adapter, loadingListItemCreator, noMoreDataItem);
            RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    wrapperAdapter.notifyDataSetChanged();
                    onAdapterDataChanged();
                }

                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    wrapperAdapter.notifyItemRangeInserted(positionStart, itemCount);
                    onAdapterDataChanged();
                }

                @Override
                public void onItemRangeChanged(int positionStart, int itemCount) {
                    wrapperAdapter.notifyItemRangeChanged(positionStart, itemCount);
                    onAdapterDataChanged();
                }

                @Override
                public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                    wrapperAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
                    onAdapterDataChanged();
                }

                @Override
                public void onItemRangeRemoved(int positionStart, int itemCount) {
                    wrapperAdapter.notifyItemRangeRemoved(positionStart, itemCount);
                    onAdapterDataChanged();
                }

                @Override
                public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                    wrapperAdapter.notifyItemMoved(fromPosition, toPosition);
                    onAdapterDataChanged();
                }
            };
            adapter.registerAdapterDataObserver(mDataObserver);
            recyclerView.setAdapter(wrapperAdapter);

            // 用包装类对 GridLayoutManager 进行包装，以保证加载中和没有更多数据 item 占满一行
            if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                WrapperSpanSizeLookup wrapperSpanSizeLookup = new WrapperSpanSizeLookup(
                        ((GridLayoutManager) recyclerView.getLayoutManager()).getSpanSizeLookup(),
                        itemSpanLookup,
                        wrapperAdapter);
                ((GridLayoutManager) recyclerView.getLayoutManager()).setSpanSizeLookup(wrapperSpanSizeLookup);
            }
        }

        checkEndOffset();

    }

    private void onAdapterDataChanged() {
        wrapperAdapter.displayLoadingRow(!callbacks.isLastPage());
        wrapperAdapter.displayNoMoreDataRow(callbacks.isLastPage());
        checkEndOffset();
    }

    private void checkEndOffset() {
        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = recyclerView.getLayoutManager().getItemCount();

        int firstVisibleItemPosition;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        } else if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            if (recyclerView.getLayoutManager().getChildCount() > 0) {
                firstVisibleItemPosition = ((StaggeredGridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPositions(null)[0];
            } else {
                firstVisibleItemPosition = 0;
            }
        } else {
            throw new IllegalStateException("LayoutManager needs to subclass LinearLayoutManager or StaggeredGridLayoutManager");
        }

        if ((totalItemCount - visibleItemCount) <= (firstVisibleItemPosition)
                || totalItemCount == 0) {

            if (!callbacks.isLastPage() && !callbacks.isLoading()) {
                callbacks.onLoadMore();
            }
        }
    }

    public interface Callbacks {

        void onLoadMore();

        boolean isLoading();

        boolean isLastPage();
    }

    public static Builder with(RecyclerView recyclerView) {
        return new Builder(recyclerView);
    }

    public static class Builder {

        private final RecyclerView recyclerView;

        private boolean isShowFootRaw = true;

        private LoadingItem loadingItem;
        private NoMoreDataItem noMoreDataItem;
        private ItemSpanLookup itemSpanLookup;

        private Builder(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        public Builder seIsShowFootRaw(boolean isShowFootRaw) {
            this.isShowFootRaw = isShowFootRaw;
            return this;
        }

        public Builder setLoadingItem(LoadingItem loadingItem) {
            this.loadingItem = loadingItem;
            return this;
        }

        public Builder setNoMoreDataItem(NoMoreDataItem noMoreDataItem) {
            this.noMoreDataItem = noMoreDataItem;
            return this;
        }

        public Builder setItemSpanSizeLookup(ItemSpanLookup itemSpanLookup) {
            this.itemSpanLookup = itemSpanLookup;
            return this;
        }

        public void callBack(Callbacks callbacks) {
            if (recyclerView.getAdapter() == null) {
                throw new IllegalStateException("Adapter needs to be set!");
            }
            if (recyclerView.getLayoutManager() == null) {
                throw new IllegalStateException("LayoutManager needs to be set on the RecyclerView");
            }

            if (loadingItem == null) {
                loadingItem = LoadingItem.DEFAULT;
            }

            if (noMoreDataItem == null) {
                noMoreDataItem = NoMoreDataItem.DEFAULT;
            }

            if (itemSpanLookup == null) {
                itemSpanLookup = new DefaultItemSpanLookup(recyclerView.getLayoutManager());
            }

            new BKLoadMore(recyclerView,
                    callbacks,
                    isShowFootRaw,
                    loadingItem,
                    noMoreDataItem,
                    itemSpanLookup);
        }
    }
}
