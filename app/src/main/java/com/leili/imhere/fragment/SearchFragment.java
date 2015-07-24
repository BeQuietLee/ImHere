package com.leili.imhere.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.leili.imhere.R;
import com.leili.imhere.entity.SearchResultAdapter;
import com.leili.imhere.entity.SearchResultItem;
import com.leili.imhere.utils.ViewUtils;
import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.BaseObject;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.param.SearchParam;
import com.tencent.lbssearch.object.result.SearchResultObject;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lei.Li on 7/23/15 8:40 PM.
 */
public class SearchFragment extends Fragment {
    private static final String TAG = SearchFragment.class.getSimpleName();
    private static final String NO_RESULT = "无匹配结果";
    // views
    private EditText etSearchInput;
    private View btnClearInput;
    private Button btnSearch;
    private ListView lvSearchResults;
    // data
    private List<SearchResultItem> searchResultItems = new ArrayList<>();
    // adapter
    private SearchResultAdapter searchResultAdapter;
    // listeners
    private View.OnClickListener searchListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            searchKeyword(etSearchInput.getText().toString());
        }
    };

    private View.OnClickListener clearInputListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            etSearchInput.setText("");
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View rootView = getView();
        etSearchInput = (EditText) rootView.findViewById(R.id.search_input_et);
        btnClearInput = rootView.findViewById(R.id.clear_input);
        btnClearInput.setOnClickListener(clearInputListener);
        btnSearch = (Button) rootView.findViewById(R.id.search_btn);
        btnSearch.setOnClickListener(searchListener);
        lvSearchResults = (ListView) rootView.findViewById(R.id.search_results_lv);
        ViewUtils.toast(getActivity(), "目前仅支持搜索上海地区内的目标");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        searchResultAdapter = new SearchResultAdapter(getActivity(), searchResultItems);
        lvSearchResults.setAdapter(searchResultAdapter);
    }

    private void searchKeyword(final String keyword) {

        TencentSearch tencentSearch = new TencentSearch(getActivity());
        SearchParam.Region r = new SearchParam.Region().poi("上海");
//		Nearby n = new Nearby().point(new Location().lat(39.962386f).lng(116.358948f)).r(1000);
        SearchParam searchParam = new SearchParam().boundary(r).page_size(20);
        searchParam.keyword(keyword);
//		object.filter("美食");
        tencentSearch.search(searchParam, new HttpResponseListener() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, BaseObject object) {
                searchResultItems.clear();
                if (object != null) {
                    SearchResultObject searchResultObject = (SearchResultObject) object;
                    if (searchResultObject.data != null) {
                        for (SearchResultObject.SearchResultData data : searchResultObject.data) {
                            searchResultItems.add(SearchResultItem.from(data));
                        }
                    } else
                        ViewUtils.toast(getActivity(), NO_RESULT);
                } else {
                    ViewUtils.toast(getActivity(), NO_RESULT);
                }
                searchResultAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseString, Throwable throwable) {
                ViewUtils.toast(getActivity(), responseString);
            }
        });

    }
}
