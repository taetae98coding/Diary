//
//  SceneDelegate.swift
//  Diary
//
//  Created by WK10695 on 12/29/23.
//

import Foundation
import UIKit
import iosApp

class SceneDelegate: NSObject, UIWindowSceneDelegate {
    let fetchDataWorkerHolder = FetchDataWorkerHolder()
    var window: UIWindow?
    
    func sceneWillEnterForeground(_ scene: UIScene) {
        fetchDataWorkerHolder.execute()
    }
    
    func sceneDidEnterBackground(_ scene: UIScene) {
        fetchDataWorkerHolder.cancel()
    }
}
