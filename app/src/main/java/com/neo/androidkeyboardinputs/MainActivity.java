package com.neo.androidkeyboardinputs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.neo.androidkeyboardinputs.models.FragmentTag;
import com.neo.androidkeyboardinputs.models.Message;
import com.neo.androidkeyboardinputs.models.User;
import com.neo.androidkeyboardinputs.settings.SettingsFragment;
import com.neo.androidkeyboardinputs.util.PreferenceKeys;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements IMainActivity, BottomNavigationViewEx.OnNavigationItemSelectedListener,
        NavigationView.OnNavigationItemSelectedListener,
        KeyEvent.Callback {                         // intercepts keyEvents of a keyboard

    private static final String TAG = "MainActivity";
    private NavigationView mNavigationView;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        // Handle navigation view item clicks here.
        switch (item.getItemId()) {


            case R.id.home: {                   // in case, reset the back stack
                mFragmentsTags.clear();
                mFragmentsTags = new ArrayList<>();
                init();                         // does transaction for the home fragment
                break;
            }

            case R.id.settings: {
                Log.d(TAG, "onNavigationItemSelected: Settings.");
                if (mSettingsFragment == null) {                        // avoids building the fragment again if it's already created
                    mSettingsFragment = new SettingsFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.main_content_frame, mSettingsFragment, getString(R.string.tag_fragment_settings));
                    transaction.commit();
                    mFragmentsTags.add(getString(R.string.tag_fragment_settings));                  // adds tag to the list
                    mFragments.add(new FragmentTag(mSettingsFragment, getString(R.string.tag_fragment_settings)));   // adds the fragment class obj to list
                } else {
                    // moves fragment from lowerEntry in stack and move it to top # implementing backStack implementation
                    mFragmentsTags.remove(getString(R.string.tag_fragment_settings));
                    mFragmentsTags.add(getString(R.string.tag_fragment_settings));
                }
                setFragmentVisibilities(getString(R.string.tag_fragment_settings));
                break;
            }

            case R.id.agreement: {
                Log.d(TAG, "onNavigationItemSelected: Agreement.");
                if (mAgreementFragment == null) {
                    mAgreementFragment = new AgreementFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.main_content_frame, mAgreementFragment, getString(R.string.tag_fragment_agreement));
                    transaction.commit();
                    mFragmentsTags.add(getString(R.string.tag_fragment_agreement));
                    mFragments.add(new FragmentTag(mAgreementFragment, getString(R.string.tag_fragment_agreement)));
                } else {
                    mFragmentsTags.remove(getString(R.string.tag_fragment_agreement));
                    mFragmentsTags.add(getString(R.string.tag_fragment_agreement));
                }
                setFragmentVisibilities(getString(R.string.tag_fragment_agreement));
                break;
            }

            case R.id.bottom_nav_home: {
                Log.d(TAG, "onNavigationItemSelected: HomeFragment.");

                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.main_content_frame, mHomeFragment, getString(R.string.tag_fragment_home));
                    transaction.commit();
                    mFragmentsTags.add(getString(R.string.tag_fragment_home));
                    mFragments.add(new FragmentTag(mHomeFragment, getString(R.string.tag_fragment_home)));
                } else {
                    mFragmentsTags.remove(getString(R.string.tag_fragment_home));
                    mFragmentsTags.add(getString(R.string.tag_fragment_home));
                }
                setFragmentVisibilities(getString(R.string.tag_fragment_home));
                item.setChecked(true);
                break;
            }

            case R.id.bottom_nav_connections: {
                Log.d(TAG, "onNavigationItemSelected: ConnectionsFragment.");

                if (mSavedConnectionsFragment == null) {
                    mSavedConnectionsFragment = new SavedConnectionsFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.main_content_frame, mSavedConnectionsFragment, getString(R.string.tag_fragment_saved_connections));
                    transaction.commit();
                    mFragmentsTags.add(getString(R.string.tag_fragment_saved_connections));
                    mFragments.add(new FragmentTag(mSavedConnectionsFragment, getString(R.string.tag_fragment_saved_connections)));
                } else {
                    mFragmentsTags.remove(getString(R.string.tag_fragment_saved_connections));
                    mFragmentsTags.add(getString(R.string.tag_fragment_saved_connections));
                }
                setFragmentVisibilities(getString(R.string.tag_fragment_saved_connections));
                item.setChecked(true);
                break;
            }

            case R.id.bottom_nav_messages: {
                Log.d(TAG, "onNavigationItemSelected: MessagesFragment.");
                if (mMessagesFragment == null) {
                    mMessagesFragment = new MessagesFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.main_content_frame, mMessagesFragment, getString(R.string.tag_fragment_messages));
                    transaction.commit();
                    mFragmentsTags.add(getString(R.string.tag_fragment_messages));
                    mFragments.add(new FragmentTag(mMessagesFragment, getString(R.string.tag_fragment_messages)));
                } else {
                    mFragmentsTags.remove(getString(R.string.tag_fragment_messages));
                    mFragmentsTags.add(getString(R.string.tag_fragment_messages));
                }
                setFragmentVisibilities(getString(R.string.tag_fragment_messages));
                item.setChecked(true);
                break;
            }
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    //constants
    private static final int HOME_FRAGMENT = 0;
    private static final int CONNECTIONS_FRAGMENT = 1;
    private static final int MESSAGES_FRAGMENT = 2;


    //Fragments
    private HomeFragment mHomeFragment;
    private SavedConnectionsFragment mSavedConnectionsFragment;
    private MessagesFragment mMessagesFragment;
    private SettingsFragment mSettingsFragment;
    private ViewProfileFragment mViewProfileFragment;
    private ChatFragment mChatFragment;
    private AgreementFragment mAgreementFragment;

    //widgets
    private BottomNavigationViewEx mBottomNavigationViewEx;
    private ImageView mHeaderImage;
    private DrawerLayout mDrawerLayout;

    //vars
    private ArrayList<String> mFragmentsTags = new ArrayList<>();
    private ArrayList<FragmentTag> mFragments = new ArrayList<>();
    private int mExitCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBottomNavigationViewEx = findViewById(R.id.bottom_nav_view);
        mNavigationView = findViewById(R.id.navigation_view);
        View headerView = mNavigationView.getHeaderView(0);
        mHeaderImage = headerView.findViewById(R.id.header_image);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        isFirstLogin();
        setHeaderImage();
        initBottomNavigationView();
        setNavigationViewListener();
        init();
    }

    private void init() {
        if (mHomeFragment == null) {
            mHomeFragment = new HomeFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.main_content_frame, mHomeFragment, getString(R.string.tag_fragment_home));
            transaction.commit();
            mFragmentsTags.add(getString(R.string.tag_fragment_home));
            mFragments.add(new FragmentTag(mHomeFragment, getString(R.string.tag_fragment_home)));

        } else {
            mFragmentsTags.remove(getString(R.string.tag_fragment_home));
            mFragmentsTags.add(getString(R.string.tag_fragment_home));
        }
        setFragmentVisibilities(getString(R.string.tag_fragment_home));
    }


    /**
     * method to fix the bottomNav icon not being marked in fragment in focus when back button is pressed
     */
    private void setNavigationIcon(String tagname) {
        Menu menu = mBottomNavigationViewEx.getMenu();
        MenuItem menuItem = null;
        if (tagname.equals(getString(R.string.tag_fragment_home))) {
            Log.d(TAG, "setNavigationIcon: home fragment is visible");
            menuItem = menu.getItem(HOME_FRAGMENT);                 // the get home_fragment menu item since first item in bottomNav
            menuItem.setChecked(true);
        } else if (tagname.equals(getString(R.string.tag_fragment_saved_connections))) {
            Log.d(TAG, "setNavigationIcon: connections fragment is visible");
            menuItem = menu.getItem(CONNECTIONS_FRAGMENT);
            menuItem.setChecked(true);
        } else if (tagname.equals(getString(R.string.tag_fragment_messages))) {
            Log.d(TAG, "setNavigationIcon: messages fragment is visible");
            menuItem = menu.getItem(MESSAGES_FRAGMENT);
            menuItem.setChecked(true);
        }
    }

    private void setFragmentVisibilities(String tagname) {
        if (tagname.equals(getString(R.string.tag_fragment_home)))
            showBottomNavigation();
        else if (tagname.equals(getString(R.string.tag_fragment_saved_connections)))
            showBottomNavigation();
        else if (tagname.equals(getString(R.string.tag_fragment_messages)))
            showBottomNavigation();
        else if (tagname.equals(getString(R.string.tag_fragment_settings)))
            hideBottomNavigation();
        else if (tagname.equals(getString(R.string.tag_fragment_view_profile)))
            hideBottomNavigation();
        else if (tagname.equals(getString(R.string.tag_fragment_chat)))
            hideBottomNavigation();
        else if (tagname.equals(getString(R.string.tag_fragment_agreement)))
            hideBottomNavigation();

        for (int i = 0; i < mFragments.size(); i++) {
            if (tagname.equals(mFragments.get(i).getTag())) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.show((mFragments.get(i).getFragment()));
                transaction.commit();
            } else {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.hide((mFragments.get(i).getFragment()));
                transaction.commit();
            }
        }
        setNavigationIcon(tagname);

        printBackStack();
    }

    private void setHeaderImage() {
        Log.d(TAG, "setHeaderImage: setting header image for navigation drawer.");

        // better to set the header with glide. It's more efficient than setting the source directly
        Glide.with(this)
                .load(R.drawable.couple)
                .into(mHeaderImage);
    }

    private void hideBottomNavigation() {
        if (mBottomNavigationViewEx != null) {
            mBottomNavigationViewEx.setVisibility(View.GONE);
        }
    }

    private void showBottomNavigation() {
        if (mBottomNavigationViewEx != null) {
            mBottomNavigationViewEx.setVisibility(View.VISIBLE);
        }
    }

    private void setNavigationViewListener() {
        Log.d(TAG, "setNavigationViewListener: initializing navigation drawer onclicklistener.");
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initBottomNavigationView() {
        Log.d(TAG, "initBottomNavigationView: initializing bottom navigation view.");
//        mBottomNavigationViewEx.enableAnimation(false);
        mBottomNavigationViewEx.setOnNavigationItemSelectedListener(this);
        mBottomNavigationViewEx.setBackgroundColor(Color.WHITE);
//        mBottomNavigationViewEx.setBackgroundColor(getResources().getColor(R.color.white));

    }

    private void isFirstLogin() {
        Log.d(TAG, "isFirstLogin: checking if this is the first login.");

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstLogin = preferences.getBoolean(PreferenceKeys.FIRST_TIME_LOGIN, true);

        if (isFirstLogin) {
            Log.d(TAG, "isFirstLogin: launching alert dialog.");

            // launch the info dialog for first-time-users
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(getString(R.string.first_time_user_message));
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Log.d(TAG, "onClick: closing dialog");
                    // now that the user has logged in, save it to shared preferences so the dialog won't
                    // pop up again
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(PreferenceKeys.FIRST_TIME_LOGIN, false);
                    editor.commit();
                    dialogInterface.dismiss();
                }
            });
            alertDialogBuilder.setTitle(" ");
            alertDialogBuilder.setIcon(R.drawable.tabian_dating);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }


    /////// IMainActivity interface method //////
    @Override
    public void inflateViewProfileFragment(User user) {

        if (mViewProfileFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(mViewProfileFragment).commitAllowingStateLoss();
        }
        mViewProfileFragment = new ViewProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.intent_user), user);
        mViewProfileFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_content_frame, mViewProfileFragment, getString(R.string.tag_fragment_view_profile));
        transaction.commit();
        mFragmentsTags.add(getString(R.string.tag_fragment_view_profile));
        mFragments.add(new FragmentTag(mViewProfileFragment, getString(R.string.tag_fragment_view_profile)));

        setFragmentVisibilities(getString(R.string.tag_fragment_view_profile));
    }

    @Override
    public void onMessageSelected(Message message) {

        if (mChatFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(mChatFragment).commitAllowingStateLoss();
        }
        mChatFragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.intent_message), message);
        mChatFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_content_frame, mChatFragment, getString(R.string.tag_fragment_chat));
        transaction.commit();
        mFragmentsTags.add(getString(R.string.tag_fragment_chat));
        mFragments.add(new FragmentTag(mChatFragment, getString(R.string.tag_fragment_chat)));

        setFragmentVisibilities(getString(R.string.tag_fragment_chat));
    }

    @Override
    public void onBackPressed() {
        // remove fragment at top and show the one directly below it.. but check if there's another fragment below top
        int backStackCount = mFragmentsTags.size();
        if (backStackCount > 1) {                     // true if a fragment after fragment in focus
            String topFragmentTag = mFragmentsTags.get(backStackCount - 1);

            // the one after the top or previous fragment in view
            String newTopFragmentTag = mFragmentsTags.get(backStackCount - 2);
            setFragmentVisibilities(newTopFragmentTag);
            mFragmentsTags.remove(topFragmentTag);
            mExitCount = 0;
        } else if (backStackCount == 1) {
            mHomeFragment.scrollToTop();
            mExitCount++;
            Toast.makeText(this, "1 more click to exit", Toast.LENGTH_SHORT).show();
        }
        if (mExitCount >= 2) {                    // true when user taps backButton 1 more times after message is shown
            super.onBackPressed();
        }

        hideKeyboard();

    }

    @Override
    public void setBottomNavigationVisibility(boolean visibility) {
        if (visibility) {
            showBottomNavigation();
        } else {
            hideBottomNavigation();
        }
    }

    @Override
    public void hideKeyboard() {
        if (getCurrentFocus() != null) {          // makes sure the View or activity not null
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            try {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);              // hides the keyboard
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

//    /**
//     * shows how to hind a keyboard in a fragment
//     */
//    public static void hideKeyboardInFragment(Context context, View view){
//        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
//    }
    /////// IMainActivity interface method //////


    private void printBackStack() {
        Log.d(TAG, "printBackStack: ----------------------------------- ");
        for (int i = 0; i < mFragmentsTags.size(); i++) {
            Log.d(TAG, "printBackStack: " + i + ": " + mFragmentsTags.get(i));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isChildFragmentVisible()) {
            if (keyCode == KeyEvent.KEYCODE_TAB) {
                moveFocusInFragment();                          // moves focus to the nextWidget
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (!event.isShiftPressed()) {
            switch ((keyCode)) {
                case KeyEvent.KEYCODE_TAB:
                    if (event.isCtrlPressed()) {                   // true when tab + ctrl is pressed, and not working
                        Log.d(TAG, "onKeyUp: shift pressed");
                        toggleNavigationDrawer();                   // toggles nav opening state
                    }
                    return false;
                case KeyEvent.KEYCODE_O:                         // improvised case
                    toggleNavigationDrawer();
                    return false;
                case KeyEvent.KEYCODE_R:
//                if(event.isCtrlPressed()){                // not working
//                    refresh();
//                }
                    refresh();
                    return false;
                case KeyEvent.KEYCODE_S:
                    focusSearchView();
                    return false;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    if (event.isCtrlPressed())
                        shiftLeft();            // shifts bottomNav selection to left
                    return false;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    if (event.isCtrlPressed())
                        shiftRight();           // shifts bottomNav selection to right
                    return false;
                default:
                    return super.onKeyUp(keyCode, event);
            }
        } else {                    // if shift is pressed before the cases
            switch ((keyCode)) {
                case KeyEvent.KEYCODE_ENTER:
                    sendChatMessage();
                    return false;
                default:
                    return super.onKeyUp(keyCode, event);
            }
        }
    }

    /**
     * checks if bottomNav is visible and if it's return true
     * bottomNav only visible in home,savedConnections and Messages fragment
     */
    private boolean isBottomNavigationVisible() {
        if (mHomeFragment != null) {
            if (mHomeFragment.isVisible()) {
                return true;
            }
        }
        if (mSavedConnectionsFragment != null) {
            if (mSavedConnectionsFragment.isVisible()) {
                return true;
            }
        }
        if (mMessagesFragment != null) {
            if (mMessagesFragment.isVisible()) {
                return true;
            }
        }
        return false;
    }

    private void shiftRight() {
        if (isBottomNavigationVisible()) {
            if(mBottomNavigationViewEx.getCurrentItem() == 0){
                mBottomNavigationViewEx.setCurrentItem(1);
            } else if(mBottomNavigationViewEx.getCurrentItem() == 1){
                mBottomNavigationViewEx.setCurrentItem(2);
            }

        }
    }

    private void shiftLeft() {
        if (isBottomNavigationVisible()) {
            if(mBottomNavigationViewEx.getCurrentItem() == 2){
                mBottomNavigationViewEx.setCurrentItem(1);
            } else if(mBottomNavigationViewEx.getCurrentItem() == 1){
                mBottomNavigationViewEx.setCurrentItem(0);
            }
        }
    }


    /**
     * called when we want to tab or nav to searchView widget in Messages Fragment
     */
    private void focusSearchView() {
        if (mMessagesFragment != null) {
            if (mMessagesFragment.isVisible()) {
                mMessagesFragment.mSearchView.requestFocusFromTouch();
            }
        }
    }


    private void toggleNavigationDrawer() {
        if (mDrawerLayout != null) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            } else {
                mDrawerLayout.openDrawer(GravityCompat.START);
                mNavigationView.requestFocus();                         // makes nav view focusable for tab key navigation
            }
        }

    }

    private void moveFocusInFragment() {
        if (mViewProfileFragment != null) {
            if (mViewProfileFragment.isVisible()) {
                mViewProfileFragment.moveFocusForward();
            }
        }
        if (mSettingsFragment != null) {
            if (mSettingsFragment.isVisible()) {
                mSettingsFragment.moveFocusForward();
            }
        }
        if (mChatFragment != null) {
            if (mChatFragment.isVisible()) {
                mChatFragment.moveFocusForward();
            }
        }

    }

    /**
     * checks if any of fragments specified to handle tab nav(click events) is visible i.e settings, viewProfile, chat
     */
    private boolean isChildFragmentVisible() {
        if (mViewProfileFragment != null) {
            if (mViewProfileFragment.isVisible()) {
                return true;
            }
        }
        if (mSettingsFragment != null) {
            if (mSettingsFragment.isVisible()) {
                return true;
            }
        }
        if (mChatFragment != null) {
            if (mChatFragment.isVisible()) {
                return true;
            }
        }
        return false;
    }

    /**
     * sends message type in chatFragment without clicking on send Button
     */
    private void sendChatMessage() {
        if (mChatFragment != null) {
            if (mChatFragment.isVisible()) {
                mChatFragment.postMessage();
            }
        }
    }


    /**
     * calls swipeRefresher layout interface methods implemented in each of this fragment
     */
    private void refresh() {
        if (mHomeFragment != null) {
            mHomeFragment.onRefresh();
        }
        if (mSavedConnectionsFragment != null) {
            mSavedConnectionsFragment.onRefresh();
        }
        if (mMessagesFragment != null) {
            mSavedConnectionsFragment.onRefresh();
        }
    }


}












