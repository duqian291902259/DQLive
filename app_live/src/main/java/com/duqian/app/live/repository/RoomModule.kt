package com.duqian.app.live.repository

import com.duqian.app.live.services.IRoomApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

/**
 * Description:房间Module提供者
 *
 * Created by 杜乾 on 2022/8/14 - 14:18.
 * E-mail: duqian2010@gmail.com
 */
@InstallIn(ActivityComponent::class)
@Module
abstract class RoomModule {

    @Binds
    abstract fun bindRoomApi(impl: RoomApiImpl): IRoomApi
}
