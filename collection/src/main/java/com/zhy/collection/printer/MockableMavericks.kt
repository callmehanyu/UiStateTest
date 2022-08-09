package com.zhy.collection.printer

/**
 * Entry point for setting up Mavericks for the app in a mockable way.
 *
 * See [initialize]
 */
object MockableMavericks {

    /**
     * Configuration for how mock state is printed.
     *
     * The Mavericks mocking system allows you to generate a reproduction of a ViewModel's state. For
     * any [MavericksState] instance that a ViewModel has, Mavericks can generate a file containing code
     * to completely reconstruct that state.
     *
     * This generated code can then be used to reconstruct States that can be used during testing.
     * The scripts in the MvRx/mock_generation folder are used to interact with the device to pull
     * the resulting mock files.
     *
     * [enableMockPrinterBroadcastReceiver] must be enabled for this to work.
     *
     * See [MavericksMockPrinter]
     * See https://github.com/airbnb/MvRx/wiki/Mock-Printer
     */
    var mockPrinterConfiguration = MockPrinterConfiguration()


}
