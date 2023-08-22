import Foundation
import SwiftUI
import shared

struct ComposeView: UIViewControllerRepresentable {
    func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context){}
    
    func makeUIViewController(context: Context) -> some UIViewController {
        App_iosKt.MainViewController()
    }
}
