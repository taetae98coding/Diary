import Foundation
import SwiftUI
import Firebase
import iosApp

class AppDelegate: NSObject, UIApplicationDelegate {
    private let stateKeeper = StateKeeperDispatcherKt.StateKeeperDispatcher(savedState: nil)
    private let backDispatcher: BackDispatcher = BackDispatcherKt.BackDispatcher()
    
    internal lazy var entry =  AppEntry(
        context: DefaultComponentContext(
            lifecycle: ApplicationLifecycle(),
            stateKeeper: stateKeeper,
            instanceKeeper: nil,
            backHandler: backDispatcher
        )
    )
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        FirebaseApp.configure()
        return true
    }
}
