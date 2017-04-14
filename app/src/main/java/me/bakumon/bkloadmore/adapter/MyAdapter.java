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

package me.bakumon.bkloadmore.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import me.bakumon.bkloadmore.R;


/**
 * MyAdapter
 * Created by bakumon on 2016/10/13.
 */

public class MyAdapter extends CommonAdapter4RecyclerView<String> implements ListenerWithPosition.OnClickWithPositionListener<CommonHolder4RecyclerView> {

    public MyAdapter(Context context, List<String> list) {
        super(context, list, R.layout.item);
    }

    @Override
    public void convert(CommonHolder4RecyclerView holder, String data) {
        holder.setTextViewText(R.id.text, data);
        holder.setOnClickListener(this, R.id.item);
    }


    @Override
    public void onClick(View v, int position, CommonHolder4RecyclerView holder) {
        Toast.makeText(mContext, "position = " + position, Toast.LENGTH_SHORT).show();
    }
}
