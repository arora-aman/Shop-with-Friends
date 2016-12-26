package com.aman_arora.firebase.swf.utils;

import com.aman_arora.firebase.swf.BuildConfig;

public final class Constants {

    public static final String FIREBASE_URL = BuildConfig.UNIQUE_FIREBASE_ROOT_URL;
    public static final String FIREBASE_ACTIVE_LISTS_LOCATION = BuildConfig.FIREBASE_ACTIVE_LISTS_LOCATION;
    public static final String FIREBASE_ACTIVE_LISTS_URL = FIREBASE_URL + '/' + FIREBASE_ACTIVE_LISTS_LOCATION;
    public static final String FIREBASE_PROPERTY_TIMESTAMP = BuildConfig.FIREBASE_TIMESTAMP_LOCATION;
    public static final String FIREBASE_PROPERTY_TIMESTAMP_UPDATED = BuildConfig.FIREBASE_LAST_TIME_STAMP_LOCATION;
    public static final String TIMESTAMP_OBJECT_KEY = "date";
    public static final String LIST_NAME_LOCATION = BuildConfig.FIREBASE_LIST_NAME_LOCATION;
    public static final String LIST_ITEMS_LOCATION = BuildConfig.FIREBASE_LIST_ITEMS_LOCATION;
    public static final String FIREBASE_ITEM_URL = FIREBASE_URL + '/' + LIST_ITEMS_LOCATION;
    public static final String FIREBASE_ITEM_NAME = BuildConfig.FIREBASE_LIST_ITEM_NAME;
    public static final String GOOGLE_OAUTH_CLIENT_ID = BuildConfig.GOOGLE_OAUTH_REQUEST_TOKEN;
    public static final String USER_LIST_LOCATION = BuildConfig.FIREBASE_USER_LIST;
    public static final String FIREBASE_USER_LIST_URL = FIREBASE_URL + '/' + USER_LIST_LOCATION;


    //EditListDialogFragment Key values for bundle
    public static final String KEY_LAYOUT_RESOURCE = "LAYOUT_RESOURCE";
    public static final String KEY_CURRENT_NAME = "currentName";

    //Intent extra key for push id for active list details activity

    public static final String KEY_PUSH_ID_ACTIVE_LIST = "pushIDIntentExtraActiveListDetails";
    public static final String KEY_PUSH_ID_LIST_ITEM = "pushIDIntentForCurrentListItem";
    public static final String PREFERENCE_LOGIN_FILE = BuildConfig.PREFERENCE_LOGIN_FILE;
    public static final String PREFERENCE_ENCODED_EMAIL = "enCOdedEmAilUsEr";

    public static final String PREFERENCE_PROVIDER = "ProviderFortHelUsErEmail";
    public static final String PROVIDER_EMAIL_PASSWORD = "EmaiLnPassWord";
}
