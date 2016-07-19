package kang.recyclerdb.Fragment;

/**
 * Created by kangjonghyuk on 2016. 7. 14..
 */

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kang.recyclerdb.DB.ContractColumns;
import kang.recyclerdb.Adapter.ContractCursorAdapter;
import kang.recyclerdb.R;


public class ListContractFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView mRecyclerView;
    private ContractCursorAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);

        view.findViewById(R.id.fabAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Dialog_Fragment().show(getFragmentManager(), "dialog");
            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        configureSwipe();

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ContractCursorAdapter(new ContractCursorAdapter.OnClickItem() {
            @Override
            public void itemClickListener(Cursor cursor) {
                long id = cursor.getLong(cursor.getColumnIndex(ContractColumns._ID));
                Dialog_Fragment f = Dialog_Fragment.newInstance(id);
                f.show(getFragmentManager(), "dialog");
            }
        });
        mAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mAdapter);

        getLoaderManager().initLoader(0, null, this);

        return view;
    }

    private void configureSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final int x = viewHolder.getLayoutPosition();
                Cursor cursor = mAdapter.getCursor();
                cursor.moveToPosition(x);
                final long id = cursor.getLong(cursor.getColumnIndex(ContractColumns._ID));
                getActivity().getContentResolver().delete(
                        Uri.withAppendedPath(ContractColumns.URI_MENSAGENS, String.valueOf(id)),
                        null, null);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        // Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder
        return new CursorLoader(getActivity(), ContractColumns.URI_MENSAGENS, null, null, null, ContractColumns.NAME);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.setCursor(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        mAdapter.setCursor(null);
    }


}