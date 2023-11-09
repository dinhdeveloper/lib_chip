#open
-keep class unit.chip.lib_unit_chip.public_release.ChipResult {
    *;
}
-keep class unit.chip.lib_unit_chip.public_release.NfcCallback {
    *;
}
-keep class unit.chip.lib_unit_chip.public_release.NfcError {
    *;
}
-keep class unit.chip.lib_unit_chip.public_release.NfcOption {
     *;
}
-keep class unit.chip.lib_unit_chip.public_release.NfcTagTool {
    public *;
}

#close
-dontwarn unit.chip.lib_unit_chip.model.**
-dontwarn unit.chip.lib_unit_chip.common.**
-dontwarn unit.chip.lib_unit_chip.nfc.**
-dontwarn unit.chip.lib_unit_chip.security.**

-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** w(...);
    public static *** e(...);
    public static *** i(...);
}