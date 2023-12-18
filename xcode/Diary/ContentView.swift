import SwiftUI
import iosApp

struct ContentView: UIViewControllerRepresentable {
    let entry: AppEntry
    
    init(entry: AppEntry) {
        self.entry = entry
    }
    
    func makeUIViewController(context: Context) -> UIViewController {
        return MainKt.compose(entry: entry)
    }
    
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        
    }
}
