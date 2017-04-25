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
 * BKLoadMoreImpl
 * Created by Bakumon on 2017/4/13 17:37.
 */
public class BKLoadMoreImpl extends BKLoadMore implements RetryItem.OnRetryItemClickListener {
    private final RecyclerView recyclerView;
    private final BKLoadMore.Callbacks callbacks;
    private WrapperAdapter wrapperAdapter;

    private BKLoadMoreImpl(RecyclerView recyclerView,
                           BKLoadMore.Callbacks callbacks,
                           boolean isShowFootRaw,
                           LoadingItem loadingListItemCreator,
                           NoMoreDataItem noMoreDataItem,
                           RetryItem retryItem,
                           ItemSpanLookup itemSpanLookup) {

        this.recyclerView = recyclerView;
        this.callbacks = callbacks;
        retryItem.setOnRetryItemClickListener(this);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                checkEndOffset();
            }
        });

        if (isShowFootRaw) {

            // 包装 Adapter
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            wrapperAdapter = new WrapperAdapter(adapter, loadingListItemCreator, noMoreDataItem, retryItem);
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

    private void displayRow() {
        if (isLastPage) {
            wrapperAdapter.displayLoadingRow(false);
            wrapperAdapter.displayNoMoreDataRow(true);
            wrapperAdapter.displayRetryRow(false);
        } else if (isLoadMoreFail) {
            wrapperAdapter.displayLoadingRow(false);
            wrapperAdapter.displayNoMoreDataRow(false);
            wrapperAdapter.displayRetryRow(true);
        } else {
            wrapperAdapter.displayLoadingRow(true);
            wrapperAdapter.displayNoMoreDataRow(false);
            wrapperAdapter.displayRetryRow(false);
        }
    }

    private void onAdapterDataChanged() {
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

            if (!isLastPage && !isLoading && !isLoadMoreFail) {
                callbacks.onLoadMore();
                isLoading = true;
            }
        }
        displayRow();
    }

    private boolean isLastPage = false; // 是否是最后一页
    private boolean isLoading = false; // 加载完成 false：正在加载
    private boolean isLoadMoreFail = false; // 加载出错

    @Override
    public void setIsLastPage(boolean isLastPage) {
        this.isLastPage = isLastPage;
        if (isLastPage) {
            wrapperAdapter.displayLoadingRow(false);
            wrapperAdapter.displayNoMoreDataRow(true);
            wrapperAdapter.displayRetryRow(false);
        }
    }

    @Override
    public void completedLoadMore() {
        isLoading = false;
    }

    @Override
    public void loadMoreFail() {
        isLoadMoreFail = true;
        wrapperAdapter.displayLoadingRow(false);
        wrapperAdapter.displayNoMoreDataRow(false);
        wrapperAdapter.displayRetryRow(true);
        wrapperAdapter.notifyItemChanged(wrapperAdapter.getItemCount() - 1);
    }

    @Override
    public void onRetryItemClick() {
        isLoadMoreFail = false;
        wrapperAdapter.displayLoadingRow(true);
        wrapperAdapter.displayNoMoreDataRow(false);
        wrapperAdapter.displayRetryRow(false);
        wrapperAdapter.notifyItemChanged(wrapperAdapter.getItemCount() - 1);
    }

    public static class Builder {

        private final RecyclerView recyclerView;
        private BKLoadMore.Callbacks callbacks;

        private boolean isShowFootRaw = true;
        private LoadingItem loadingItem;
        private NoMoreDataItem noMoreDataItem;
        private RetryItem retryItem;
        private ItemSpanLookup itemSpanLookup;

        public Builder(RecyclerView recyclerView, BKLoadMore.Callbacks callbacks) {
            this.recyclerView = recyclerView;
            this.callbacks = callbacks;
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

        public Builder setRetryItem(RetryItem retryItem) {
            this.retryItem = retryItem;
            return this;
        }

        public Builder setItemSpanSizeLookup(ItemSpanLookup itemSpanLookup) {
            this.itemSpanLookup = itemSpanLookup;
            return this;
        }

        public BKLoadMoreImpl build() {
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

            if (retryItem == null) {
                retryItem = new DefaultRetryItem(callbacks);
            }

            if (itemSpanLookup == null) {
                itemSpanLookup = new DefaultItemSpanLookup(recyclerView.getLayoutManager());
            }

            return new BKLoadMoreImpl(recyclerView, callbacks,
                    isShowFootRaw, loadingItem,
                    noMoreDataItem, retryItem,
                    itemSpanLookup);
        }
    }
}
