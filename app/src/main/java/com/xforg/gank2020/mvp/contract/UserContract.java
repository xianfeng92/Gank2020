package com.xforg.gank2020.mvp.contract;

import android.app.Activity;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xforg.g2020.mvp.IModel;
import com.xforg.g2020.mvp.IView;
import com.xforg.gank2020.mvp.model.entity.User;
import java.util.List;
import io.reactivex.Observable;

/**
 * ================================================
 * 展示 Contract 的用法
 * ================================================
 */
public interface UserContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void startLoadMore();
        void endLoadMore();
        void setNewData(List<User> mData);
        Activity getActivity();
        //申请权限
        RxPermissions getRxPermissions();
    }
    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,如是否使用缓存
    interface Model extends IModel {
        Observable<List<User>> getUsers(boolean update);
    }
}
