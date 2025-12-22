<div align="center">
  <img src="https://gym-pw.de/wp-content/uploads/2015/06/logo.png" alt="Logo" width="300" height="120">

  <h3 align="center">Weihnachts-Minecraft-Server</h3>

  <p align="center">
    Plugins, Web-Interface & Jahresrückblick für das Schulevent
    <br />
    <a href="https://github.com/CyZeTLC/Weihnachtsminecraft/issues">Fehler melden</a>
    ·
    <a href="https://github.com/CyZeTLC/Weihnachtsminecraft/pulls">Feature Request</a>
  </p>
</div>

<div align="center">

![GitHub License](https://img.shields.io/github/license/CyZeTLC/Weihnachtsminecraft?style=for-the-badge)
![GitHub stars](https://img.shields.io/github/stars/CyZeTLC/Weihnachtsminecraft?style=for-the-badge)
![Minecraft Version](https://img.shields.io/badge/Minecraft-1.20.x-green?style=for-the-badge&logo=minecraft)

</div>

---

## Über das Projekt

Dieses Repository enthält alle technischen Komponenten für den jährlichen **Weihnachts-Minecraft-Server** vom GymPW. Es dient als zentraler Speicherort für die serverseitige Logik sowie die Web-Integrationen.

### Kern-Features:
* **Server Info Panel:** Echtzeit-Datenabfrage des Serverstatus über das Web.
* **Jahresrückblick (Wrapped):** Eine interaktive Web-Übersicht, die Spielern ihre Statistiken, gebauten Blöcke und Erfolge des vergangenen Jahres zeigt.
* **Custom Plugins:** Spezielle Anpassungen für den Weihnachtsserver.

---

## Komponenten

Das Projekt umfasst nicht nur Plugins, sondern auch die komplette Server-Steuerung:

| Bereich | Beschreibung | Technologien |
| :--- | :--- | :--- |
| **Server** | Minecraft Plugins & Konfigurationen | Java (Spigot/Paper API) |
| **Start-Skipt** | Hochverfügbarkeit & Crash-Schutz | `start_server.sh` |
| **Wartung** | Automatischer Daily-Restart (04:00 Uhr) | `restart_server.sh` |
| **Datensicherheit** | Automatisierte Backups der Welt | `backup.sh` |
| **Web-Interface** | Dashboard & Jahresrückblick-Webseite | HTML/JS/TailwindCSS |

## Mitwirken

Beiträge von ehemaligen Schülern sind herzlich willkommen!
1. Forke das Projekt.
2. Erstelle einen Feature Branch (`git checkout -b feature/NeuesFeature`).
3. Committe deine Änderungen (`git commit -m 'Add some feature'`).
4. Pushe den Branch (`git push origin feature/NeuesFeature`).
5. Öffne einen Pull Request.

---

<div align="center">
  Erstellt mit ❤️ für die Schulgemeinschaft von Tom Coombs.
</div>