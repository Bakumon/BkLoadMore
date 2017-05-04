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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.bakumon.library.R;

/**
 * DefaultRetryItem
 * Created by Bakumon on 2017/4/19 13:53.
 */
class DefaultRetryItem extends RetryItem {

    private BKLoadMore.Callbacks mCallbacks;

    DefaultRetryItem(BKLoadMore.Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retry_row, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NoDoubleClickUtils.isDoubleClick()) {
                    if (mCallbacks != null) {
                        mCallbacks.onRetry();
                    }
                    if (listener != null) {
                        listener.onRetryItemClick();
                    }
                }
            }
        });
    }

}
