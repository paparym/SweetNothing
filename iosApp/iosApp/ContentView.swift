import SwiftUI
import shared

struct ContentView: View {
    
    var body: some View {
        ComposeView().ignoresSafeArea(.all) // Compose has own keyboard handler
    }
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
