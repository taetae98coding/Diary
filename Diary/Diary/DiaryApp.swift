import SwiftUI
import Kotlin

@main
struct DiaryApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    
    init() {
        IosInitializerKt.doInit()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView().ignoresSafeArea()
        }
    }
}
