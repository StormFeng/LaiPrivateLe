/*
Copyright 2015 shizhefei（LuckyJayce）

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.lailem.app.widget.pulltorefresh.helper;


import java.util.ArrayList;

/**
 * 数据源
 *
 */
public interface IDataSource<Model> {
    /**
     * 获取刷新的数据
     *
     * @return
     * @throws Exception
     */
    public ArrayList<Model> refresh() throws Exception;

    /**
     * 获取加载更多的数据
     *
     * @return
     * @throws Exception
     */
    public ArrayList<Model> loadMore() throws Exception;

    /**
     * 是否还可以继续加载更多
     *
     * @return
     */
    public boolean hasMore();

    /**
     * 获得数据集合
     */
    public ArrayList<Model> getResultList();

    /**
     * 是否下拉添加新数据到顶部
     */
    public boolean isPullDownLoadMore();

    public int getRefreshInsertIndex();

}
