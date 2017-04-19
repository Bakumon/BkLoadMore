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

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;


/**
 * 对目标 RecyclerView 的 Adapter 的包装，增加两种 view_type
 * Created by Bakumon on 2017/4/13 16:54.
 */
class WrapperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_LOADING = Integer.MIN_VALUE + 1;
    private static final int VIEW_TYPE_NO_DATA = Integer.MIN_VALUE + 2;
    private static final int VIEW_TYPE_RETRY = Integer.MIN_VALUE + 3;

    private final RecyclerView.Adapter<RecyclerView.ViewHolder> wrappedAdapter;
    private final LoadingItem loadingListItem;
    private final NoMoreDataItem noMoreDataItem;
    private final RetryItem retryItem;
    private boolean displayLoadingRow = true;
    private boolean displayNoMoreDataRow = false;
    private boolean displayRetryRow = false;

    WrapperAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter,
                   LoadingItem loadingListItem,
                   NoMoreDataItem noMoreDataItem,
                   RetryItem retryItem) {
        this.wrappedAdapter = adapter;
        this.loadingListItem = loadingListItem;
        this.noMoreDataItem = noMoreDataItem;
        this.retryItem = retryItem;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOADING) {
            return loadingListItem.onCreateViewHolder(parent, viewType);
        } else if (viewType == VIEW_TYPE_NO_DATA) {
            return noMoreDataItem.onCreateViewHolder(parent, viewType);
        } else if (viewType == VIEW_TYPE_RETRY) {
            return retryItem.onCreateViewHolder(parent, viewType);
        }
        return wrappedAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = wrappedAdapter.getItemViewType(position);
        if (isLoadingRow(position)) {
            viewType = VIEW_TYPE_LOADING;
        } else if (isNoMoreDataRow(position)) {
            viewType = VIEW_TYPE_NO_DATA;
        } else if (isRetryRow(position)) {
            viewType = VIEW_TYPE_RETRY;
        }
        return viewType;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isLoadingRow(position)) {
            loadingListItem.onBindViewHolder(holder, position);
        } else if (isNoMoreDataRow(position)) {
            noMoreDataItem.onBindViewHolder(holder, position);
        } else if (isRetryRow(position)) {
            retryItem.onBindViewHolder(holder, position);
        } else {
            wrappedAdapter.onBindViewHolder(holder, position);
        }
    }

    @Override
    public long getItemId(int position) {
        return isLoadingRow(position) || isNoMoreDataRow(position) || isRetryRow(position)
                ? RecyclerView.NO_ID : wrappedAdapter.getItemId(position);
    }

    @Override
    public int getItemCount() {
        int count = wrappedAdapter.getItemCount();
        return displayLoadingRow || displayNoMoreDataRow || displayRetryRow
                ? count + 1 : count;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
        wrappedAdapter.setHasStableIds(hasStableIds);
    }

    /**
     * 此行是否是加载中
     */
    boolean isLoadingRow(int position) {
        int loadingRowPosition = displayLoadingRow ? getItemCount() - 1 : -1;
        return displayLoadingRow && position == loadingRowPosition;
    }

    /**
     * 此行是否是没有更多数据
     */
    boolean isNoMoreDataRow(int position) {
        int noMoreDataPositionRow = displayNoMoreDataRow ? getItemCount() - 1 : -1;
        return displayNoMoreDataRow && position == noMoreDataPositionRow;
    }

    /**
     * 此行是否是加载失败行
     */
    private boolean isRetryRow(int position) {
        int retryPositionRow = displayRetryRow ? getItemCount() - 1 : -1;
        return displayRetryRow && position == retryPositionRow;
    }

    /**
     * 显示加载中行
     */
    void displayLoadingRow(boolean displayLoadingRow) {
        this.displayLoadingRow = displayLoadingRow;
    }

    /**
     * 显示没有更多数据行
     */
    void displayNoMoreDataRow(boolean displayNoMoreDataRow) {
        this.displayNoMoreDataRow = displayNoMoreDataRow;
    }

    /**
     * 显示加载失败行
     */
    void displayRetryRow(boolean displayRetryRow) {
        this.displayRetryRow = displayRetryRow;
    }

}
