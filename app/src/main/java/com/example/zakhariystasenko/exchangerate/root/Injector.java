package com.example.zakhariystasenko.exchangerate.root;

import com.example.zakhariystasenko.exchangerate.graph.GraphViewActivity;
import com.example.zakhariystasenko.exchangerate.modules.ContextModule;
import com.example.zakhariystasenko.exchangerate.modules.DataManagerModule;
import com.example.zakhariystasenko.exchangerate.modules.DatabaseHelperModule;
import com.example.zakhariystasenko.exchangerate.modules.DownloaderModule;
import com.example.zakhariystasenko.exchangerate.modules.PicassoModule;
import com.example.zakhariystasenko.exchangerate.rate.RateViewActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ContextModule.class,
        PicassoModule.class,
        DownloaderModule.class,
        DatabaseHelperModule.class,
        DataManagerModule.class
})
public interface Injector {
    void inject(RateViewActivity activity);

    void inject(GraphViewActivity activity);
}
