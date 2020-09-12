fun android.view.View.show() {
    visibility = android.view.View.VISIBLE
}

fun android.view.View.hide() {
    visibility = android.view.View.GONE
}

fun android.content.Context.showToast(message: String) {
    android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_LONG).show()
}

fun android.content.Context.showToast(@@androidx.annotation.StringRes resourceId: Int) {
    android.widget.Toast.makeText(this, resourceId, android.widget.Toast.LENGTH_LONG).show()
}

fun android.view.View.hideKeyboard() {
    val inputMethodManager =
        context.getSystemService(android.app.Activity.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

fun android.view.View.navigateTo(navigationId: Int) {
    android.view.findNavController().navigate(navigationId)
}

fun android.view.View.navigateTo(navDirections: androidx.navigation.NavDirections) {
    androidx.navigation.Navigation.findNavController(this).navigate(navDirections)
}
