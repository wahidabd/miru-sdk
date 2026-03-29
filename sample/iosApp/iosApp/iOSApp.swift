import SwiftUI
import Sample

@main
struct iOSApp: App {
    init() {
        // Read API key from environment or fallback
        let apiKey = Bundle.main.object(forInfoDictionaryKey: "NEWS_API_KEY") as? String ?? ""
        IosAppInitializerKt.initializeSdk(newsApiKey: apiKey)
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
