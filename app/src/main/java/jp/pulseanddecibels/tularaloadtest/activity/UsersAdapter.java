package jp.pulseanddecibels.tularaloadtest.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jp.pulseanddecibels.tularaloadtest.R;
import jp.pulseanddecibels.tularaloadtest.model.User;

/**
 * Created by Diarmaid Lindsay on 2016/05/30.
 * Copyright Pulse and Decibels 2016
 */
public class UsersAdapter extends BaseAdapter {
    private final LayoutInflater layoutInflater;
    private List usersList = new ArrayList<>();

    public UsersAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        usersList.add(new User("admin", "pass"));
        usersList.add(new User("miyawaki", "pass"));
        usersList.add(new User("otori", "pass"));
        usersList.add(new User("ueda", "pass"));
        usersList.add(new User("fujiwara", "pass"));
        usersList.add(new User("tanemura", "pass"));
        usersList.add(new User("haruki", "pass"));
        usersList.add(new User("matsusue", "pass"));
        usersList.add(new User("diarmaid", "pass"));
        usersList.add(new User("hamatani", "pass"));
        usersList.add(new User("morita", "pass"));
        usersList.add(new User("soma", "pass"));
        usersList.add(new User("sugimoto", "pass"));
        usersList.add(new User("takahata", "pass"));
        usersList.add(new User("user1", "pass"));
        usersList.add(new User("user2", "pass"));
        usersList.add(new User("user3", "pass"));
        usersList.add(new User("user4", "pass"));
        usersList.add(new User("user5", "pass"));
    }

    @Override
    public int getCount() {
        return usersList.size();
    }

    @Override
    public Object getItem(int position) {
        return usersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem viewHolderItem;
        if (convertView == null) {
            viewHolderItem = new ViewHolderItem();
            convertView = layoutInflater.inflate(R.layout.list_item_user, parent, false);
            viewHolderItem.name = (TextView) convertView.findViewById(R.id.user_list_name);
            convertView.setTag(viewHolderItem);
        } else {
            viewHolderItem = (ViewHolderItem) convertView.getTag();
        }
        User user = (User) getItem(position);

        viewHolderItem.name.setText(user.getUserName());

        return convertView;
    }

    static class ViewHolderItem {
        TextView name;
    }
}
