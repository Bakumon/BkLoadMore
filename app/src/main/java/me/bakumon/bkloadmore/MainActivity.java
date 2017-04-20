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

package me.bakumon.bkloadmore;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.bakumon.bkloadmore.adapter.MyAdapter;
import me.bakumon.library.bkloadmore.BKLoadMore;
import me.bakumon.library.bkloadmore.BKLoadMoreImpl;

public class MainActivity extends AppCompatActivity implements BKLoadMoreImpl.Callbacks {

    private MyAdapter adapter;

    private BKLoadMore mBKLoadMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        handler = new Handler();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("text" + i);
        }
        adapter = new MyAdapter(this, list);
        recyclerView.setAdapter(adapter);

        mBKLoadMore = BKLoadMoreImpl.with(recyclerView, this).build();

    }

    private Handler handler;

    @Override
    public void onLoadMore() {
        handler.postDelayed(fakeCallback, 1500);
    }

    @Override
    public void onRetry() {
        handler.postDelayed(retryCallback, 1500);
    }

    private int page = 0;
    private Runnable fakeCallback = new Runnable() {
        @Override
        public void run() {
            page++;
            if (page != 3) {
                int start = adapter.mData.size();
                adapter.mData.add("add" + page);
                adapter.mData.add("add" + page);
                adapter.mData.add("add" + page);
                adapter.notifyItemRangeInserted(start, 3);
            } else {
                mBKLoadMore.loadMoreFail();
            }
            mBKLoadMore.completedLoadMore();
            mBKLoadMore.setIsLastPage(page == 5);

        }
    };
    private Runnable retryCallback = new Runnable() {
        @Override
        public void run() {
            int start = adapter.mData.size();
            adapter.mData.add("add" + page);
            adapter.mData.add("add" + page);
            adapter.mData.add("add" + page);
            adapter.notifyItemRangeInserted(start, 3);
            mBKLoadMore.completedLoadMore();
        }
    };
}
