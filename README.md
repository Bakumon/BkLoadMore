
# BKLoadMore(non-invasive)

Modified from [Paginate](https://github.com/MarkoMilos/Paginate), the extension is in addition to displaying the rows that have finished loading all the data.

## Features

- API is simple and flexible
- No immersion implementation
- Custom loading & no more data list item
- Support RecyclerView (using linear, grid and staggered LayoutManager)

## Preview

![BKLoadMore.gif](https://github.com/Bakumon/BkLoadMore/raw/master/art/demo.gif)

## Setup

TODO JCenter

## Usage

Use the default implementation to start quickly.

```java
BKLoadMore mBKLoadMore = BKLoadMoreImpl.with(recyclerView, this).build();
```

Implement the CallBacks interface to control paging action.

```java
@Override
public void onLoadMore() {
    // Called when next page of data needs to be loaded.
    handler.postDelayed(fakeCallback, 1500);
}

@Override
public void onRetry() {
    // Called when the next page of data needs to be reloaded.
    handler.postDelayed(retryCallback, 1500);
}
```

Call the following method at the appropriate time, see the note for details.

```java
// Called to check if there is more data (more pages) to load.
mBKLoadMore.setIsLastPage(isLastPage);

// This method is called when the data has been loaded.
mBKLoadMore.completedLoadMore();

// This method is called when data loading fails.It will show the load failed line.
mBKLoadMore.loadMoreFail();
```

**Note:** `LayoutManager` and `RecyclerView.Adapter` needs to be set before calling the code above.

## Customize

```java
mBKLoadMore = BKLoadMoreImpl.with(recyclerView, this)
                            .setLoadingItem(new CustomerLoadingItem())
                            .setNoMoreDataItem(new CustomerNoMoreDataItem())
                            .build();
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

For details, please refer to [demo](https://github.com/Bakumon/BkLoadMore/tree/master/app).

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
