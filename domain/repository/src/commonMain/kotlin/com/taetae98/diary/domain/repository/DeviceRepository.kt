package com.taetae98.diary.domain.repository

import com.taetae98.diary.domain.entity.device.DeviceInfo
import kotlinx.coroutines.flow.Flow

public interface DeviceRepository {
    public fun getDeviceInfo(): Flow<DeviceInfo>
}