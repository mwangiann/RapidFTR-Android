package com.rapidftr.model;

import org.json.JSONException;

public class PotentialMatch extends BaseModel {
    private static final String ENQUIRY_ID_FIELD = "enquiry_id";
    private static final String CHILD_ID_FIELD = "child_id";

    public PotentialMatch(String jsonString) throws JSONException {
        super(jsonString);
    }

    public PotentialMatch(String enquiryId, String childId, String uniqueIdentifier) {
        this.put(ENQUIRY_ID_FIELD, enquiryId);
        this.put(CHILD_ID_FIELD, childId);
        this.put(FIELD_INTERNAL_ID, uniqueIdentifier);
    }

    public String getChildId() {
        return getString(CHILD_ID_FIELD);
    }

    public String getEnquiryId() {
        return getString(ENQUIRY_ID_FIELD);
    }

    public String getUniqueId() {
        return getString(FIELD_INTERNAL_ID);
    }

    public String getRevision() {
        return getString(FIELD_REVISION_ID);
    }

    @Override
    public String getApiPath() {
        return "/api/potential_matches";
    }

    @Override
    public String getApiParameter() {
        return "potential_match";
    }
}