package com.example.zakhariystasenko.exchangerate;

import dagger.Component;

@Component (modules = {PicassoModule.class,DownloaderModule.class})
interface RateViewActivityInjector {
    void inject(RateViewActivity activity);
}
