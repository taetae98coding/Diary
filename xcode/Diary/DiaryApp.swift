import SwiftUI
import iosApp

@main
struct DiaryApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self)
    var appDelegate: AppDelegate
    
    init() {
        KoinKt.doInit()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView(entry: appDelegate.entry).ignoresSafeArea(.keyboard)
        }
    }
}
