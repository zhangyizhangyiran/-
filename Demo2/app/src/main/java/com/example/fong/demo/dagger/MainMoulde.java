package com.example.fong.demo.dagger;

import com.example.fong.demo.ui.DetailPagesActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Fong on 17/2/6.
 */
@Module
public class MainMoulde {
    @Provides
    DetailPagesActivity getDetailPagesActivity() {
        return new DetailPagesActivity();
    }
}
