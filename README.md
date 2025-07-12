# 📡 indoor-geolocation-b4a

Android application developed in **B4A (Basic for Android)** for indoor geolocation systems based on WiFi signals. Designed for educational and research environments focused on geolocation, signal interpolation, and real-time data filtering.

---

## 🚀 Features

The app operates in **two working modes**:

### 1. 🔘 Localization OFF Mode (data collection)
- The user manually enters:
  - The **scan point** (number)
  - The **floor/level** (G, L1, etc.)
- The app performs a WiFi scan and sends the results as a JSON message to a configured **MQTT server**.
- Used to **build a WiFi fingerprinting database**.

### 2. 🛰️ Localization ON Mode (real-time estimation)
- The app periodically scans for WiFi networks.
- The data is continuously sent to the MQTT server.
- The app **subscribes to a topic** to receive the **estimated position** calculated by the external system.
- Can be used to display the user’s estimated position on a map or floorplan.

---

## 🛠️ Development Environment

This project is built using:

### ✔️ IDE & Tools
- **B4A (Basic for Android)** version **>= 11.80**
- **B4XPages framework** (optional but recommended for modularity)
- **Java JDK 11** or later
- **Android SDK** (API level 29 or higher recommended)

### 📦 Required Libraries
- `MQTT_B4X` (based on Eclipse Paho MQTT)
- `Network` (for connectivity checking)
- `Phone` (for WiFi scan access)
- `JSON` (for serialization of messages)
- `Timer`, `StringUtils`, etc. (standard B4A libraries)

### ⚙️ Device Requirements
- Android device with:
  - **WiFi scanning capabilities**
  - **Location permission enabled**
  - Android **API 21 (Lollipop)** or newer

### 🚀 How to Compile
1. Download and install **B4A** from [b4x.com](https://www.b4x.com).
2. Open the `indoor-geolocation-b4a.b4a` project file.
3. Ensure all required libraries are available in the **Libraries** tab.
4. Configure your **bridge** or **USB device** for deployment.
5. Compile and install on a real device (WiFi scanning does not work on emulators).

---

## 📦 Data Format (JSON Example)

```json
{
  "registro": {
    "manufacturer": "Xiaomi",
    "model": "22111317G",
    "product": "sunstone_eea",
    "sdk": "34",
    "androidID": "0f200264cc00a000",
    "timestamp": 1750756597330,
    "floor": "G",
    "point": 1
  },
  "wifi_scans": [
    {
      "SSID": "wifi_test",
      "BSSID": "00:23:03:00:18:00",
      "security": "WPA2",
      "rssi": -48,
      "channel": 6,
      "distance": 2.46,
      "data_0": 0,
      "data_1": 100,
      "data_2": 2437,
      "data_3": 2427,
      "data_4": 0,
      "data_5": 1,
      "data_6": 1,
      "data_7": 0,
      "data_8": 0,
      "data_9": 0
    }
  ]
}

---

## 🔄 Cambios en la última versión

- Se añade control automático del permiso y activación del GPS.
- Se amplía el formato del mensaje JSON con información del dispositivo y marca temporal.
- Se introduce cálculo de distancia estimada basada en RSSI.
- Se añaden campos extendidos para redes WiFi (data_0 a data_9).
- Se refactoriza y reestructura el código para mejorar mantenibilidad y preparar futuras funcionalidades.
