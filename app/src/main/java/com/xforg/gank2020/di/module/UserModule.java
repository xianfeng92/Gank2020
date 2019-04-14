package com.xforg.gank2020.di.module;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xforg.g2020.di.scope.ActivityScope;
import com.xforg.gank2020.mvp.contract.UserContract;
import com.xforg.gank2020.mvp.model.UserModel;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class UserModule {

    @Binds
    abstract UserContract.Model bindUserModel(UserModel model);

    @ActivityScope
    @Provides
    static RxPermissions provideRxPermissions(UserContract.View view) {
        return new RxPermissions((FragmentActivity) view.getActivity());
    }

    @ActivityScope
    @Provides
    static RecyclerView.LayoutManager provideLayoutManager(UserContract.View view) {
        return new GridLayoutManager(view.getActivity(), 2);
    }
}
