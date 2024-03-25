package com.healspan.claimservice.mstupload.claim.constants;

import org.springframework.stereotype.Component;

@Component
public class HealspanQueries {

    public static final String RECEIVER_DASHBOARD_QUERY =
            "select * from healspan.GET_CLAIM_DETAILS where user_mst_id = {0}";

    public static final String HOSPITAL_DASHBOARD_QUERY =
            "select * from healspan.get_hospital_dashboard_details({0})";

    public static final String HEALSPAN_DASHBOARD_QUERY =
            "select * from healspan.get_healspan_dashboard_details({0})";
}
