package utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bsi.pontua.CadastroEventosConfigurarTabEntidades;
import com.bsi.pontua.CadastroEventosConfigurarTabItensInspecao;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                CadastroEventosConfigurarTabEntidades tab1 = new CadastroEventosConfigurarTabEntidades();
                return tab1;
            case 1:
                CadastroEventosConfigurarTabItensInspecao tab2 = new CadastroEventosConfigurarTabItensInspecao();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}