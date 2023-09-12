package com.yxr.base.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yxr.base.R;
import com.yxr.base.http.BaseUrlReplaceConfig;
import com.yxr.base.http.manager.HttpManager;
import com.yxr.base.http.util.HttpUtil;
import com.yxr.base.util.MMKVUtil;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class BaseUrlReplaceEditActivity extends AppCompatActivity {
    private static final String SP_URL_CHANGED_HISTORY = "urlChangedHistory";
    private EditText etBaseUrl;
    private RecyclerView recyclerView;
    private RecyclerView rvHistory;
    private Button btnSave;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> historyAdapter;
    private List<String> historyList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_url_replace);

        initView();
        initListener();
        initData();
    }

    private void initView() {
        etBaseUrl = findViewById(R.id.etBaseUrl);
        recyclerView = findViewById(R.id.recyclerView);
        rvHistory = findViewById(R.id.rvHistory);
        btnSave = findViewById(R.id.btnSave);
    }

    private void initListener() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBaseUrl();
            }
        });
    }

    private void initData() {
        BaseUrlReplaceConfig replaceConfig = HttpManager.get().getHttpConfig().getBaseUrlReplaceConfig();
        etBaseUrl.setText(replaceConfig == null ? null : replaceConfig.getBaseUrl());

        List<String> urlList = new ArrayList<>();
        if (replaceConfig != null && replaceConfig.getBaseUrlList().size() > 0) {
            urlList.addAll(replaceConfig.getBaseUrlList());
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return new RecyclerView.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_base_url_replace, null, false)) {
                };
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") int i) {
                TextView tvUrl = viewHolder.itemView.findViewById(R.id.tvUrl);
                if (tvUrl != null) {
                    final String url = urlList.get(i);
                    tvUrl.setText(url);
                    tvUrl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            etBaseUrl.setText(url);
                        }
                    });
                }
            }

            @Override
            public int getItemCount() {
                return urlList.size();
            }
        });

        historyList.addAll(getHistories());
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        historyAdapter = new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return new RecyclerView.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_base_url_replace, null, false)) {
                };
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") int i) {
                TextView tvUrl = viewHolder.itemView.findViewById(R.id.tvUrl);
                if (tvUrl != null) {
                    final String url = historyList.get(i);
                    tvUrl.setText(url);
                    tvUrl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            etBaseUrl.setText(url);
                        }
                    });
                }
            }

            @Override
            public int getItemCount() {
                return historyList.size();
            }
        };
        rvHistory.setAdapter(historyAdapter);
    }

    private void saveBaseUrl() {
        String baseUrl = etBaseUrl.getText().toString();
        if (HttpUtil.isUrl(baseUrl)) {
            HttpManager.get().updateDebugUrl(baseUrl);
            addHistory();
            toast("保存成功");
        } else {
            toast("这不是个正确的地址");
        }
    }

    private void toast(String message) {
        try {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private List<String> getHistories() {
        List<String> list = new ArrayList<>();
        String json = MMKVUtil.getString(SP_URL_CHANGED_HISTORY, null);
        if (json != null) {
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    list.add(jsonArray.optString(i));
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addHistory() {
        String url = etBaseUrl.getText().toString().trim();
        historyList.remove(url);
        historyList.add(0, url);
        if (historyList.size() > 10) {
            historyList.remove(historyList.size() - 1);
        }
        updateHistory();
        historyAdapter.notifyDataSetChanged();
    }

    private void updateHistory() {
        try {
            JSONArray jsonArray = new JSONArray(historyList);
            MMKVUtil.putString(SP_URL_CHANGED_HISTORY, jsonArray.toString());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}