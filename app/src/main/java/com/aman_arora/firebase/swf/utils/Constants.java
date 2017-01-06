package com.aman_arora.firebase.swf.utils;

import com.aman_arora.firebase.swf.BuildConfig;


public final class Constants {

    public static final String FIREBASE_URL = BuildConfig.UNIQUE_FIREBASE_ROOT_URL;
    public static final String FIREBASE_USER_LISTS_LOCATION = BuildConfig.FIREBASE_USER_LISTS_LOCATION;
    public static final String FIREBASE_USER_LISTS_URL = FIREBASE_URL + '/' + FIREBASE_USER_LISTS_LOCATION;
    public static final String FIREBASE_PROPERTY_TIMESTAMP_UPDATED = BuildConfig.FIREBASE_LAST_TIME_STAMP_LOCATION;
    public static final String TIMESTAMP_OBJECT_KEY = "date";
    public static final String LIST_NAME_LOCATION = BuildConfig.FIREBASE_LIST_NAME_LOCATION;
    public static final String LIST_ITEMS_LOCATION = BuildConfig.FIREBASE_LIST_ITEMS_LOCATION;
    public static final String FIREBASE_ITEM_URL = FIREBASE_URL + '/' + LIST_ITEMS_LOCATION;
    public static final String FIREBASE_ITEM_NAME = BuildConfig.FIREBASE_LIST_ITEM_NAME;
    public static final String GOOGLE_OAUTH_CLIENT_ID = BuildConfig.GOOGLE_OAUTH_REQUEST_TOKEN;
    public static final String USER_LOCATION = BuildConfig.FIREBASE_USER_LIST;
    public static final String FIREBASE_USERS_URL = FIREBASE_URL + '/' + USER_LOCATION;
    public static final String PROPERTY_ITEM_BOUGHT = BuildConfig.PROPERTY_ITEM_BOUGHT;
    public static final String PROPERTY_ITEM_BOUGHT_BY = BuildConfig.PROPERTY_ITEM_BOUGHT_BY;
    public static final String PROPERTY_LIST_SHOPPING_USERS = BuildConfig.PROPERY_LIST_SHOPPING_USERS;
    public static final String FIREBASE_USER_FRIENDS_LOCATION = BuildConfig.USER_FRIENDS_LOCATION;
    public static final String FIREBASE_USER_FRIENDS_URL = FIREBASE_URL + '/' + FIREBASE_USER_FRIENDS_LOCATION;
    public static final String FIREBASE_SHARED_WITH_LOCATION = BuildConfig.SHARED_WITH_LOCATION;
    public static final String FIREBASE_SHARED_WITH_URL = FIREBASE_URL + '/' + FIREBASE_SHARED_WITH_LOCATION;
    public static final String FIREBASE_UID_MAPPINGS_LOCATION = BuildConfig.FIREBASE_UID_MAPPINGS;
    public static final String FIREBASE_LIST_OWNERS_LOCATION = BuildConfig.FIREBASE_LIST_OWNER_LOCATION;
    public static final String FIREBASE_USER_VERIFIED_LOCATION = BuildConfig.FIREBASE_USER_VERIFIED_LCOATION;

    //EditListDialogFragment Key values for bundle
    public static final String KEY_LAYOUT_RESOURCE = "LAYOUT_RESOURCE";
    public static final String KEY_CURRENT_NAME = "currentName";

    //Intent extra key for push id for user list details activity

    public static final String KEY_PUSH_ID_USER_LIST = "pushIDIntentExtraUserListDetails";
    public static final String KEY_PUSH_ID_LIST_ITEM = "pushIDIntentForCurrentListItem";
    public static final String PREFERENCE_LOGIN_FILE = BuildConfig.PREFERENCE_LOGIN_FILE;
    public static final String PREFERENCE_ENCODED_EMAIL = "enCOdedEmAilUsEr";
    public static final String KEY_SHOPPING_LIST_OWNER = "shoppingListOWNERArgumentForDialog";
    public static final String KEY_SHARED_WITH_USERS = "sharedUsersKeyArgs";

    public static final String PREFERENCE_PROVIDER = "ProviderFortHelUsErEmail";
    public static final String PROVIDER_EMAIL_PASSWORD = "EmaiLnPassWord";

    //Sort preferences

    public static final String KEY_PREF_SORT_ORDER_LISTS = "PERF_SORT_ORDER_LISTS";
    public static final String ORDER_BY_KEY = "orderByPushKey";


}
