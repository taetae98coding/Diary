import SwiftUI

@main
struct DiaryApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self)
    var appDelegate: AppDelegate
    
	var body: some Scene {
		WindowGroup {
            ContentView(appEntry: appDelegate.contextHolder.appEntry)
		}
	}
}
