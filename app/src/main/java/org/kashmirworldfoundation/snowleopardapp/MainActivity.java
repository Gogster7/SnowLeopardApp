package org.kashmirworldfoundation.snowleopardapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.ListFragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import org.kashmirworldfoundation.snowleopardapp.Fragment.AddFragment;
import org.kashmirworldfoundation.snowleopardapp.Fragment.ProfileFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private ListFragment listFragment;
    private AddFragment addFragment;
    private ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       toolbar = findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);

       viewPager = findViewById(R.id.view_pager);
       tabLayout = findViewById(R.id.tab_layout);

       listFragment = new ListFragment();
       addFragment = new AddFragment();
       profileFragment = new ProfileFragment();

       tabLayout.setupWithViewPager(viewPager);

       // Setting up Tabs

       ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
       viewPagerAdapter.addFragment(listFragment, "List");
       viewPagerAdapter.addFragment(addFragment, "Add");
       viewPagerAdapter.addFragment(profileFragment, "Profile");
       viewPager.setAdapter(viewPagerAdapter);
       // Icons
       tabLayout.getTabAt(0).setIcon(R.drawable.ic_list);
       tabLayout.getTabAt(1).setIcon(R.drawable.ic_add);
       tabLayout.getTabAt(2).setIcon(R.drawable.ic_profile);
       // Badges
        //BadgeDrawable badgeDrawable = tabLayout.getTabAt(0).getOrCreateBadge();
        //badgeDrawable.setVisible(true);
        //badgeDrawable.setNumber(10);
    }



    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }
}
