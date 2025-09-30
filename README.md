# PteroControl
Your Pterodactyl panel, now inside Minecraft.

<p align="center">
    <a href="https://fabricmc.net/"><img src="https://img.shields.io/badge/Requires-Fabric_API-blue?logo=fabric" alt="Requires Fabric API"></a>
    <a href="LINK_ZU_MODRINTH_HIER_EINSETZEN"><img src="https://img.shields.io/modrinth/v/MOD_ID_HIER_EINSETZEN?color=00AF5C&label=Modrinth&logo=modrinth" alt="Modrinth"></a>
    <a href="LINK_ZU_CURSEFORGE_HIER_EINSETZEN"><img src="https://img.shields.io/curseforge/v/PROJECT_ID_HIER_EINSETZEN?color=F16436&label=CurseForge&logo=curseforge" alt="CurseForge"></a>
</p>

PteroControl is a powerful, client-side Minecraft mod that seamlessly integrates your Pterodactyl game server panel directly into the multiplayer screen. Manage your servers without ever leaving the game!

![PteroControl Demo](media/MinecraftMultiplayerScreen.gif)

---

## ✨ Features

* **In-Game Controls:** Adds **Start**, **Stop**, and **Restart** buttons directly to your server entries in the multiplayer list.
* **Smart Display:** Buttons only appear for servers matching your configured Pterodactyl node address.
* **Dynamic Button States:** Buttons automatically enable or disable based on the server's live status (Online, Offline, or Pinging), preventing unwanted actions.
* **Auto-Refresh:** The button states update automatically a few seconds after sending a command, reflecting the server's new state.
* **Fully Configurable:** A comprehensive in-game settings screen allows you to configure your API endpoint, key, and node address (requires Mod Menu).

---

## 📥 Installation

1.  Make sure you have the [Fabric Loader](https://fabricmc.net/use/) installed.
2.  Download the latest release of PteroControl from the [Releases page](https://github.com/Partymann2000/PteroControlMC/releases). 3.  Place the downloaded `.jar` file in your `mods` folder.

### Required Dependencies

For PteroControl to work, you **must** also have the following mods installed:

* [**Fabric API**](https://modrinth.com/mod/fabric-api)
* [**Mod Menu**](https://modrinth.com/mod/modmenu)
* [**Cloth Config API**](https://modrinth.com/mod/cloth-config)

---

## ⚙️ Configuration

1.  Launch Minecraft and go to the **Mods** menu.
2.  Find **PteroControl** in the list and click the **gear icon** (⚙️).
3.  Enter your **Pterodactyl Panel URL** (e.g., `https://panel.yourdomain.com`).
4.  Enter your **Client API Key** (starts with `ptlc_`).
5.  Enter the **Node Address** of your servers (e.g., `node.yourdomain.com`).
6.  Click **Save** and you're done!

---

## 👨‍💻 Building from Source

1.  Clone the repository: `git clone https://github.com/Partymann2000/PteroControlMC.git`
2.  Navigate into the directory: `cd PteroControlMC`
3.  Build the JAR: `gradlew.bat build` (Windows) or `./gradlew build` (Linux/macOS).
4.  The compiled `.jar` will be in `build/libs/`.

---

## 📜 License

This project is licensed under the **[CC0-1.0 Universal](https://creativecommons.org/publicdomain/zero/1.0/)** license.