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
 * 正在加载中的 Item,并且提供了默认的实现
 * Created by Bakumon on 2017/4/13 17:00.
 */
public abstract class RetryItem {

    abstract RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    abstract void onBindViewHolder(RecyclerView.ViewHolder holder, int position);

    protected OnRetryItemClickListener listener;

    void setOnRetryItemClickListener(OnRetryItemClickListener listener){
        this.listener = listener;
    }

    interface OnRetryItemClickListener {
        void onRetryItemClick();
    }

}
