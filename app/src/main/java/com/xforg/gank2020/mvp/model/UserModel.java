package com.xforg.gank2020.mvp.model;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import com.xforg.g2020.di.scope.ActivityScope;
import com.xforg.g2020.integration.IRepositoryManager;
import com.xforg.g2020.mvp.BaseModel;
import com.xforg.gank2020.mvp.contract.UserContract;
import com.xforg.gank2020.mvp.model.api.service.UserService;
import com.xforg.gank2020.mvp.model.entity.User;
import java.util.List;
import javax.inject.Inject;
import io.reactivex.Observable;
import timber.log.Timber;

/**
 * ================================================
 * 展示 Model 的用法
 * ================================================
 */
@ActivityScope
public class UserModel extends BaseModel implements UserContract.Model {
    private static final String TAG = "UserModel";
    public static final int USERS_PER_PAGE = 10;

    @Inject
    public UserModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<List<User>> getUsers(boolean update) {
        //使用rxcache缓存,上拉刷新则不读取缓存,加载更多读取缓存
        return mRepositoryManager
                .obtainRetrofitService(UserService.class)
                .getUsers(USERS_PER_PAGE);
//                .flatMap(new Function<Observable<List<User>>, ObservableSource<List<User>>>() {
//                    @Override
//                    public ObservableSource<List<User>> apply(@NonNull Observable<List<User>> listObservable) throws Exception {
//                        return mRepositoryManager.obtainCacheService(CommonCache.class)
//                                .getUsers(listObservable
//                                        , new DynamicKey(lastIdQueried)
//                                        , new EvictDynamicKey(update))
//                                .map(new Function<Reply<List<User>>, List<User>>() {
//                                    @Override
//                                    public List<User> apply(Reply<List<User>> listReply) throws Exception {
//                                        Log.d(TAG, "apply: "+listReply.getData());
//                                        return listReply.getData();
//                                    }
//                                });
//                    }
//                });

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause() {
        Timber.d("Release Resource");
    }

}
