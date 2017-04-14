
# BKLoadMore

## Features

- API is simple and flexible
- Custom loading & no more data list item
- Support RecyclerView (using linear, grid and staggered LayoutManager)

## Preview

![BKLoadMore.gif](https://github.com/Bakumon/BkLoadMore/raw/master/art/BKLoadMore.gif)

## Setup

TODO JCenter

## Usage

Use the default implementation to start quickly

```java
BKLoadMore.with(mRecyclerView)
          .callBack(this);

@Override
public void onLoadMore() {
    // Load next page of data (e.g. network or database)
}

@Override
public boolean isLoading() {
    // Indicate whether new page loading is in progress or not
    return isLoading;
}

@Override
public boolean isLastPage() {
    // Indicate whether all data (pages) are loaded or not
    return isLastPage;
}

```

**Note:** `LayoutManager` and `RecyclerView.Adapter` needs to be set before calling the code above.

## Customize

```java
BKLoadMore.with(mRecyclerView)
          .setLoadingItem(new CustomerLoadingItem())
          .setNoMoreDataItem(new CustomerNoMoreDataItem())
          .callBack(this);
```

Implement the LoadingItem interface to customize the loading line.

`CustomerLoadingItem.java`

```java
public class CustomerLoadingItem implements LoadingItem {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_row_customer, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
}
```

Implement the NoMoreDataItem interface to customize the showing no more data line.

`CustomerNoMoreDataItem.java`

```java
public class CustomerNoMoreDataItem implements NoMoreDataItem {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.no_more_data_row_customer, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
}
```

## About me

- E-mail：bakumon@aliyun.com

- Blog: https://www.bakumon.me/

- Github: https://github.com/Bakumon

## License

```
Copyright 2017 bakumon@aliyun.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```