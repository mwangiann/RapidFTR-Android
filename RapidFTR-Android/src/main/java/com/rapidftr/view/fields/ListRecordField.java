package com.rapidftr.view.fields;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.rapidftr.R;
import com.rapidftr.activity.ViewChildActivity;
import com.rapidftr.adapter.HighlightedFieldsViewAdapter;
import com.rapidftr.model.Child;
import com.rapidftr.model.Enquiry;
import com.rapidftr.model.PotentialMatch;
import com.rapidftr.repository.ChildRepository;
import com.rapidftr.repository.PotentialMatchRepository;
import com.rapidftr.utils.ApplicationInjector;
import lombok.Cleanup;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ListRecordField extends BaseView {

    public ListRecordField(Context context) {
        super(context);
    }

    public ListRecordField(Context context, AttributeSet attr) {
        super(context, attr);
    }


    private ListView getListRecordView() {
        return (ListView) findViewById(R.id.list_records);
    }


    @Override
    protected void initialize() throws JSONException {
        super.initialize();
        Enquiry enquiry=(Enquiry) model;
        Injector inject = Guice.createInjector(new ApplicationInjector());

        @Cleanup ChildRepository childRepo = inject.getInstance(ChildRepository.class);
        @Cleanup PotentialMatchRepository potentialMatchRepo = inject.getInstance(PotentialMatchRepository.class);

        List<PotentialMatch> potentialMatches = potentialMatchRepo.getPotentialMatchesFor(enquiry);
        List<Child> children = childRepo.getAllWithInternalIds(Child.idsFromMatches(potentialMatches));

        HighlightedFieldsViewAdapter highlightedFieldsViewAdapter = new HighlightedFieldsViewAdapter(getContext(), children, Child.CHILD_FORM_NAME, ViewChildActivity.class);
        ListView childListView = (ListView) findViewById(R.id.list_records);
        if (children.isEmpty()) {
            childListView.setEmptyView(findViewById(R.id.no_matches));
        } else
        childListView.setAdapter(highlightedFieldsViewAdapter);
        setListViewHeightBasedOnChildren(childListView);
    }

    private static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0) {
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));
            }
            view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    public void setEnabled(boolean enabled) {
        enabled = false;
        super.setEnabled(enabled);

        getListRecordView().setEnabled(enabled);
        getListRecordView().setClickable(enabled);
        getListRecordView().setFocusable(enabled);
        getListRecordView().setFocusableInTouchMode(enabled);
    }


}
