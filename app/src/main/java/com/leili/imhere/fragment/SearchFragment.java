package com.leili.imhere.fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.leili.imhere.R;
import com.leili.imhere.database.DatabaseHelper;
import com.leili.imhere.entity.ILikePosition;
import com.leili.imhere.entity.PositionAdapter;
import com.leili.imhere.entity.Position;
import com.leili.imhere.event.Event;
import com.leili.imhere.utils.ViewUtils;
import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.BaseObject;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.param.SearchParam;
import com.tencent.lbssearch.object.result.SearchResultObject;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Lei.Li on 7/23/15 8:40 PM.
 */
public class SearchFragment extends Fragment implements ILikePosition {
    private static final String TAG = SearchFragment.class.getSimpleName();
    private static final String NO_RESULT = "无匹配结果";
    public static final int DEFAULT_PAGE_SIZE = 10;
    // views
    private EditText etSearchInput;
    private View btnClearInput;
    private Button btnSearch;
    private ListView lvSearchResults;
    // data
    private List<Position> positions = new ArrayList<>();
    // adapter
    private PositionAdapter positionAdapter;
    // dialog
    Dialog dialog;
    // listeners
    private View.OnClickListener searchListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ViewUtils.hideKeyboard(getActivity());
            positions.clear();
            showLoadingDialog();
            searchKeyword(etSearchInput.getText().toString(), DEFAULT_PAGE_SIZE, 0);
        }
    };
    private View.OnClickListener clearInputListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            etSearchInput.setText("");
        }
    };
    // database
    private DatabaseHelper databaseHelper;

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
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        positionAdapter = new PositionAdapter(getActivity(), positions, this);
        lvSearchResults.setAdapter(positionAdapter);
        lvSearchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventBus.getDefault().post(new Event.LocatePositionEvent(positions.get(position)));
            }
        });
        databaseHelper = new DatabaseHelper(getActivity());
    }

    @Override
    public void likePosition(Position position) {
        // insert into DB
        ViewUtils.toast(getActivity(), "like " + position.getTitle());
        databaseHelper.insertPosition(position.getTencentId(), position.getTitle(), position.getAddress(),
                position.getLatitude(), position.getLongitude());
        EventBus.getDefault().post(new Event.LikeEvent());
    }

    @Override
    public void loadMore(int offset, int pageSize) {
        showLoadingDialog();
        searchKeyword(etSearchInput.getText().toString(), pageSize, offset / DEFAULT_PAGE_SIZE + 1);
    }

    private void showLoadingDialog() {
        if (dialog == null) {
            dialog = new ProgressDialog(getActivity());
            dialog.setTitle("正在搜索");
            ((ProgressDialog) dialog).setMessage("请稍候...");
        }
        dialog.show();
    }

    private void searchKeyword(final String keyword, final int pageSize, final int pageIndex) {

        TencentSearch tencentSearch = new TencentSearch(getActivity());
        SearchParam.Region r = new SearchParam.Region().poi("上海");
//		Nearby n = new Nearby().point(new Location().lat(39.962386f).lng(116.358948f)).r(1000);
        SearchParam searchParam = new SearchParam().boundary(r).page_size(pageSize).page_index(pageIndex);
        searchParam.keyword(keyword);
//		object.filter("美食");
        tencentSearch.search(searchParam, new HttpResponseListener() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, BaseObject object) {
                dialog.dismiss();
                if (object != null) {
                    SearchResultObject searchResultObject = (SearchResultObject) object;
                    if (searchResultObject.data != null) {
                        for (SearchResultObject.SearchResultData data : searchResultObject.data) {
                            positions.add(Position.from(data));
                        }
                    } else
                        ViewUtils.toast(getActivity(), NO_RESULT);
                } else {
                    ViewUtils.toast(getActivity(), NO_RESULT);
                }
                positionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseString, Throwable throwable) {
                dialog.dismiss();
                ViewUtils.toast(getActivity(), responseString);
            }
        });

    }
}
