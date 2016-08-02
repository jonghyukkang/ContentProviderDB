package kang.recyclerdb.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kang.recyclerdb.Activity.GroupDeleteActivity;
import kang.recyclerdb.ETC.TotalListener;
import kang.recyclerdb.ETC.ExpandedMenuModel;
import kang.recyclerdb.R;

/**
 * Created by kangjonghyuk on 2016. 7. 31..
 */
public class ExpandListAdapter extends BaseExpandableListAdapter {

    public List<List<String>> sGroupList = new ArrayList<>();
    private Context mContext;
    private List<ExpandedMenuModel> sListDataHeader;
    private HashMap<ExpandedMenuModel, List<String>> sListDataChild;
    ArrayList<ArrayList<Boolean>> selectedChildCheckBoxStates = new ArrayList<>();
    ArrayList<Boolean> selectedParentCheckBoxStates = new ArrayList<>();
    TotalListener mListener;
    ExpandableListView expandable_list;
    ArrayList<String> delete_list = new ArrayList<>();
    ArrayList<String> delete_group = new ArrayList<>();
    public ArrayList<String> list_group = new ArrayList<>();

    public void setListener(TotalListener mListener) {
        this.mListener = mListener;
    }

    public ExpandListAdapter(Context context, List<ExpandedMenuModel> listDataHeader, HashMap<ExpandedMenuModel, List<String>> listChildData, ExpandableListView view) {
        this.mContext = context;
        this.sListDataHeader = listDataHeader;
        this.sListDataChild = listChildData;
        this.expandable_list = view;

        initCheckStates(false);
    }

    public void initCheckStates(boolean defaultState) {
        for (int i = 0; i < sListDataHeader.size(); i++) {
            sGroupList.add(i, sListDataChild.get(sListDataHeader.get(i)));
        }

        for (int i = 0; i < sGroupList.size(); i++) {
            selectedParentCheckBoxStates.add(i, defaultState);
            ArrayList<Boolean> childStates = new ArrayList<>();
            for (int j = 0; j < sGroupList.get(i).size(); j++) {
                childStates.add(defaultState);
            }
            selectedChildCheckBoxStates.add(i, childStates);
        }
    }

    @Override
    public int getGroupCount() {
        return sListDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int childCount = 0;
        try {
            if (groupPosition != 100) {
                childCount = sListDataChild.get(sListDataHeader.get(groupPosition)).size();
            }
        } catch (NullPointerException e) {
            Log.d("ExpandListAdapter", "List.size() null");
        }
        return childCount;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return sListDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return sListDataChild.get(sListDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.group_edit_header, null);

        CheckBox groupCheckBox = (CheckBox) convertView.findViewById(R.id.group_check_box);
        TextView tv1 = (TextView) convertView.findViewById(R.id.submenu);

        ExpandedMenuModel headerTitle = (ExpandedMenuModel) getGroup(groupPosition);
        tv1.setText(headerTitle.getIconName());

        if (selectedParentCheckBoxStates.size() <= groupPosition) {
            selectedParentCheckBoxStates.add(groupPosition, false);
        } else {
            groupCheckBox.setChecked(selectedParentCheckBoxStates.get(groupPosition));
        }

        groupCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isExpanded)
                    mListener.expandGroupEvent(groupPosition, isExpanded);

                boolean state = selectedParentCheckBoxStates.get(groupPosition);
                selectedParentCheckBoxStates.remove(groupPosition);
                selectedParentCheckBoxStates.add(groupPosition, state ? false : true);

                for (int i = 0; i < sGroupList.get(groupPosition).size(); i++) {
                    selectedChildCheckBoxStates.get(groupPosition).remove(i);
                    selectedChildCheckBoxStates.get(groupPosition).add(i, state ? false : true);
                }
                notifyDataSetChanged();

                if (selectedParentCheckBoxStates.get(groupPosition) == true) {
                    for (int i = 0; i < sGroupList.get(groupPosition).size(); i++) {
                        if (!delete_list.contains(sGroupList.get(groupPosition)))
                            delete_list.add(sGroupList.get(groupPosition).get(i));
                    }
                    delete_group.add(sListDataHeader.get(groupPosition).getIconName());
                } else {
                    for (int i = 0; i < sGroupList.get(groupPosition).size(); i++) {
                        delete_list.remove(sGroupList.get(groupPosition).get(i));
                    }
                    delete_group.remove(sListDataHeader.get(groupPosition).getIconName());
                }
                mListener.onTotalChanged(delete_list, delete_group);
            }
        });

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.expandGroupEvent(groupPosition, isExpanded);
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.group_edit_child, null);

        final String childText = (String) getChild(groupPosition, childPosition);
        CheckBox childCheckBox = (CheckBox) convertView.findViewById(R.id.child_check_box);
        TextView tv1 = (TextView) convertView.findViewById(R.id.submenu);

        tv1.setText(childText);
        try {
            if (selectedChildCheckBoxStates.size() <= groupPosition) {
                ArrayList<Boolean> childState = new ArrayList<>();
                for (int i = 0; i < sGroupList.get(groupPosition).size(); i++) {
                    if (childState.size() > childPosition)
                        childState.add(childPosition, false);
                    else
                        childState.add(false);
                }
                if (selectedChildCheckBoxStates.size() > groupPosition) {
                    selectedChildCheckBoxStates.add(groupPosition, childState);
                } else
                    selectedChildCheckBoxStates.add(childState);
            } else {
                childCheckBox.setChecked(selectedChildCheckBoxStates.get(groupPosition).get(childPosition));
            }
        }catch(IndexOutOfBoundsException e){
            Log.d("IndexOutOfBounds", " Invalid index");
        }
        childCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean state = selectedChildCheckBoxStates.get(groupPosition).get(childPosition);
                selectedChildCheckBoxStates.get(groupPosition).remove(childPosition);
                selectedChildCheckBoxStates.get(groupPosition).add(childPosition, state ? false : true);

                if (selectedChildCheckBoxStates.get(groupPosition).get(childPosition) == true) {
                    delete_list.add(sGroupList.get(groupPosition).get(childPosition));
                    list_group.add(sListDataHeader.get(groupPosition).getIconName());
                }
                else {
                    delete_list.remove(sGroupList.get(groupPosition).get(childPosition));
                }
                mListener.onTotalChanged(delete_list, delete_group);
            }
        });

        return convertView;
    }


//    for (int i = 0; i < sListDataHeader.size(); i++) {
//        sGroupList.add(i, sListDataChild.get(sListDataHeader.get(i)));
//    }
//    initCheckStates(false);
//
//
//    private void initCheckStates(boolean defaultState) {
//        for (int i = 0; i < sGroupList.size(); i++) {
//            selectedParentCheckBoxStates.add(i, defaultState);
//            ArrayList<Boolean> childStates = new ArrayList<>();
//            for (int j = 0; j < sGroupList.get(i).size(); j++) {
//                childStates.add(defaultState);
//            }
//            selectedChildCheckBoxStates.add(i, childStates);
//        }
//    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
