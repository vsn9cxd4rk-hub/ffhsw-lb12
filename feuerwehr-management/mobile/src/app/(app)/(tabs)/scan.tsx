import { CameraView, useCameraPermissions, type BarcodeScanningResult } from 'expo-camera';
import { router, useFocusEffect, useIsFocused } from 'expo-router';
import { useCallback, useRef, useState } from 'react';
import { KeyboardAvoidingView, Platform, Pressable, ScrollView, StyleSheet, View } from 'react-native';

import { ThemedText } from '@/components/themed-text';
import { ThemedView } from '@/components/themed-view';
import { Button } from '@/components/ui/button';
import { TextField } from '@/components/ui/text-field';
import { MaxContentWidth, Spacing } from '@/constants/theme';
import { parseScannedCode } from '@/lib/scan';

// QR: web URL ".../scan/<nr>"; Code 128 is the JsBarcode default used for the labels
const BARCODE_TYPES = ['qr', 'code128', 'code39', 'ean13'] as const;

export default function ScanScreen() {
  const [permission, requestPermission] = useCameraPermissions();
  const isFocused = useIsFocused();
  const [torch, setTorch] = useState(false);
  const [manualCode, setManualCode] = useState('');
  // The camera reports the same code many times per second: handle only the first one until
  // the user comes back to this tab.
  const handled = useRef(false);

  function open(code: string) {
    if (!code) return;
    router.push({ pathname: '/scan/[code]', params: { code } });
  }

  function handleScan(result: BarcodeScanningResult) {
    if (handled.current) return;
    handled.current = true;
    open(parseScannedCode(result.data));
  }

  useFocusEffect(
    useCallback(() => {
      handled.current = false;
    }, []),
  );

  return (
    <ThemedView style={styles.container}>
      <KeyboardAvoidingView style={styles.container} behavior={Platform.OS === 'ios' ? 'padding' : undefined}>
        <ScrollView contentContainerStyle={styles.content} keyboardShouldPersistTaps="handled">
          <View style={styles.cameraBox}>
            {!permission ? null : permission.granted ? (
              // Camera only runs while the tab is visible (battery, frees it for other apps)
              isFocused && (
                <>
                  <CameraView
                    style={StyleSheet.absoluteFill}
                    facing="back"
                    enableTorch={torch}
                    barcodeScannerSettings={{ barcodeTypes: [...BARCODE_TYPES] }}
                    onBarcodeScanned={handleScan}
                  />
                  <View style={styles.frame} pointerEvents="none" />
                  <Pressable
                    accessibilityRole="button"
                    onPress={() => setTorch((t) => !t)}
                    style={styles.torch}>
                    <ThemedText type="smallBold" style={styles.torchText}>
                      {torch ? 'Licht aus' : 'Licht an'}
                    </ThemedText>
                  </Pressable>
                </>
              )
            ) : (
              <View style={styles.permission}>
                <ThemedText type="small" style={styles.center}>
                  Zum Scannen der Etiketten braucht FENIX Zugriff auf die Kamera.
                </ThemedText>
                {permission.canAskAgain ? (
                  <Button title="Kamera erlauben" onPress={requestPermission} />
                ) : (
                  <ThemedText type="small" style={styles.center}>
                    Der Zugriff wurde abgelehnt. Bitte in den Einstellungen des Geräts freigeben.
                  </ThemedText>
                )}
              </View>
            )}
          </View>

          <ThemedText type="small" themeColor="textSecondary">
            QR-Code oder Barcode auf dem Geräte-Etikett in den Rahmen halten. Alternativ die
            Inventarnummer eingeben:
          </ThemedText>
          <TextField
            label="Inventarnummer"
            value={manualCode}
            onChangeText={setManualCode}
            autoCapitalize="characters"
            autoCorrect={false}
            returnKeyType="search"
            onSubmitEditing={() => open(manualCode.trim())}
          />
          <Button title="Suchen" variant="secondary" onPress={() => open(manualCode.trim())} disabled={!manualCode.trim()} />
        </ScrollView>
      </KeyboardAvoidingView>
    </ThemedView>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1 },
  content: {
    padding: Spacing.three,
    gap: Spacing.three,
    width: '100%',
    maxWidth: MaxContentWidth / 2,
    alignSelf: 'center',
  },
  cameraBox: {
    aspectRatio: 1,
    borderRadius: Spacing.three,
    overflow: 'hidden',
    backgroundColor: '#000000',
    justifyContent: 'center',
  },
  frame: {
    position: 'absolute',
    top: '20%',
    left: '20%',
    right: '20%',
    bottom: '20%',
    borderWidth: 3,
    borderColor: '#ffffff',
    borderRadius: Spacing.three,
    opacity: 0.8,
  },
  torch: {
    position: 'absolute',
    bottom: Spacing.three,
    alignSelf: 'center',
    backgroundColor: 'rgba(0,0,0,0.6)',
    borderRadius: Spacing.four,
    paddingHorizontal: Spacing.three,
    paddingVertical: Spacing.two,
  },
  torchText: { color: '#ffffff' },
  permission: { padding: Spacing.four, gap: Spacing.three, alignItems: 'stretch' },
  center: { textAlign: 'center', color: '#ffffff' },
});
