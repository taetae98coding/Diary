package com.taetae98.diary.data.repository.device

import com.taetae98.diary.domain.entity.device.DeviceInfo
import com.taetae98.diary.domain.repository.DeviceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.koin.core.annotation.Factory

@Factory
internal class DeviceRepositoryImpl(

) : DeviceRepository {
    override fun getDeviceInfo(): Flow<DeviceInfo> {
        return emptyFlow()
    }
}