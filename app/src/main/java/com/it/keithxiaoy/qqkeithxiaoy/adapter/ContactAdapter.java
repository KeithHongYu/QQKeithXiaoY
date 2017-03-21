package com.it.keithxiaoy.qqkeithxiaoy.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.it.keithxiaoy.qqkeithxiaoy.R;
import com.it.keithxiaoy.qqkeithxiaoy.util.StringUtils;

import java.util.List;

/**
 * Created by xiaoY on 2017/3/4.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> implements SlideBarAdapter{
    private List<String> mContactsList;

    public ContactAdapter(List<String> contactsList) {
        this.mContactsList = contactsList;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_contact, parent, false);
        ContactViewHolder contactViewHolder = new ContactViewHolder(view);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        /**
         * 1.开头第一个肯定显示mTvSection
         * 2.后面的条目跟上一个条目比较：
         * 如果后面的条目跟上一个条目首字母相同，则不显示，gone掉
         * 如果后面的条目跟上一个条目的首字母不相同，则显示出来
         */


        final String contact = mContactsList.get(position);
        String inital = StringUtils.getInital(contact);
        holder.mTvUsername.setText(contact);
        holder.mTvSection.setText(inital);
        if (position == 0){
            holder.mTvSection.setVisibility(View.VISIBLE);
        }else {
            String preName = mContactsList.get(position-1);
            if (StringUtils.getInital(preName).equalsIgnoreCase(StringUtils.getInital(inital))){
                holder.mTvSection.setVisibility(View.GONE);
            }else {
                holder.mTvSection.setVisibility(View.VISIBLE);
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnContactItemClickListener.onClick(contact);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
              mOnContactItemClickListener.onLongClick(contact);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mContactsList == null ? 0 : mContactsList.size();
    }

    @Override
    public List<String> getData() {
        return mContactsList;
    }


    class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView mTvUsername;
        TextView mTvSection;

        public ContactViewHolder(View itemView) {
            super(itemView);
            mTvSection = (TextView) itemView.findViewById(R.id.tv_section);
            mTvUsername = (TextView) itemView.findViewById(R.id.tv_username);
        }
    }

    public interface OnContactItemClickListener{
        void onClick(String username);
        void onLongClick(String username);
    }

    private OnContactItemClickListener mOnContactItemClickListener;

    public void setonContactItemClickListener(OnContactItemClickListener onContactItemClickListener){
        this.mOnContactItemClickListener = onContactItemClickListener ;
    }


}
