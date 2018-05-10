package com.innext.szqb.ui.authentication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.innext.szqb.R;
import com.innext.szqb.base.BaseActivity;
import com.innext.szqb.events.MapResultEvent;
import com.innext.szqb.ui.authentication.adapter.PoiSearchAdapter;
import com.innext.szqb.util.Tool;
import com.innext.szqb.widget.recycler.BaseRecyclerAdapter;
import com.innext.szqb.widget.recycler.RecycleViewDivider;
import com.innext.szqb.widget.refresh.base.OnLoadMoreListener;
import com.innext.szqb.widget.refresh.base.OnRefreshListener;
import com.innext.szqb.widget.refresh.base.SwipeToLoadLayout;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 地图
 * hengxinyongli
 */
public class GDMapActivity extends BaseActivity implements LocationSource,
        AMapLocationListener, AMap.OnCameraChangeListener
,OnRefreshListener,OnLoadMoreListener{

    public static final int GET_POI_REQUEST_CODE = 1001;
    /** 第一次定位请求码 */
    public static final int GET_POI_FIRST_REQUEST_CODE = 1002;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_search)
    TextView mTvSearch;
    @BindView(R.id.map)
    MapView mMap;
    @BindView(R.id.iv_location)
    ImageView mIvLocation;
    @BindView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @BindView(R.id.refresh)
    SwipeToLoadLayout mRefresh;
    private AMap aMap;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    private PoiSearch.SearchBound searchBound;

    private PoiSearchAdapter adapter;
    private int page = 0;
    private int pageCount = 20;
    private int pageSize = 15;

    private String cityCode = "";


    @Override
    public int getLayoutId() {
        return R.layout.activity_gd_map;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        initView();
    }

    public void initView() {
        mRefresh.setOnRefreshListener(this);
        mRefresh.setOnLoadMoreListener(this);
        adapter = new PoiSearchAdapter();
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(this));
        mSwipeTarget.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL));
        mSwipeTarget.setAdapter(adapter);
        mMap.onCreate(null);// 此方法必须重写
        if (aMap == null) {
            aMap = mMap.getMap();
            setUpMap();
        }
        aMap.setOnCameraChangeListener(this);
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClick() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("result", adapter.getData().get(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        autoLoading(mRefresh,mSwipeTarget);
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_location_marker));// 设置小蓝点的图标
        myLocationStyle.radiusFillColor(ContextCompat.getColor(mContext,R.color.amap_transparent_theme_color));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeColor(ContextCompat.getColor(mContext,R.color.amap_transparent_theme_color));
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        aMap.moveCamera(CameraUpdateFactory.zoomTo(19));
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.getUiSettings().setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_RIGHT);
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
    }

    /** 是否是第一次定位 */
    private boolean isFirstLocation = true;
    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                mIvLocation.setVisibility(View.VISIBLE);
                cityCode = amapLocation.getCityCode();
                if (isFirstLocation) {
                    double longitude = amapLocation.getLongitude();
                    double latitude = amapLocation.getLatitude();
                    EventBus.getDefault().post(new MapResultEvent(longitude, latitude));
                    isFirstLocation = false;
                }
                Log.e("Amap","定位成功," +  amapLocation.toStr());
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("Amap", errText);
            }
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMap.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMap.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMap.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMap.onDestroy();
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        searchBound = new PoiSearch.SearchBound(new LatLonPoint(cameraPosition.target.latitude, cameraPosition.target.longitude), 1000, true);
        page = 0;
        pageCount = 20;
        poiSearch(page);
    }

    protected void poiSearch(final int pageNum) {
//		POI搜索类型共分为以下20种：汽车服务|汽车销售|汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施
//		餐饮服务|商务住宅|生活服务
        PoiSearch.Query query = new PoiSearch.Query("", "商务住宅|餐饮服务|生活服务");
        query.setPageSize(pageSize);
        query.setPageNum(pageNum);
        PoiSearch poiSearch = new PoiSearch(this, query);
        poiSearch.setBound(searchBound);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
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
            public void onPoiItemSearched(PoiItem poiItem, int i) {
            }
        });
        poiSearch.searchPOIAsyn();
    }

    @OnClick({R.id.iv_back, R.id.tv_search,R.id.iv_location})
    public void onClick(View view) {
        if (Tool.isFastDoubleClick())
            return;
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_search:
                Intent intent=new Intent(this,GDMapSearchActivity.class);
                intent.putExtra("cityCode",cityCode);
                startActivityForResult(intent,GDMapSearchActivity.SEARCH_REQUEST_CODE);
                break;
            case R.id.iv_location:
                if (mlocationClient != null) {
                    AMapLocation aMapLocation = mlocationClient.getLastKnownLocation();
                    if (aMapLocation!=null){
                        aMap.animateCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GDMapSearchActivity.SEARCH_REQUEST_CODE&&resultCode == RESULT_OK ){
            Intent intent = new Intent();
            intent.putExtra("result", data.getParcelableExtra("result"));
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onLoadMore() {
        //上啦加载
        page = page + 1;
        poiSearch(page);
    }

    @Override
    public void onRefresh() {
        //下啦刷新
        page = 0;
        pageCount = 20;
        poiSearch(page);
    }
}
