package com.skilledhacker.developer.musiqx;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.skilledhacker.developer.musiqx.Fragments.LoginFragment;
import com.skilledhacker.developer.musiqx.Fragments.RegistrationFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guy on 6/23/2017.
 */

public class IdentificationActivty extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identification);

        viewPager = (ViewPager) findViewById(R.id.pager_identification);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs_identification);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new LoginFragment(),getString(R.string.login_title));
        adapter.addFragment(new RegistrationFragment(), getString(R.string.registration_title));
        viewPager.setAdapter(adapter);
    }

    public void selectFragment(int position){
        viewPager.setCurrentItem(position, true);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
