import SwiftUI
import iosApp

struct ContentView: UIViewControllerRepresentable {
    let appEntry: AppEntry
    
    init(appEntry: AppEntry) {
        self.appEntry = appEntry
    }
    
    func makeUIViewController(context: Context) -> UIViewController {
        return MainKt.compose(appEntry: appEntry)
    }
    
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        
    }
}
