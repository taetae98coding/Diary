import KMP
import SwiftUI

struct ContentView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        return DiaryViewControllerKt.DiaryViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {

    }
}
