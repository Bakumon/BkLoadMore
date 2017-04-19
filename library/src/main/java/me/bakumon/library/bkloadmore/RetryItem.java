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
 * Create a line that indicates that it is failed to load.
 * Created by Bakumon on 2017/4/13 17:00.
 */
public abstract class RetryItem {
    /**
     * Create new loading list item {@link android.support.v7.widget.RecyclerView.ViewHolder}.
     *
     * @param parent   parent ViewGroup.
     * @param viewType viewType type of the loading list item.
     * @return ViewHolder that will be used as loading list item.
     */
    public abstract RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    /**
     * Bind the loading list item.
     *
     * @param holder   loading list item ViewHolder.
     * @param position position loading list item position.
     */
    public abstract void onBindViewHolder(RecyclerView.ViewHolder holder, int position);

    /**
     * Listener used to dispatch the retry item click events.
     */
    public OnRetryItemClickListener listener;

    /**
     * Register a callback to be invoked when this the retry item is clicked.
     *
     * @param listener The callback that will run
     */
    public void setOnRetryItemClickListener(OnRetryItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Interface definition for a callback to be invoked when the retry item is clicked.
     */
    public interface OnRetryItemClickListener {
        void onRetryItemClick();
    }

}
