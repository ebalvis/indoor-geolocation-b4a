# 📡 indoor-geolocation-b4a

Aplicación Android desarrollada en **B4A (Basic for Android)** para sistemas de geolocalización en interiores basada en señales WiFi. Diseñada para usarse en entornos docentes y de investigación sobre geolocalización, interpolación de señales y filtrado de datos en tiempo real.

---

## 🚀 Características

La app trabaja en **dos modos operativos**:

### 1. 🔘 Modo Localización OFF (recolección de datos)
- El usuario introduce manualmente:
  - El **punto** de escaneo (número/nombre)
  - El **nivel/planta** (PB, 1, 2, etc.)
- La app realiza un escaneo de redes WiFi y envía los resultados como un mensaje JSON a un **servidor MQTT** configurado.
- Utilizado para **crear una base de datos de huellas WiFi**.

### 2. 🛰️ Modo Localización ON (estimación en tiempo real)
- La app realiza escaneos periódicos de WiFi.
- Los datos se envían continuamente al servidor MQTT.
- La app se **suscribe a un topic** para recibir la **posición estimada** calculada por el sistema externo.
- Puede usarse para mostrar al usuario su posición estimada sobre un plano o mapa.

---

## 📦 Formato de Datos (Ejemplo JSON)

```json
{
  "registro": {
    "fabricante": "Xiaomi",
    "modelo": "22111317G",
    "producto": "sunstone_eea",
    "sdk": "34",
    "androidID": "4f2c1264cc83a0c1",
    "fecha_hora": 1750756597330,
    "nivel": "PB",
    "punto": 1
  },
  "wifi_scans": [
    {
      "SSID": "innebo",
      "BSSID": "30:23:03:dc:18:69",
      "seguridad": "WPA2",
      "potencia": -48,
      "canal": 6,
      "distancia": 2.46,
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
