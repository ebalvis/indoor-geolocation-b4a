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

