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

/**
 * BKLoadMore
 * Created by Bakumon on 2017/4/19 16:12.
 */
public abstract class BKLoadMore {
    public interface Callbacks {

        /**
         * Called when next page of data needs to be loaded.
         */
        void onLoadMore();

        /**
         * Called when the next page of data needs to be reloaded.
         */
        void onRetry();
    }

    /**
     * Create load more functionality upon RecyclerView.
     *
     * @param recyclerView RecyclerView that will have load more functionality.
     * @param callback     load more callbacks
     * @return {@link me.bakumon.library.bkloadmore.BKLoadMore.Callbacks}
     */
    public static BKLoadMoreImpl.Builder with(RecyclerView recyclerView, Callbacks callback) {
        return new BKLoadMoreImpl.Builder(recyclerView, callback);
    }

    /**
     * Called to check if there is more data (more pages) to load.
     *
     * @param isLastPage Whether it is the last page.
     */
    public abstract void setIsLastPage(boolean isLastPage);

    /**
     * This method is called when the data has been loaded.
     */
    public abstract void completedLoadMore();

    /**
     * This method is called when data loading fails.
     */
    public abstract void loadMoreFail();
}
