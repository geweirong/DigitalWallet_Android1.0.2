package com.innext.szqb.ui.authentication.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.innext.szqb.R;
import com.innext.szqb.base.BaseFragment;
import com.innext.szqb.ui.authentication.activity.GDMapActivity;
import com.innext.szqb.ui.authentication.adapter.PoiSearchAdapter;
import com.innext.szqb.util.check.StringUtil;
import com.innext.szqb.widget.recycler.BaseRecyclerAdapter;
import com.innext.szqb.widget.recycler.RecycleViewDivider;
import com.innext.szqb.widget.refresh.base.OnLoadMoreListener;
import com.innext.szqb.widget.refresh.base.OnRefreshListener;
import com.innext.szqb.widget.refresh.base.SwipeToLoadLayout;

import butterknife.BindView;

public class GDSearchFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {

    private static GDSearchFragment fragment;
    @BindView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @BindView(R.id.refresh)
    SwipeToLoadLayout mRefresh;

    private PoiSearchAdapter adapter;
    private int page = 0;
    private int pageCount = 20;
    private int pageSize = 15;

    private String cityCode = "";
    private String keyword = "";

    private GDMapActivity gdMapActivity;

    public static GDSearchFragment getInstance(String cityCode) {
        if (fragment == null) {
            fragment = new GDSearchFragment();
            fragment.cityCode = cityCode;
        }
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_gd_search;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        mRefresh.setOnRefreshListener(this);
        mRefresh.setOnLoadMoreListener(this);
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(mContext));
        mSwipeTarget.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL));
        adapter = new PoiSearchAdapter();
        mSwipeTarget.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClick() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("result", adapter.getData().get(position));
                mActivity.setResult(mActivity.RESULT_OK, intent);
                mActivity.finish();
            }
        });
        autoLoading(mRefresh, mSwipeTarget);
    }

    public void poiSearch(final int pageNum, String key) {
//		POI搜索类型共分为以下20种：汽车服务|汽车销售|汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施
//		餐饮服务|商务住宅|生活服务
        page = pageNum;
        if (StringUtil.isBlank(key)) {
            onComplete(mRefresh);
            return;
        }
        keyword = key;
        PoiSearch.Query query = new PoiSearch.Query(key, "商务住宅|餐饮服务|生活服务", cityCode);
        query.setPageSize(pageSize);
        query.setPageNum(pageNum);
        PoiSearch poiSearch = new PoiSearch(mActivity, query);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                refreshData(pageNum, poiResult);
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {
            }
        });
        poiSearch.searchPOIAsyn();
    }

    public void refreshData(int pageNum, PoiResult poiResult) {
        onComplete(mRefresh);
        pageCount = poiResult.getPageCount();
        if (pageNum == 0) {
            mSwipeTarget.scrollToPosition(0);
            adapter.clearData();
        }
        adapter.addData(poiResult.getPois());
        if (pageNum < pageCount - 1) {
            mRefresh.setLoadMoreEnabled(true);
        } else {
            mRefresh.setLoadMoreEnabled(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragment = null;
    }

    @Override
    public void onLoadMore() {
        //上啦加载
        page = page + 1;
        poiSearch(page, keyword);
    }

    @Override
    public void onRefresh() {
        //下啦刷新
        page = 0;
        pageCount = 20;
        poiSearch(page, keyword);
    }
    public void loadSearchData(int pageNum, String key){
        page = pageNum;
        keyword = key;
        mRefresh.post(new Runnable() {
            @Override
            public void run() {
                mRefresh.setRefreshing(true);
            }
        });
    }
}
