package kang.recyclerdb.ETC;

import java.util.ArrayList;

/**
 * Created by kangjonghyuk on 2016. 7. 31..
 */
public interface TotalListener {

    void onTotalChanged(ArrayList<String> list, ArrayList<String> group);
    void expandGroupEvent(int groupPosition, boolean isExpanded);
}

