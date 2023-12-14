//
//  AppDelegate.swift
//  Diary
//
//  Created by WK10695 on 12/7/23.
//  Copyright © 2023 taetae98. All rights reserved.
//

import Foundation
import SwiftUI
import iosApp
import FirebaseCore

class AppDelegate: NSObject, UIApplicationDelegate {
    let contextHolder = ContextHolder()
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        FirebaseApp.configure()
        return true
    }
}
