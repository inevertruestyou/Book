package activitytest.example.com.book;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by wjm19 on 2016/12/6.
 */
public class sheetAdapter extends RecyclerView.Adapter {
    private List<entity> list;

    private OnItemClickListener mOnItemClickListener;

    public sheetAdapter(List<entity> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sheet_item_layout, parent, false);
        return new sheetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final sheetViewHolder vh = (sheetViewHolder) holder;

        vh.getTv_sheetRow1().setText(list.get(position).getSheetRow1());
        vh.getTv_sheetRow2().setText(list.get(position).getSheetRow2());
        vh.getTv_sheetRow3().setText(list.get(position).getSheetRow3());
        vh.getTv_sheetRow4().setText(list.get(position).getSheetRow4());

        //判断是否设置了监听器
        if(mOnItemClickListener != null){
            //为ItemView设置监听器
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = vh.getLayoutPosition(); // 1
                    mOnItemClickListener.onItemClick(vh.itemView,position); // 2
                }
            });
        }
    }





    @Override
    public int getItemCount() {
        return list.size();
    }



public class sheetViewHolder extends RecyclerView.ViewHolder{
        public final View mView;
        public final TextView tv_sheetRow1;
        public final TextView tv_sheetRow2;
        public final TextView tv_sheetRow3;
        public final TextView tv_sheetRow4;

        public sheetViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            tv_sheetRow1 = (TextView) itemView.findViewById(R.id.tv_sheetRow1);
            tv_sheetRow2 = (TextView) itemView.findViewById(R.id.tv_sheetRow2);
            tv_sheetRow3 = (TextView) itemView.findViewById(R.id.tv_sheetRow3);
            tv_sheetRow4 = (TextView) itemView.findViewById(R.id.tv_sheetRow4);
        }

        public TextView getTv_sheetRow1() {
            return tv_sheetRow1;
        }

        public TextView getTv_sheetRow2() {
            return tv_sheetRow2;
        }

        public TextView getTv_sheetRow3() {
            return tv_sheetRow3;
        }

        public TextView getTv_sheetRow4() {
            return tv_sheetRow4;
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }



}