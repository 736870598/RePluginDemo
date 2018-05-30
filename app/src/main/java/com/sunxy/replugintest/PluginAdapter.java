package com.sunxy.replugintest;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * --
 * <p>
 * Created by sunxy on 2018/5/28 0028.
 */
public class PluginAdapter extends RecyclerView.Adapter<PluginAdapter.ViewHolder>{


    private List<PluginInfoModel> list;
    private OnItemClick listener;

    public PluginAdapter(OnItemClick listener) {
        this.listener = listener;
    }

    public void setList(List list){
        this.list = list;
        notifyDataSetChanged();
    }

    public PluginInfoModel getModel(int pos){
        return list.get(pos);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindView(position);
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView nameView;
        private TextView statusView;
        private TextView reInstallView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.nameView);
            statusView = itemView.findViewById(R.id.statusView);
            reInstallView = itemView.findViewById(R.id.reInstallView);
        }

        public void bindView(final int pos){
            PluginInfoModel model = getModel(pos);
            nameView.setText(model.getFile().getName());
            if (model.isInstall()){
                statusView.setText("插件安装版本：" + model.getInstallCode() + " , apk版本：" + model.getApkCode());
            }else{
                statusView.setText("该插件未安装");
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(pos);
                }
            });
            reInstallView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.reInsall(pos);
                }
            });

        }
    }

    public interface OnItemClick{
        void onClick(int pos);
        void reInsall(int pos);
    }
}
