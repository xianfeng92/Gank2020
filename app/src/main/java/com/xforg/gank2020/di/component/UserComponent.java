
package com.xforg.gank2020.di.component;

import com.xforg.g2020.di.component.AppComponent;
import com.xforg.g2020.di.scope.ActivityScope;
import com.xforg.gank2020.mvp.ui.activity.UserActivity;
import com.xforg.gank2020.di.module.UserModule;
import com.xforg.gank2020.mvp.contract.UserContract;
import dagger.BindsInstance;
import dagger.Component;

/**
 * ================================================
 * 展示 Component 的用法
 * ================================================
 */
@ActivityScope
@Component(modules = UserModule.class, dependencies = AppComponent.class)
public interface UserComponent {
    void inject(UserActivity activity);
    @Component.Builder
    interface Builder {
        @BindsInstance
        UserComponent.Builder view(UserContract.View view);
        UserComponent.Builder appComponent(AppComponent appComponent);
        UserComponent build();
    }
}
