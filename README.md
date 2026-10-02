# NoWakeProximity

LSPosed / Xposed module to prevent proximity sensor check from sleeping the display on double-tap wake on Xiaomi HyperOS.

## Automated Builds and Releases (GitHub Actions)

This repository includes configured GitHub Actions workflows for continuous integration and automated releases:

### 1. Automated Releases (`.github/workflows/release.yml`)

The release workflow compiles the app (both Release and Debug APKs), computes SHA-256 checksums, publishes a GitHub Release, and uploads workflow artifacts.

#### Triggering a Release:
- **By Git Tag:** Push a tag starting with `v` (for example: `v1.0.0`):
  ```bash
  git tag v1.0.0
  git push origin v1.0.0
  ```
- **Manually via GitHub Web UI:**
  1. Go to the **Actions** tab on your GitHub repository.
  2. Select **Build and Release** from the left sidebar.
  3. Click **Run workflow**, enter your desired tag name (e.g. `v1.0.0`), draft/pre-release settings, and run.

#### Signing Configuration:
The release workflow is designed to work immediately without manual setup, while also supporting custom production signing keys:
- **Out of the box:** If no repository secrets are provided, the workflow automatically generates a self-signed keystore on the CI runner, ensuring compilation and release never fail.
- **Custom Production Signing (Optional):** To sign releases with your own keystore, add the following secrets in **Settings > Secrets and variables > Actions**:
  - `KEYSTORE_BASE64`: Base64 string of your `.jks` file (generate with `base64 -w 0 my-upload-key.jks`)
  - `STORE_PASSWORD`: Keystore password
  - `KEY_PASSWORD`: Key password (defaults to `STORE_PASSWORD` if omitted)

### 2. Continuous Integration (`.github/workflows/ci.yml`)

Runs automatically on every push or pull request to `main` and `master`:
- Runs unit tests (`./gradlew test`)
- Compiles the debug APK (`./gradlew assembleDebug`)
- Attaches the debug APK as an action artifact

---

## Local Compilation

```bash
# Compile debug APK
./gradlew assembleDebug

# Run unit tests
./gradlew test
```
The compiled APK will be located in `app/build/outputs/apk/`.
