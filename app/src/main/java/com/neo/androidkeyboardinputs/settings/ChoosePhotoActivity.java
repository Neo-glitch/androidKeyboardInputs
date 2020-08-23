package com.neo.androidkeyboardinputs.settings;

import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


import com.google.android.material.tabs.TabLayout;
import com.neo.androidkeyboardinputs.R;


/**
 * Activity to host the new fragments for implementing tabs
 */
public class ChoosePhotoActivity extends AppCompatActivity implements KeyEvent.Callback {

    private static final String TAG = "ChoosePhotoActivity";
    private static final int GALLERY_FRAGMENT = 0;
    private static final int PHOTO_FRAGMENT = 1;

    //fragments
    private GalleryFragment mGalleryFragment;
    private PhotoFragment mPhotoFragment;

    //widgets
    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_photo);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_container);

        setupViewPager();
    }

    /**
     * setup viewpager for manager the tabs
     */
    private void setupViewPager() {
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        mGalleryFragment = new GalleryFragment();
        mPhotoFragment = new PhotoFragment();
        adapter.addFragment(mGalleryFragment);
        adapter.addFragment(mPhotoFragment);
        mViewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs_bottom);
        tabLayout.setupWithViewPager(mViewPager);                       // associates the tabLayout with the viewPager that hosts our fragment

        tabLayout.getTabAt(GALLERY_FRAGMENT).setText(getString(R.string.tag_fragment_gallery));
        tabLayout.getTabAt(PHOTO_FRAGMENT).setText(getString(R.string.tag_fragment_photo));

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_DPAD_DOWN_LEFT:
                if(event.isShiftPressed()){
                    tabLeft();
                }
                return false;
            case KeyEvent.KEYCODE_DPAD_DOWN_RIGHT:
                if(event.isCtrlPressed()){
                    tabRight();
                }
                return false;
            default:
                return super.onKeyUp(keyCode, event);
        }

    }

    /**
     * moves viewPager to right item if current item is left item(0)
     */
    private void tabRight() {
        if(mViewPager.getCurrentItem() == 0){
            mViewPager.setCurrentItem(1);
        }
    }

    private void tabLeft(){
        if(mViewPager.getCurrentItem() == 1){
            mViewPager.setCurrentItem(0);
        }
    }
}










