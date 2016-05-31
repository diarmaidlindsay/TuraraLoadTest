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
        usersList.add(new User("admin", "DReA"));
        usersList.add(new User("user", "Xsoz"));
        usersList.add(new User("demouser01", "UTWT"));
        usersList.add(new User("demouser02", "Jtd6"));
        usersList.add(new User("testuser01", "BC7N"));
        usersList.add(new User("testuser02", "Bb3F"));
        usersList.add(new User("ueda01", "3L9C"));
        usersList.add(new User("ueda02", "aDFK"));
        usersList.add(new User("ueda03", "KpDP"));
        usersList.add(new User("ueda04", "GMR6"));
        usersList.add(new User("diarmaid01", "8BKZ"));
        usersList.add(new User("diarmaid02", "YAsG"));
        usersList.add(new User("diarmaid03", "tJPZ"));
        usersList.add(new User("diarmaid04", "AjsD"));
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
