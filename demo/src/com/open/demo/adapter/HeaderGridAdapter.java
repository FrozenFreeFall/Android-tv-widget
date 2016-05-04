package com.open.demo.adapter;

import java.util.ArrayList;
import java.util.List;

import com.open.demo.R;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HeaderGridAdapter extends RecyclerView.Adapter<GridViewHolder> {

	private static final int ITEM_VIEW_TYPE_HEADER = 0; // 头
	private static final int ITEM_VIEW_TYPE_ITEM = 1; // item.

	private final List<String> labels;

	public HeaderGridAdapter(int count) {
		this.labels = new ArrayList<String>(count);
		for (int i = 0; i < count; i++) {
			labels.add(String.valueOf(i));
		}
	}

	@Override
	public int getItemCount() {
		return labels.size() + 1;
	}
	
	/**
	 * 判断是否为Header.
	 */
	public boolean isHeader(int position) {
		return (position %12)== 0;
	}

	@Override
	public void onBindViewHolder(GridViewHolder holder, int position) {
		if (isHeader(position)) {
			return;
		}
		// holder.head_tv
	}

	@Override
	public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType == ITEM_VIEW_TYPE_HEADER) {
			View headview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header_view, parent, false);
			// return new HeaderHolder(headview);
			return new GridViewHolder(headview);
		}
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_view, parent, false);
		return new GridViewHolder(view);
	}

	@Override
	public int getItemViewType(int position) {
		return isHeader(position) ? ITEM_VIEW_TYPE_HEADER : ITEM_VIEW_TYPE_ITEM;
	}

}
