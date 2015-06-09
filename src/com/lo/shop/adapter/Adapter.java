package com.lo.shop.adapter;



import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public abstract class Adapter extends BaseAdapter
{
    public class BeeCellHolder
    {
        public int position;
    }
    protected LayoutInflater mInflater = null;

    protected Context mContext;
    public ArrayList dataList = new ArrayList();
    protected static final int TYPE_ITEM = 0;
    protected static final int TYPE_FOOTER = 1;

    protected static final int TYPE_HEADER = 2;

    public Adapter(Context c, ArrayList dataList)
    {
        mContext = c;
        mInflater = LayoutInflater.from(c);
        this.dataList = dataList;
    }

    protected abstract View bindData(int position, View cellView, ViewGroup parent, BeeCellHolder h);
    protected abstract BeeCellHolder createCellHolder(View cellView);
    public abstract View createCellView();

    protected View dequeueReuseableCellView(int position, View convertView,
                                            ViewGroup parent) {
        return null;
    }

    /* (non-Javadoc)
     * @see com.miyoo.lottery.adapter.HummerBaseAdapter#getCount()
     */
    @Override
    public int getCount() {
        int count = dataList != null?dataList.size():0;
        return count;
    }

    /* (non-Javadoc)
     * @see com.miyoo.lottery.adapter.HummerBaseAdapter#getItem(int)
     */
    @Override
    public Object getItem(int position) {
        if (0 <= position && position < getCount()) {
            return dataList.get(position);
        }
        else {
            return null;
        }
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int position) {
         
        return 0;
    }

    public int getItemViewType(int position)
    {
        return TYPE_ITEM;
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View cellView, ViewGroup parent) {

        BeeCellHolder holder = null;
        if (cellView == null ) {
            cellView = createCellView();
            holder = createCellHolder(cellView);
            if (null != holder) 
            {
            	cellView.setTag(holder);
			}
            
        }
        else {
            holder = (BeeCellHolder)cellView.getTag();
            if (holder == null)
            {
                Log.v("error", "error");
            }
        }
        if(null != holder)
        {
        	holder.position = position;
        }
        
        bindData(position, cellView, parent, holder);
        return cellView;
    }

    /* (non-Javadoc)
     * @see android.widget.BaseAdapter#getViewTypeCount()
     */
    @Override
    public int getViewTypeCount() {
        return 1;
    }


    public void update(int newState)
    {
        notifyDataSetInvalidated();
    }
}
