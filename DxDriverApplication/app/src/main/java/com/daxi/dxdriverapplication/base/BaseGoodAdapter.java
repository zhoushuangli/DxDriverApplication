package com.daxi.dxdriverapplication.base;


import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseGoodAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements View.OnClickListener {

    private BaseGoodAdapter.OnItemClickListener onItemClickListener;
    private OnItemSelectListener onItemSelectListener;
    private OnItemMultiSelectListener onItemMultiSelectListener;
    private SelectMode selectMode;

    public void setOnItemClickListener(BaseGoodAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    public void setOnItemMultiSelectListener(OnItemMultiSelectListener onItemMultiSelectListener) {
        this.onItemMultiSelectListener = onItemMultiSelectListener;
    }

    public abstract void whenBindViewHolder(VH holder, int position);

    private int singleSelected = 0; // 默认为第一个被选中
    private List<Integer> multiSelected = new ArrayList<>();
    private int maxSelectedCount = -1;

    @NonNull
    @Override
    public abstract VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(VH holder, int position) {
        whenBindViewHolder(holder, position);
        holder.itemView.setTag(position);

        holder.itemView.setOnClickListener(this);

        if (selectMode == SelectMode.CLICK) { //点击
            holder.itemView.setSelected(false);
        } else if (selectMode == SelectMode.SINGLE_SELECT) { //单选
            if (singleSelected == position) {
                holder.itemView.setSelected(true);
            } else {
                holder.itemView.setSelected(false);
            }
        } else if (selectMode == SelectMode.MULTI_SELECT) {//多选
            if (multiSelected.contains(position)) {
                holder.itemView.setSelected(true);
            } else {
                holder.itemView.setSelected(false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int itemPosition = (int) v.getTag();
        if (selectMode == SelectMode.CLICK) {//点击模式
            if (onItemClickListener != null) {
                onItemClickListener.onClicked(itemPosition, v);
            }
        } else if (selectMode == SelectMode.SINGLE_SELECT) { //单选模式
            singleSelected = itemPosition;
            if (onItemSelectListener != null) {
                onItemSelectListener.onSelected(itemPosition, v);
            }
            notifyDataSetChanged();//通知刷新
        } else if (selectMode == SelectMode.MULTI_SELECT) {//多选模式
            if (maxSelectedCount <= 0  //选择不受限制
                    || multiSelected.size() < maxSelectedCount) {  // 选择个数需要小于最大可选数
                if (multiSelected.contains(itemPosition)) {
                    multiSelected.remove((Object) itemPosition);
                } else {
                    multiSelected.add(itemPosition);
                }
                if (onItemMultiSelectListener != null) {
                    onItemMultiSelectListener.onMultiSelected(itemPosition);
                }
            } else if (multiSelected.size() == maxSelectedCount && multiSelected.contains(itemPosition)) { //当等于最大数量并且点击的item包含在已选中 可清除
                multiSelected.remove((Object) itemPosition);
            }
            notifyDataSetChanged();
        }
    }

    /**
     * 设置选择模式
     *
     * @param selectMode
     */

    public void setSelectMode(BaseGoodAdapter.SelectMode selectMode) {
        this.selectMode = selectMode;
        notifyDataSetChanged();
    }

    /**
     * 设置默认选中项，一个或多个
     *
     * @param itemPositions
     */

    public void setSelected(int... itemPositions) {
        multiSelected.clear();
        if (selectMode == BaseGoodAdapter.SelectMode.SINGLE_SELECT) {
            singleSelected = itemPositions[0];
        } else {
            for (int itemPosition : itemPositions) {
                multiSelected.add(itemPosition);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 清除选择项，只有在MULT_SELECT模式下有效
     */
    public void clearSelected() { // 这个方法只在多选模式下生效

        if (selectMode == BaseGoodAdapter.SelectMode.MULTI_SELECT) {
            multiSelected.clear();
        }
        notifyDataSetChanged();
    }

    /**
     * 获取单选项位置
     */
    public int getSingleSelectedPosition() {
        return singleSelected;
    }

    /**
     * 获取多选项位置
     */
    public List<Integer> getMultSelectedPosition() {
        return multiSelected;
    }

    /**
     * 设置最大可选数量，
     *
     * @param maxSelectedCount maxSelectedCount <= 0 表示不限制选择数
     */
    public void setMaxSelectedCount(int maxSelectedCount) {
        if (maxSelectedCount < multiSelected.size()) {
            multiSelected.clear();
        }
        this.maxSelectedCount = maxSelectedCount;
        notifyDataSetChanged();
    }

    /**
     * 选择全部，仅在maxSelectedCount <= 0 不限制选择数时有效
     */
    public void selectAll() {
        if (maxSelectedCount <= 0) {
            multiSelected.clear();
            for (int i = 0; i < getItemCount(); i++) {
                multiSelected.add(i);
            }
            notifyDataSetChanged();
        }
    }

    /**
     * 反选全部,仅在maxSelectedCount <= 0 不限制选择数时有效
     */
    public void reverseSelected() {
        if (maxSelectedCount <= 0) {
            for (int i = 0; i < getItemCount(); i++) {
                if (multiSelected.contains(i)) {
                    multiSelected.remove((Object) i);
                } else {
                    multiSelected.add(i);
                }
            }
            notifyDataSetChanged();
        }
    }

    public interface OnItemClickListener {
        void onClicked(int itemPosition, View view);
    }

    public interface OnItemSelectListener {
        void onSelected(int itemPosition, View view);
    }

    public interface OnItemMultiSelectListener {
        void onMultiSelected(int itemPosition);
    }

    public enum SelectMode {
        CLICK, SINGLE_SELECT, MULTI_SELECT
    }
}
