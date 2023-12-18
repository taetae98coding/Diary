import Foundation
import SwiftUI
import Firebase
import iosApp

class AppDelegate: NSObject, UIApplicationDelegate {
//    private let lifecycle = ApplicationLifecycle()
//    private let context = DefaultComponentContext(
//        lifecycle: ApplicationLifecycle()
//    )
    
    internal let entry =  AppEntry(
        context: DefaultComponentContext(
            lifecycle: ApplicationLifecycle()
        )
    )
    
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        FirebaseApp.configure()
        return true
    }
}
