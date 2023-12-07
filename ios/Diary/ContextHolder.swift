import Foundation
import iosApp

class ContextHolder : ObservableObject {
    let lifecycle: LifecycleRegistry
    let appEntry: AppEntry
    
    init() {
        lifecycle = LifecycleRegistryKt.LifecycleRegistry()
        appEntry = AppEntry(context: DefaultComponentContext(lifecycle: lifecycle))
        
        LifecycleRegistryExtKt.create(lifecycle)
    }
    
    deinit {
        LifecycleRegistryExtKt.destroy(lifecycle)
    }
}
