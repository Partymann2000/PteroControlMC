# Xenority Tools

![Lizenz](https://img.shields.io/badge/license-CC0--1.0-blue)
[![Modrinth](https://img.shields.io/badge/dynamic/json?color=00AF5C&label=Modrinth&query=versions[0]&url=https%3A%2F%2Fapi.modrinth.com%2Fv2%2Fproject%2FMOD_ID_HIER_EINSETZEN%2Fversion&logo=modrinth)](LINK_ZU_MODRINTH)
[![CurseForge](https://img.shields.io/curseforge/v/PROJECT_ID_HIER_EINSETZEN?color=F16436&label=CurseForge&logo=curseforge)](LINK_ZU_CURSEFORGE)

Ein kleiner, aber mächtiger Client-Mod für Minecraft, der die Verwaltung deiner Pterodactyl-Gameserver direkt in die Multiplayer-Serverliste integriert.

![Screenshot der Serverliste mit den neuen Buttons](https://i.imgur.com/your-screenshot-url.png)
*(Tipp: Mache einen Screenshot von deiner Mod im Spiel, lade ihn z.B. bei [Imgur](https://imgur.com/upload) hoch und ersetze den Link hier!)*

---

## ✨ Features

* Fügt **Start**, **Restart** und **Stop** Buttons zu Servereinträgen in der Multiplayer-Liste hinzu.
* **Intelligente Anzeige:** Die Buttons erscheinen nur bei Servern, die zu deiner konfigurierten Domain gehören (z.B. `node.xenority.com`).
* **Dynamischer Status:** Die Buttons werden automatisch aktiviert oder deaktiviert, basierend auf dem Live-Status des Servers in Minecraft (Online, Offline, Pinging).
    * Ist der Server offline, ist nur "Start" aktiv.
    * Ist der Server online, sind "Restart" und "Stop" aktiv.
    * Während der Server angepingt wird, sind alle Buttons deaktiviert.
* **Vollständig konfigurierbar** über ein Einstellungsmenü im Spiel (erfordert Mod Menu).

---

## 📥 Installation

1.  Stelle sicher, dass du den [Fabric Loader](https://fabricmc.net/use/) installiert hast.
2.  Lade die Mod von [Modrinth](LINK_ZU_MODRINTH) oder [CurseForge](LINK_ZU_CURSEFORGE) herunter.
3.  Platziere die heruntergeladene `.jar`-Datei in deinem `mods`-Ordner.

### Benötigte Abhängigkeiten (Dependencies)

Damit dieser Mod funktioniert, müssen die folgenden Bibliotheken ebenfalls in deinem `mods`-Ordner sein:

* [**Fabric API**](https://modrinth.com/mod/fabric-api)
* [**Mod Menu**](https://modrinth.com/mod/modmenu)
* [**Cloth Config API**](https://modrinth.com/mod/cloth-config)

---

## ⚙️ Konfiguration

Nach der Installation musst du die Mod konfigurieren, damit sie sich mit deinem Pterodactyl Panel verbinden kann.

1.  Starte Minecraft.
2.  Gehe im Hauptmenü auf `Mods`.
3.  Suche in der Liste nach `Xenority Tools` und klicke auf das **Zahnrad-Icon** (⚙️).
4.  Gib deine **Pterodactyl Panel URL** ein (z.B. `https://panel.xenority.com`).
5.  Gib deinen **Client API Key** ein (muss im Account-Bereich deines Panels erstellt werden, beginnt meist mit `ptlc_`).
6.  Speichere die Einstellungen.

Die Buttons erscheinen nun automatisch bei den richtigen Servern in deiner Multiplayer-Liste!

---

## 👨‍💻 Für Entwickler (Kompilieren)

Wenn du das Projekt selbst kompilieren möchtest:

1.  Klone das Repository:
    ```bash
    git clone [https://github.com/DEIN_USERNAME/DEIN_REPO_NAME.git](https://github.com/DEIN_USERNAME/DEIN_REPO_NAME.git)
    ```
2.  Navigiere in den Projektordner:
    ```bash
    cd DEIN_REPO_NAME
    ```
3.  Führe den Build-Befehl aus:
    * Für Windows: `gradlew.bat build`
    * Für Linux/macOS: `./gradlew build`
4.  Die fertige `.jar`-Datei findest du im Ordner `build/libs`.

---

## 📜 Lizenz

Dieses Projekt steht unter der **[CC0-1.0 Universal](https://creativecommons.org/publicdomain/zero/1.0/)** Lizenz. Das bedeutet, es ist Public Domain. Du kannst damit machen, was du möchtest – es kopieren, verändern, weiterverbreiten, ohne um Erlaubnis zu fragen.